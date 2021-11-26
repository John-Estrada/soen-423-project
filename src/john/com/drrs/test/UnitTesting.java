package john.com.drrs.test;


import john.com.drrs.ws.Primary;
import john.com.drrs.ws.PrimaryImplService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnitTesting {

    static PrimaryImplService primaryService;
    static Primary primary;
    String studentID = "DVLS0001";
    String adminID = "DVLA0001";
    String wrongStudentID = "DVLS9999";
    String wrongAdminID = "DVLA9999";

    @BeforeAll
    public static void initializeOrb() {
        try {
            primaryService = new PrimaryImplService();
            primary = primaryService.getPrimaryImplPort();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCreateRoom() {
        //correct implementation
        assertTrue(primary.createRoom(adminID, 111, "1990-1-1", "1 2 3"));

        //admin not in the list
        assertFalse(primary.createRoom("DVLA1234", 111, "1990-1-1", "1 2 3"));

        //admin id in wrong format
        assertFalse(primary.createRoom("D1234", 111, "1990-1-1", "1 2 3"));

        //date in wrong format
        assertFalse(primary.createRoom("DVLA0001", 111, "asdf", "1 2 3"));

//        //time slots in wrong format
        assertFalse(primary.createRoom("DVLA0001", 111, "1990-1-1", "123"));

//        //time slots empty
        assertFalse(primary.createRoom("DVLA0001", 111, "1990-1-1", ""));
//
//        //time slots too long
        assertFalse(primary.createRoom("DVLA0001", 111, "1990-1-1", "0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16"));
    }

    @Test
    void testDeleteRoom() {
        //correct format
        assertTrue(primary.createRoom(adminID, 111, "1990-1-1", "1 2 3"));
        assertTrue(primary.deleteRoom(adminID, 111, "1990-1-1", "1 2 3"));

        //admin not in list

        //wrong room number
        assertTrue(primary.createRoom(adminID, 111, "1990-1-1", "1 2 3"));
//        assertFalse(primary.deleteRoom(adminID, 444, "1990-1-1", "1 2 3"));
        primary.deleteRoom(adminID, 111, "1990-1-1", "1 2 3");

        //wrong date
        assertTrue(primary.createRoom(adminID, 111, "1990-1-1", "1 2 3"));
        assertFalse(primary.deleteRoom(adminID, 111, "1992-1-1", "1 2 3"));
        primary.deleteRoom(adminID, 111, "1990-1-1", "1 2 3");

        //bad time slot format
        assertTrue(primary.createRoom(adminID, 111, "1990-1-1", "1 2 3"));
        assertFalse(primary.deleteRoom(adminID, 111, "1990-1-1", "126354"));
        primary.deleteRoom(adminID, 111, "1990-1-1", "1 2 3");

        //bad date format
        assertTrue(primary.createRoom(adminID, 111, "1990-1-1", "1 2 3"));
        assertFalse(primary.deleteRoom(adminID, 111, "asdf", "1 2 3"));
        primary.deleteRoom(adminID, 111, "1990-1-1", "1 2 3");
    }

    @Test
    void testBookRoom() {
        char first = 'a';

        //correct method
        primary.createRoom(adminID, 111, "1990-1-1", "1 2 3");
        first = primary.bookRoom(studentID, "DVL", 111, "1990-1-1", "1").charAt(0);
        assertTrue(first <= '9' && first >= '0');   //workaround for checking for a correct bookingID format (all numbers)

        //room does not exist
        first = primary.bookRoom(studentID, "DVL", 222, "1990-1-1", "1").charAt(0);
        assertFalse(first <= '9' && first >= '0');

        //date does not have any available rooms
        first = primary.bookRoom(studentID, "DVL", 111, "1992-1-1", "2").charAt(0);
        assertFalse(first <= '9' && first >= '0');

        //date in bad format
        first = primary.bookRoom(studentID, "DVL", 111, "asdf", "1").charAt(0);
        assertFalse(first <= '9' && first >= '0');

        //time slot is not available
        first = primary.bookRoom(studentID, "DVL", 111, "1990-1-1", "1").charAt(0);
        assertFalse(first <= '9' && first >= '0');

        //time slots in bad format
        first = primary.bookRoom(studentID, "DVL", 111, "1990-1-1", "asdf").charAt(0);
        assertFalse(first <= '9' && first >= '0');

        //incorrect campus
        first = primary.bookRoom(studentID, "WST", 111, "1990-1-1", "1").charAt(0);
        assertFalse(first <= '9' && first >= '0');

        //campus in bad format
        first = primary.bookRoom(studentID, "21easdf", 111, "1990-1-1", "1").charAt(0);
        assertFalse(first <= '9' && first >= '0');

        //student not in list
        first = primary.bookRoom("sadfasdf", "DVL", 111, "1990-1-1", "1").charAt(0);
        assertFalse(first <= '9' && first >= '0');
    }

    @Test
    void testCancelBooking() {
        primary.createRoom(adminID, 222, "1990-1-1", "1 2 3 4 5");
        String bid = primary.bookRoom(studentID, "DVL", 222, "1990-1-1", "1 2");

        //correct implementation
        assertTrue(primary.cancelBooking(studentID, "DVL", bid));

        //rebook to repeat testing
        bid = primary.bookRoom(studentID, "DVL", 222, "1990-1-1", "1 2");


        //wrong campus
        assertFalse(primary.cancelBooking(studentID, "KKL", bid));

        //student does not exist
//        assertFalse(primary.cancelBooking("asdf", "DVL", bid));

        //wrong booking id
        assertFalse(primary.cancelBooking(studentID, "DVL", "124124"));
    }

    @Test
    void testChangeBooking() {
        //changeReservation(studentID, bID, newCampus, oldCampus, newRoom, newTimeSlotsRep)
        primary.createRoom(adminID, 333, "1990-1-1", "1 2 3 4 5");
        primary.createRoom(adminID, 444, "1990-1-1", "1 2 3 4 5");

        String bid = primary.bookRoom(studentID, "DVL", 333, "1990-1-1", "1 2");


        assertTrue(primary.changeReservation(studentID, bid, "DVL", "DVL", 444, "1 2"));

        //booking id doesn't exist
        assertFalse(primary.changeReservation(studentID, "abc", "DVL", "DVL", 444, "1 2"));

        //student id wrong
        assertFalse(primary.changeReservation("AAAA", bid, "DVL", "DVL", 444, "1 2"));
    }
}