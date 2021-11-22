package john.com.drrs.util;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

//id
//date
//roomNumber
//timeSlot

public class Booking implements Serializable {
    private String bookingID;
    private Date date;
    private int roomNumber;
    private int timeSlot;

    public Booking(String bookingID, Date date, int roomNumber, int timeSlot) {
        this.bookingID = bookingID;
        this.date = date;
        this.roomNumber = roomNumber;
        this.timeSlot = timeSlot;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public static String generateRandomID() {
        String output = "";
        output = (String) (""+Math.random()).split("\\.")[1].subSequence(0, 10);

        return output;
    }

    @Override
    public String toString() {
        // expected format "id yyyy-mm-dd 123 1"
        return String.format("%s %s-%s-%s %s %s", this.bookingID, this.date.getYear()+1900, this.date.getMonth(), this.date.getDay(), this.roomNumber, this.timeSlot);
    }
}
