package sample;

import java.util.List;
import java.util.Objects;

/**
 * Created by kifkif on 20/03/2017.
 */
public class Utils {
    public static int getPort(String port)
    {
        return Integer.valueOf(port);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static Message buildMessage(List<String> lines) throws Exception
    {
        Message message = new Message();

        String[] parts = lines.get(0).split(" ");

        if(!parts[0].toUpperCase().equals("+OK"))
            throw new Exception("Error while getting message from server");
        lines.remove(0);

        StringBuilder content = new StringBuilder();
        boolean lastLineDot = false;

        boolean header = true;
        for (String line : lines) {
            if(header && !line.equals(""))
            {
                parts = line.split(":", 2);
                if(parts.length == 2)
                    message.addHeader(parts[0], parts[1]);
            }
            else
            {

                if(header && line.equals(""))
                    header = false;
                else
                {
                    if(line.equals("."))
                        lastLineDot = true;
                    else if(line.equals("") && lastLineDot)
                        break;
                    else
                    {
                        content.append(line).append("\r\n");
                        lastLineDot = false;
                    }
                }
            }
        }

        System.out.println("message content : "+content.toString());
        message.setMessage(content.toString());

        return message;
    }
}
