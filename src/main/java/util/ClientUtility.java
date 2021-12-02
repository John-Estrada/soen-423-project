package util;

import java.util.Scanner;

public class ClientUtility {
    public Request buildRequest(String requestType, String id, Scanner sc) {
        Request request = null;

        int roomNumber;
        String date;
        String[] timeSlots;
        String campus;
        String bookingId;

        switch (requestType) {
            case "createRoom":
            case "deleteRoom":
                System.out.println("Enter a room number\n");
                roomNumber = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter a date\n");   // TODO: 2021-11-27 specify date format
                date = sc.nextLine();

                System.out.println("Enter the time slots\n");
                timeSlots = sc.nextLine().split(" ");

                request = new Request().withId(id).withRoomNumber(roomNumber).withDate(date).withTimeSlots(timeSlots);

                break;

            case "bookRoom":
                System.out.println("Enter a room number\n");
                roomNumber = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter a campus\n");
                campus = sc.nextLine();

                System.out.println("Enter a date\n");   // TODO: 2021-11-27 specify date format
                date = sc.nextLine();

                System.out.println("Enter the time slots\n");
                timeSlots = sc.nextLine().split(" ");

                request = new Request().withId(id).withRoomNumber(roomNumber).withDate(date).withTimeSlots(timeSlots).withCampus(campus);

                break;
            case "cancelBooking":
                System.out.println("Enter the booking Id of the request you would like to cancel\n");
                bookingId = sc.nextLine();

                request = new Request().withId(id).withBookingId(bookingId);
                break;
            case "getAvailableTimeSlots":
                System.out.println("Enter the date\n");
                date = sc.nextLine();

                request = new ClientUtility.Request().withId(id).withDate(date);
                break;
            default:
                request = new Request();
                break;
        }
        return request;
    }

    // For use by the buildRequest method
    public class Request {
        String id;
        int roomNumber;
        String date;
        String[] timeSlots;
        String campus;
        String bookingId;

        public Request withId(String id) {
            this.id = id;
            return this;
        }

        public Request withRoomNumber(int roomNumber) {
            this.roomNumber = roomNumber;
            return this;
        }

        public Request withDate(String date) {
            this.date = date;
            return this;
        }

        public Request withTimeSlots(String[] timeSlots) {
            this.timeSlots = timeSlots;
            return this;
        }

        public Request withCampus(String campus) {
            this.campus = campus;
            return this;
        }

        public Request withBookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public String getId() {
            return id;
        }

        public int getRoomNumber() {
            return roomNumber;
        }

        public String getDate() {
            return date;
        }

        public String[] getTimeSlots() {
            return timeSlots;
        }

        public String getCampus() {
            return campus;
        }

        public String getBookingId() {
            return bookingId;
        }
    }
}
