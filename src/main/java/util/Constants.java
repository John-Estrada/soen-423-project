package util;

public class Constants {
    public static final int TIMEOUT = 5000;
    public static final int NUMBER_OF_REPLICAS = 4;

    // Ports
    public static final int FRONTEND_PORT = 6789;
    public static final int SEQUENCER_PORT = 9000;
    public static final int DEBUG_PORT = 8889;
    // TODO: 2021-11-27 web service url published on port 8888 - fix this
    public static final int[] RM_PORTS = {8000, 8001, 8002, 8003};
    public static int PRIMARY_RM_ID = 0;    //use this to get the primary rm's port

    // URLs
    public static final String FRONTEND_WEBSERVICE_URL = "http://localhost:8888/webservice/helloworld";
    public static final String MOCK_NAMESPACE_URI = "http://mock/";
    public static final String MOCK_SERVICE = "MockFEJaxImplService";

    public static final String NAMESPACE_URI = "http://frontend/";
    public static final String SERVICE = "FrontendImplService";

    public static int currentResponseId = 0;
}
