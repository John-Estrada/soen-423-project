package client;

import frontend.FrontendInterface;
import util.ClientUtility;
import util.Constants;
import util.Udp;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class AdminClient {
    public static void main(String[] args) throws MalformedURLException, InterruptedException {
        String id = "DVLA1111";

        // set up client utility and looped message
        ClientUtility clientUtility = new ClientUtility(); //cannot be static because of scanner usage?
        StringBuilder sb = new StringBuilder();

        sb.append("Choose a method: \n");
        sb.append("1. Create Room \n");
        sb.append("2. Delete Room \n");
        sb.append("3. Quit \n");

        String message = sb.toString();

        // JAX-WS setup for interacting with the FrontEnd
        URL wsdlUrl = new URL(Constants.FRONTEND_WEBSERVICE_URL + "?wsdl");
        QName qname = new QName(Constants.NAMESPACE_URI, Constants.SERVICE);
        Service service = Service.create(wsdlUrl, qname);
        FrontendInterface frontend = service.getPort(FrontendInterface.class);

        boolean go = true;
        while (go) {
            System.out.println(message);
            Scanner sc = new Scanner(System.in);
            String userChoice = sc.nextLine();
            ClientUtility.Request request;
            boolean result;

            switch (userChoice) {
                case "1":
                    request = clientUtility.buildRequest("createRoom", id, sc);
                    result = frontend.createRoom(request.getId(), request.getRoomNumber(), request.getDate(), request.getTimeSlots());

                    if (result) {
                        System.out.println("Room Record successfully created");
                    } else {
                        System.out.println("This room record could not be created");
                    }

                    break;

                case "2":
                    request = clientUtility.buildRequest("deleteRoom", id, sc);
                    result = frontend.deleteRoom(request.getId(), request.getRoomNumber(), request.getDate(), request.getTimeSlots());

                    if (result) {
                        System.out.println("Room successfully deleted");
                    } else {
                        System.out.println("This room could not be deleted");
                    }

                    break;

                case "3":
                    go = false;
                    System.out.println("Quitting");
                    break;
            }
        }
    }
}
