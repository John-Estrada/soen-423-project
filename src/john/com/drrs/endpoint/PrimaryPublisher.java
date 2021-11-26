package john.com.drrs.endpoint;

import john.com.drrs.ws.PrimaryImpl;

import javax.xml.ws.Endpoint;

public class PrimaryPublisher {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:9999/ws/primary", new PrimaryImpl());
    }
}
