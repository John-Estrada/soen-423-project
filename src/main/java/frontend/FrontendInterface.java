package frontend;


import rm.Booking;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface FrontendInterface {
    // Admin
    @WebMethod boolean createRoom(String adminId, int roomNumber, String date, String[] timeSlots);
    @WebMethod boolean deleteRoom(String adminId, int roomNumber, String date, String[] timeSlots);

    // Student
    @WebMethod
    Booking bookRoom(String studentId, String campus, int roomNumber, String date, String[] timeSlots);
    @WebMethod boolean cancelBooking(String studentId, String bookingId);
    @WebMethod String getAvailableTimeSlots(String date);
}