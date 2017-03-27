package sample;

import javafx.beans.InvalidationListener;

import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

/**
 * Created by kifkif on 13/03/2017.
 */
public class Client extends Observable {

    private String server;
    private int port;
    private String username;
    private String password;
    private SocketFactory socketFactory;
    private Socket socket;

    private BufferedReader in;
    private DataOutputStream out;
    private boolean connected;

    final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };
    private int nbrTry;
    private String timestamp;
    private Integer nbrMessages;

    public static Client getInstance()
    {
        if(instance == null)
            instance = new Client();
        return instance;
    }

    private static Client instance;

    protected Client()
    {
        this.socketFactory = SSLSocketFactory.getDefault();
        this.connected = false;
        this.nbrTry = 0;
        this.nbrMessages = 0;
    }

    public boolean connect(String server, String port, String username, String password)
    {
        connect(server, port);

        return processConnection();
    }

    public boolean connect(String server, String port)
    {
        this.server = server;
        this.port = Utils.getPort(port);

        try {
            SSLSocket sslSocket = (SSLSocket)socketFactory.createSocket(this.server, this.port);
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
            sslSocket.startHandshake();
            socket = sslSocket;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        initBuffers();

        List<String> lines = read();

        String[] parts = lines.get(0).split(" ");
        if(parts[0].toUpperCase().equals("+OK"))
        {
            timestamp = parts[parts.length-1];
            connected = true;
            this.nbrTry = 0;
            return true;
        }
        else 
            return false;
    }

    public boolean login(String username, String password)
    {
        if(connected) {
            this.username = username;
            this.password = password;

            if(processConnection())
                return true;
            else
            {
                nbrTry++;
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean quit()
    {
        if(connected) {
            try {
                send("QUIT");
                List<String> lines = read();
                String[] parts = lines.get(0).split(" ");

                return parts[0].toUpperCase().equals("+OK");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private boolean processConnection()
    {
        byte[] controlCrypt = new byte[0];
        try {
            controlCrypt = MessageDigest.getInstance("MD5").digest(timestamp.concat(password).getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
        String encryptPassword = Utils.bytesToHex(controlCrypt).toLowerCase();
        try {
            send("APOP "+username+" "+encryptPassword);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        List<String> lines = read();
        String[] parts = lines.get(0).split(" ");

        if(parts[0].toUpperCase().equals("+OK"))
        {
            if(parts[1].toLowerCase().equals("maildrop") && parts[2].toLowerCase().equals("has"))
            {
                nbrMessages = Integer.valueOf(parts[3]);
            }
            return true;
        }
        else return false;
    }

    public int stats()
    {
        try {
            send("STAT");
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        List<String> lines = read();
        String[] parts = lines.get(0).split(" ");

        if(parts[0].toUpperCase().equals("+OK"))
        {
            if(parts[1].toLowerCase().equals("maildrop") && parts[2].toLowerCase().equals("has"))
            {
                nbrMessages = Integer.valueOf(parts[3]);
            }
            return nbrMessages;
        }
        else return 0;
    }

    public void initBuffers()
    {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e)
        {
            e.printStackTrace();
            log(e.getMessage());
            close();
        }
    }

    public void close()
    {
        try
        {
            socket.close();
        } catch (IOException e)
        {
            log("Error during closing : "+e.getMessage());
            e.printStackTrace();
        }
        System.exit(0);
    }

    public List<Message> getMessages()
    {
        List<Message> messages = new ArrayList<>();

        for (int i = 0; i < this.nbrMessages; i++) {
            try {
                send("RETR "+(i+1));
                try {
                    messages.add(Utils.buildMessage(read()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return messages;
    }

    public void log(String m)
    {
        System.out.println(m);
    }

    public void writeLine(String message) throws IOException
    {
        write(message+"\r\n");//CR LF
    }

    public void write(String message) throws IOException
    {
        out.writeBytes(message);
    }

    public void send(String message) throws IOException
    {
        if(message != null && !message.equals(""))
            writeLine(message);
        writeLine("");
        writeLine("");
        out.flush();
        log("sending message");
    }

    public void send() throws IOException
    {
        send("");
    }

    public List<String> read()
    {
        String line;
        List<String> lines = new ArrayList<String>();

        try
        {
            log("waiting message");
            boolean reading = true;
            int nullLine = 0;
            boolean head = true;
            while(reading)
            {
                line = in.readLine();
                System.out.println("line:"+line);
                if(line == null)
                {
                    reading = false;
                    close();
                }
                else if(line.length() > 0)
                {
                    lines.add(line);
                    nullLine = 0;
                    head = false;
                }
                else if(!head)
                {
                    nullLine++;
                    if(nullLine > 1)
                        reading = false;
                    else lines.add(line);
                }
            }

            if(lines.size() > 0)
            {
                log("message received : "+lines.get(0)+"...");
            }

        } catch (IOException e)
        {
            //e.printStackTrace();
            log(e.getMessage());
        }

        return lines;
    }

    public int getTry() {
        return 4-nbrTry;
    }
}
