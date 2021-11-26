package john.com.drrs.client;

import john.com.drrs.util.Log;
import john.com.drrs.util.Utility;
import john.com.drrs.ws.Primary;
import john.com.drrs.ws.PrimaryImplService;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Scanner;

public class StudentClient {
    public static void main(String[] args) throws Exception {
        String studentID = "DVLS0001";
        String params;
        Log log;

        PrimaryImplService primaryService = new PrimaryImplService();
        Primary primary = primaryService.getPrimaryImplPort();

        System.out.println("Student Client");
        boolean go = true;
        Scanner sc = new Scanner(System.in);
        String input = null;
        while (go) {
            System.out.println("Please Enter a choice:\n1. Book a room\n2. Get available time slots\n3. Cancel Booking\n4. Change booking\n5. quit\n");
            String choice = sc.nextLine();
            switch (choice) {
                case "1":
                    System.out.println("Book a room");
                    System.out.println("Enter a room number");
                    int roomNumber = Integer.parseInt(sc.nextLine());


                    System.out.println("Enter a date in ISO format");
                    String dateRep = sc.nextLine();

                    System.out.println("Enter a list of time slots separated by spaces");
                    String timeSlotsRep = sc.nextLine();

                    System.out.println("Enter the campus that the booking was on");
                    String campus = sc.nextLine();

                    String result = primary.bookRoom(studentID, campus, roomNumber, dateRep, timeSlotsRep);
                    System.out.println(String.format("Book room - id: %s, campus: %s, roomNumber: %s, dateRep: %s, timeSlotsRep: %s, result: %s", studentID, campus, roomNumber, dateRep, timeSlotsRep, result));
                    //updateLogs(studentID.substring(0, 3), studentID, Utility.getDateFromString(dateRep), "Book room", "test", result, result);
                    params = String.format(" RoomNumber: %s, Date: %s, TimeSlots: [%s]", roomNumber, dateRep, timeSlotsRep);
                    //updateLogs(adminID.substring(0,3), adminID, Utility.getDateFromString(dateRep), "Create Room", "test", result+"", result+"");
                    log = new Log(studentID.substring(0, 3), studentID, Utility.getDateFromString(dateRep), "BookRoom", params, result + "", result + "");
                    Utility.writeFile(log, studentID);
                    break;
                case "2":
                    System.out.println("Get Time slots");
                    System.out.println("Enter a date in ISO format");
                    String dateString = sc.nextLine();

                    int res = primary.getAvailableTimeSlots(dateString);
                    System.out.println(res);
                    params = String.format(" Date: %s", dateString);
                    //updateLogs(studentID.substring(0, 3), studentID, (new Date(System.currentTimeMillis())), "Get time slots", "test", ""+res, ""+res);
                    log = new Log(studentID.substring(0, 3), studentID, Utility.getDateFromString(dateString), "GetTimeSlots", params, res + "", res + "");
                    Utility.writeFile(log, studentID);
                    break;
                case "3":
                    System.out.println("Cancel Booking");
                    System.out.println("Enter your booking id");
                    String bookingID = sc.nextLine();

                    System.out.println("Enter the campus that the booking was on");
                    String campus2 = sc.nextLine();

                    boolean output = primary.cancelBooking(studentID, campus2, bookingID);
                    System.out.println(output);
                    params = String.format(" BookingID: %s, Campus: %s", bookingID, campus2);
                    //updateLogs(adminID.substring(0,3), adminID, Utility.getDateFromString(dateRep), "Create Room", "test", result+"", result+"");
                    log = new Log(studentID.substring(0, 3), studentID, new Date(System.currentTimeMillis()), "CancelBooking", params, output + "", output + "");
                    Utility.writeFile(log, studentID);
                    break;
                case "4":
                    System.out.println("Change booking");
                    System.out.println("Enter your booking id");
                    String bID = sc.nextLine();

                    System.out.println("What is the new campus?");
                    String newCampus = sc.nextLine();

                    System.out.println("What was the old campus?");
                    String oldCampus = sc.nextLine();

                    System.out.println("Enter the room number");
                    int newRoom = Integer.parseInt(sc.nextLine());

                    System.out.println("What time slot do you want to book?");
                    String newTimeSlotsRep = sc.nextLine();

                    boolean output1 = false;
                    try {
                        output1 = primary.changeReservation(studentID, bID, newCampus, oldCampus, newRoom, newTimeSlotsRep);
                    } catch (Exception e) {
                        System.out.println("Call from client failed");
                        e.printStackTrace();
                    }
                    System.out.println(output1);
                    params = String.format(" BookingID: %s, Campus: %s, OldCampus: %s, NewRoom: %s, NewTimeSlots: [%s]", bID, newCampus, oldCampus, newRoom, newTimeSlotsRep);
                    //updateLogs(adminID.substring(0,3), adminID, Utility.getDateFromString(dateRep), "Create Room", "test", result+"", result+"");
                    log = new Log(studentID.substring(0, 3), studentID, new Date(System.currentTimeMillis()), "CancelBooking", params, output1 + "", output1 + "");
                    Utility.writeFile(log, studentID);

                    //updateLogs(studentID.substring(0, 3), studentID, new Date(System.currentTimeMillis()), "Book room", "test", output1+"", output1+"");

                    break;
                case "5":
                    System.out.println("Quit");
                    go = false;
                    break;
            }
        }
    }


}
