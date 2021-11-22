package john.com.drrs.endpoint;

import com.drrs.ws.SecondaryImpl;

import javax.xml.ws.Endpoint;

public class SecondaryPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9998/ws/secondary", new SecondaryImpl());
    }
}
