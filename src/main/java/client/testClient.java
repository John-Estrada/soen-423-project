package client;

import frontend.FrontendInterface;
import rm.Booking;
import util.ClientUtility;
import util.Constants;
import util.Udp;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class testClient {
    public static void main(String[] args) throws MalformedURLException {
        String id = "DVLS1111";

        // test getavailabletimeslot from studentClient
        ClientUtility clientUtility = new ClientUtility();
        StringBuilder sb = new StringBuilder();

        sb.append("Choose a method: \n");
        sb.append("1. Non-primary RM crashes \n");
        sb.append("2. Two RMs crash \n");
        sb.append("3. Byzantine Error \n"); // assume there is 1 rm send incorrect message and 3 send correct message
        sb.append("4. Simultaneous Byzantine Error and Crash \n");

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
            String output;
            Booking booking;

            switch (userChoice) {
                case "1":
                    request = clientUtility.buildRequest("getAvailableTimeSlots", id, sc);

                    //output = frontend.getAvailableTimeSlots(request.getDate());
                    output = frontend.NonPrimaryRMCrash(request.getDate());
                    System.out.println(String.format("There are %s time slots available", output));

                    break;

                case "2":
                    request = clientUtility.buildRequest("getAvailableTimeSlots", id, sc);
                    //output = frontend.getAvailableTimeSlots(request.getDate());
                    output = frontend.twoRMsCrash(request.getDate());
                    System.out.println(String.format("There are %s time slots available", output));

                    break;
                case "3":
                    request = clientUtility.buildRequest("getAvailableTimeSlots", id, sc);
                    //output = frontend.getAvailableTimeSlots(request.getDate());
                    output = frontend.Byzantine(request.getDate());
                    System.out.println(String.format("There are %s time slots available", output));

                    break;

                case "4":
                    request = clientUtility.buildRequest("getAvailableTimeSlots", id, sc);
                    //output = frontend.getAvailableTimeSlots(request.getDate());
                    output = frontend.ByzantineAndRMCrash(request.getDate());
                    System.out.println(String.format("There are %s time slots available", output));

                    break;
            }
        }
    }
}