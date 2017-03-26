package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kifkif on 13/03/2017.
 */
public class Message {
    private List<Header> headers;
    private String message;

    public Message(List<Header> headers, String message)
    {
        this.headers = headers;
        this.message = message;
    }

    public Message()
    {
        this(new ArrayList<Header>(), "undefined");
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public void addHeader(Header header)
    {
        this.headers.add(header);
    }

    public void addHeader(String title, String value)
    {
        this.addHeader(new Header(title, value));
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHeader(String headerTitle) throws Exception {
        for (Header header: headers) {
            if (header.getTitle().equals(headerTitle))
                return header.getValue();
        }

        throw new Exception("No such header.");
    }

    @Override
    public String toString() {
        return "Message{" +
                "headers=" + headers +
                ", message='" + message + '\'' +
                '}';
    }
}
