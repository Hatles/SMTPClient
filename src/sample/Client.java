package sample;

import javafx.beans.InvalidationListener;

import javax.net.SocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
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

    final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };

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
    }

    public boolean connect(String server, String port, String username, String password)
    {
        this.server = server;
        this.port = Utils.getPort(port);
        this.username = username;
        this.password = password;

        try {
            SSLSocket sslSocket = (SSLSocket)socketFactory.createSocket(this.server, this.port);
            sslSocket.setEnabledCipherSuites(enabledCipherSuites);
            socket = sslSocket;
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }
        initBuffers();

        return processConnection();
    }

    private boolean processConnection() {
        List<String> lines = read();

        return true;
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
            while(reading)
            {
                line = in.readLine();
                log("read line: " + line);
                if(line == null)
                {
                    reading = false;
                    close();
                }
                else if(line.length() > 0)
                {
                    lines.add(line);
                }
                else
                    reading = false;
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
}
