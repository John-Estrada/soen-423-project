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

public class StudentClient {
    public static void main(String[] args) throws MalformedURLException {
        String id = "DVLS1111";

        // set up client utility and looped message
        ClientUtility clientUtility = new ClientUtility();
        StringBuilder sb = new StringBuilder();

        sb.append("Choose a method: \n");
        sb.append("1. Book a Room \n");
        sb.append("2. Cancel Booking \n");
        sb.append("3. Get available time slots \n");
        sb.append("4. Quit \n");

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
                    request = clientUtility.buildRequest("bookRoom", id, sc);
                    booking = frontend.bookRoom(id, request.getCampus(), request.getRoomNumber(), request.getDate(), request.getTimeSlots());

                    if (booking.getBookingId().equals("")) {
                        System.out.println("The room could not be booked.");
                    } else {
                        System.out.println(String.format("Your booking Id is: %s", booking.getBookingId()));
                    }

                    break;

                case "2":
                    request = clientUtility.buildRequest("cancelBooking", id, sc);
                    result = frontend.cancelBooking(id, request.getBookingId());

                    if (result) {
                        System.out.println("Your booking was successfully cancelled");
                    } else {
                        System.out.println("The booking could not be cancelled.");
                    }

                    break;
                case "3":
                    request = clientUtility.buildRequest("getAvailableTimeSlots", id, sc);
                    output = frontend.getAvailableTimeSlots(request.getDate());

                    System.out.println(String.format("There are %s time slots available", output));

                    break;

                case "4":
                    go = false;
                    System.out.println("Quitting");

                    break;
            }
        }
    }
}
