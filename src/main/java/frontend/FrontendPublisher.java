package frontend;

import util.Constants;

import javax.xml.ws.Endpoint;

public class FrontendPublisher {
    public static void main(String[] args) {
        Endpoint.publish(Constants.FRONTEND_WEBSERVICE_URL, new FrontendImpl());
    }
}
