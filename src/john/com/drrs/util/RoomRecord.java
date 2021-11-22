package john.com.drrs.util;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RoomRecord implements Serializable {
    private String id;
    private Date date;
    private int roomNumber;
    private List<Integer> availableTimes;
    private String bookedBy; //student id
    private HashMap<Integer, String> bookedTimes;

    private String[] __timeSlots;


    public RoomRecord(String id) throws RemoteException {
        super();
        this.id = id;
    }

    public RoomRecord(String id, Date date, List<Integer> availableTimes, String bookedBy) throws RemoteException {
        super();
        this.id = id;
        this.date = date;
        this.availableTimes = availableTimes;
        if (bookedBy != null) this.bookedBy = bookedBy;
        this.__timeSlots = new String[12];
    }

    public RoomRecord(int roomNumber, Date date, String[] timeSlots) throws Exception {
        super();
        this.id = "RR" + roomNumber;
        this.roomNumber = roomNumber;
        this.date = date;
        if (timeSlots.length != 12){
            throw new Exception("Time slots must be size 12");
        } else {
            this.__timeSlots = timeSlots;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean book(String studentId, int timeSlot) {
        if (!availableTimes.contains(timeSlot)) {
            System.out.println("This time slot is not available");
            return false;
        } else {
            availableTimes.remove(timeSlot);
            bookedTimes.put(timeSlot, studentId);
        }

        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: " + id + "\n");
        sb.append("Date: " + this.date.toString() + "\n");
        if (this.__timeSlots!= null) sb.append("Available Times: " + Arrays.toString(this.__timeSlots) + "\n");

        String output = sb.toString();

        return output;
    }

    public boolean __addTimeSlots(String[] newTimeSlots) {
        if (newTimeSlots.length != 12) {
            System.out.println("The time slots array must be of size 12");
            return false;
        }

        for (int i = 0; i<this.__timeSlots.length; i++) {   //if the time slot is null, set it to the value in the new time slots array
            if (this.__timeSlots[i] ==  null) {
                this.__timeSlots[i] = newTimeSlots[i];
            }
        }

        return true;
    }

    public boolean __removeTimeSlots(String[] newTimeSlots) {
        System.out.println("RR removing time slots");
        if (newTimeSlots.length != 12) {
            System.out.println("Array must be of length 12");
            return false;
        }

        System.out.println("RR passed length check");

        for (int i =0; i < newTimeSlots.length; i++) {
            if (newTimeSlots[i] != null && newTimeSlots[i].equals("Delete")) {
                if (this.__timeSlots[i] == null || this.__timeSlots[i].equals("Available")) {   // if the slot is null or available, delete it
                    this.__timeSlots[i] = null;
                } else {    //if a student has booked the slot, unbook them before deleting the slot
                    System.out.println("Implement unbooking student");
                    this.__timeSlots[i] = null;
                }
            }
        }

        System.out.println("RR finished removing time slots");

        return true;
    }

    public boolean __bookRoom(String studentID, int timeSlot) {
        if (this.__timeSlots[timeSlot].equals("Available")) {
            this.__timeSlots[timeSlot] = studentID;
            return true;
        }
        return false;
    }

    public int __getAvailableTimeSlots() {
        int output = 0;
        for (String s : this.__timeSlots) {
            if (s != null) {
                if (s.equals("Available")) output++;
            }
        }

        return output;
    }

    public boolean __cancelBooking(String bookingID) {
        System.out.println("Booking - start cancel");
        System.out.println(bookingID);
        for (int i = 0; i < __timeSlots.length; i++ ) {
            if (__timeSlots[i] != null) {
                if (__timeSlots[i].contains(bookingID)) {
                    __timeSlots[i] = "Available";
                    return true;
                }
            }
        }
        return false;
    }

    public void updateID(int id) {
        this.__timeSlots[id] = "updated";
    }
}
