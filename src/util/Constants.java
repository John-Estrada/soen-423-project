package util;

public class Constants {
    public static final int TIMEOUT = 5000;
    public static final int NUMBER_OF_REPLICAS = 4;

    // Ports
    public static final int FRONTEND_PORT = 6789;
    public static final int DEBUG_PORT = 8889;
    // TODO: 2021-11-27 web service url published on port 8888 - fix this

    // URLs
    public static final String FRONTEND_WEBSERVICE_URL = "http://localhost:8888/webservice/helloworld";
    public static final String MOCK_NAMESPACE_URI = "http://mock/";
    public static final String MOCK_SERVICE = "MockFEJaxImplService";

    public static final String NAMESPACE_URI = "http://frontend/";
    public static final String SERVICE = "FrontendImplService";

    public static int currentResponseId = 0;
}
