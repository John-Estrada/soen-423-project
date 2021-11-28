package client;

import frontend.FrontendInterface;
import john.com.drrs.util.Booking;
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

        ClientUtility cu = new ClientUtility(); //cannot be static because of scanner usage?
        StringBuilder sb = new StringBuilder();

        sb.append("Choose a method: \n");
        sb.append("1. Book a Room \n");
        sb.append("2. Cancel Booking \n");
        sb.append("3. Get available time slots \n");
        sb.append("4. Quit \n");
        String message = sb.toString();

        Udp.listen(Constants.DEBUG_PORT, reply -> {
            System.out.println(reply);
            return null;
        });

        // JAX-WS setup for interacting with the FrontEnd
        URL wsdlUrl = new URL(Constants.FRONTEND_WEBSERVICE_URL + "?wsdl");
        QName qname = new QName(Constants.NAMESPACE_URI, Constants.SERVICE);  // TODO: 2021-11-27 change these to the real service uri
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
                    request = cu.buildRequest("bookRoom", id, sc);
                    booking = frontend.bookRoom(id, request.getCampus(), request.getRoomNumber(), request.getDate(), request.getTimeSlots());
                    System.out.println(String.format("Your booking Id is: %s", booking.getBookingID()));
                    break;

                case "2":
                    request = cu.buildRequest("cancelBooking", id, sc);
                    result = frontend.cancelBooking(id, request.getBookingId());
                    System.out.println(result);
                    break;
                case "3":
                    request = cu.buildRequest("getAvailableTimeSlots", id, sc);
                    output = frontend.getAvailableTimeSlots(request.getDate());
                    System.out.println(output);
                    break;

                case "4":
                    go = false;
                    System.out.println("Quitting");
                    break;
            }
            System.out.println("Request done");
        }
    }
}
