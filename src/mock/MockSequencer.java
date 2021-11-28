package mock;

import rm.Booking;
import rm.RoomRecord;

public class MockSequencer {
    int currentResponseId;
    public MockSequencer() {
        this.currentResponseId = 0;
    }

    public int createRoom(String adminId, int roomNumber, String date, String[] timeSlots) {
        return this.currentResponseId++;
    }

    public int deleteRoom(String adminId, int roomNumber, String date, String[] timeSlots) {
        return this.currentResponseId++;
    }

    // Student

    public int bookRoom(String studentId, String campus, int roomNumber, String date, String[] timeSlots) {
        return this.currentResponseId++;
    }

    public int cancelBooking(String studentId, String bookingId) {
        return this.currentResponseId++;
    }

    public int getAvailableTimeSlots(String date) {
       return this.currentResponseId++;
    }
}
