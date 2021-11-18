package com.drrs.ws;

import com.drrs.util.Booking;
import com.drrs.util.RoomRecord;
import com.drrs.util.Utility;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@WebService(endpointInterface = "com.drrs.ws.Secondary")
public class SecondaryImpl implements Secondary {
    private static HashMap<Date, HashMap<Integer, RoomRecord>> records;
    private HashMap<String, Booking> bookings;

    private ArrayList<String> admins;
    private ArrayList<String> students;

    public SecondaryImpl() {
        initHashMaps();
    }

    public static HashMap<Date, HashMap<Integer, RoomRecord>> getRecords() {
        return records;
    }

    public String secondaryTest() {
        return "Text from secondary";
    }

    @Override
    public String bookRoom(String id, String campus, int roomNumber, String dateRep, String timeSlotsRep) {
        if (!students.contains(id)) {
            System.out.println("This student is not in the list");
            return "This student is not in the list";
        }
        System.out.println("Second booking room");  //debug
        String output = "This room could not be booked";
        Date date = null;
        try {
            date = Utility.getDateFromString(dateRep);
        } catch (Exception e) {
            e.printStackTrace();
            return "Date is in incorrect format";
        }
        String[] timeSlots = new String[0];
        try {
            timeSlots = Utility.getTimeSlotsFromString(timeSlotsRep, "Book");
        } catch (Exception e) {
            e.printStackTrace();
            return "Time slots in bad format";
        }

        //book(id, timeslot)

        //for slot in array,
        //book the slot if it is 'book', else do nothing

        //if date in map
        //if room number in map
        //book the rooms
        //else
        //return false
        //return false
        String idStore = "";

        if (records.containsKey(date)) {
            System.out.println("Second contains date"); //debug
            if (records.get(date).containsKey(roomNumber)) {
                for (int i = 0; i < timeSlots.length; i++) {
                    if (timeSlots[i] != null) {
                        if (timeSlots[i].equals("Book")) {
                            System.out.println("TimeSlot is Book");
                            String bookingID = Booking.generateRandomID();

                            if (!records.get(date).get(roomNumber).__bookRoom(String.format("%s-%s", id, bookingID), i))
                                return "Time slot already booked";
                            bookings.put(bookingID, new Booking(bookingID, date, roomNumber, i));
                            output = bookingID;
                            System.out.println(String.format("i: %s, booked", i));
                            idStore = bookingID;
                        }
                    }
                }
            }
        }

        System.out.println("Second done booking room");
        Booking test = bookings.get(idStore);
        System.out.println(String.format("Booked under %s", idStore));
        if (test != null) {
            System.out.println(test.toString());
        } else {
            System.out.println("Test booking is null");
        }

        return output;
    }

    @Override
    public int getAvailableTimeSlots(String dateRep) {
        int output = 0;
        Date date = null;
        try {
            date = Utility.getDateFromString(dateRep);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (records.containsKey(date)) {
            if (records.get(date).size() > 0) {
                for (int i : records.get(date).keySet()) {
                    output += records.get(date).get(i).__getAvailableTimeSlots();
                }
            }
        }
        return output;
    }

    @Override
    public boolean cancelBooking(String id, String campus, String bookingID) {
        if (!students.contains(id)) {
            System.out.println("This student is not in the list");
            return false;
        }
        System.out.println("Second cancelling booking - begin");
        //find bookingID in bookings map
        //check if the date and room num is in the main map
        //cancel and remove it from both maps
        boolean result = false;

        if (bookings.containsKey(bookingID)) {
            Booking b = bookings.get(bookingID);
            if (records.containsKey(b.getDate())) {
                if (records.get(b.getDate()).containsKey(b.getRoomNumber())) {
                    result = records.get(b.getDate()).get(b.getRoomNumber()).__cancelBooking(bookingID);
                    if (result) {
                        bookings.remove(bookingID);
                        System.out.println("Second successfully cancelled booking");
                        return true;
                    }
                    System.out.println("Second did not cancel booking");
                }
            }
        }
        return result;
    }

    @Override
    public boolean changeReservation(String id, String bookingID, String newCampus, String oldCampus, int newRoom, String newtimeSlotsRep) {
        System.out.println("SECONDARY CHANGING RES");
        System.out.println(String.format("id: %s, bookingID: %s, newCampus: %s, oldCampus: %s, newRoom, %s, newTimeSLots: %s", id, bookingID, newCampus, oldCampus, newRoom, newtimeSlotsRep));

        //if booking exists
            //try to book new one
            //then delete the old one and mark it as available

        if (bookings.containsKey(bookingID)) {
            try {
                Date bookingDate = bookings.get(bookingID).getDate();
                String dateRep = String.format("%s-%s-%s", bookingDate.getYear()+1900, bookingDate.getMonth()+1, bookingDate.getDay());
                System.out.println("DEBUG DATE REP " + dateRep);
                String newBookingID = this.bookRoom(id, newCampus, newRoom, dateRep, newtimeSlotsRep);
                if (!newBookingID.equals("This room could not be booked")) {
                    boolean cancelled = this.cancelBooking(id, oldCampus, bookingID);
                    return cancelled;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public String getBooking(String bookingID) {
        return bookings.get(bookingID).toString();
    }

    public boolean createRoom(String id, int roomNumber, String dateRep, String timeSlotsRep) {
        if (!admins.contains(id)) {
            System.out.println("This admin is not in the list");
            return false;
        }

        System.out.println("%%%% admin in the list");

        System.out.println("Second Create room");

        Date date = null;
        try {
            date = Utility.getDateFromString(dateRep);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String[] timeSlots = new String[0];

        try {
            timeSlots = Utility.getTimeSlotsFromString(timeSlotsRep, "Create");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Time slots not in correct format");
            return false;
        }

        System.out.println(Arrays.toString(timeSlots));
        System.out.println(date);

        RoomRecord rr = null;
        try {
            rr = new RoomRecord(roomNumber, date, timeSlots);
            System.out.println("Room Record successfully created"); //debug
        } catch (Exception exception) {
            System.out.println("Error creating room record");   //debug
            exception.printStackTrace();
        }
        System.out.println("Finished creating rr");

        //if date exists
        //if room number exists
        //get record and update time slots
        //else
        //put (room number, record)
        //else
        // put new record at this date, and put new record at this room num

        System.out.println("Starting records check");   //debug
        if (records.containsKey(date)) {
            System.out.println("contains date");
            if (records.get(date).containsKey(roomNumber)) {
                System.out.println("Contains room number");
                records.get(date).get(roomNumber).__addTimeSlots(timeSlots);
            } else {
                System.out.println("no room number");
                records.get(date).put(roomNumber, rr);
            }
            System.out.println("Date already here");    //debug
        } else {
            records.put(date, new HashMap<Integer, RoomRecord>());
            records.get(date).put(roomNumber, rr);
            System.out.println("Date inserted" +
                    "");   //debug
        }
        System.out.println("Second done creating room");

        return true;
    }

    public boolean deleteRoom(String id, int roomNumber, String dateRep, String timeSlotsRep) {
        if (!admins.contains(id)) {
            System.out.println("This admin is not in the list");
            return false;
        }

        System.out.println("Second delete room");   //debug
        boolean output = false;

        Date date = null;
        try {
            date = Utility.getDateFromString(dateRep);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        String[] timeSlots = new String[0];
        try {
            timeSlots = Utility.getTimeSlotsFromString(timeSlotsRep, "Delete");
        } catch (Exception e) {
            System.out.println("Time slots in bad format");
            e.printStackTrace();
            return false;
        }
        System.out.println("TS array");
        System.out.println(Arrays.toString(timeSlots));

        //if date in map
        //if room number exists
        //remove the selected time slots
        //else
        //return false
        //else
        //return false

        System.out.println("Checking for keys");    //debug
        if (records.containsKey(date)) {
            System.out.println("contains date");    //debug
            if (records.get(date).containsKey(roomNumber)) {
                System.out.println("contains room num");    //debug
                if (records.get(date).get(roomNumber) == null) {
                    System.out.println("RR is null");
                } else {
                    System.out.println(records.get(date).get(roomNumber).toString());
                }
                output = records.get(date).get(roomNumber).__removeTimeSlots(timeSlots);
            }
        }

        System.out.println("Second completed room deletion");

        return output;
    }

    public void initHashMaps() {
        records = new HashMap<Date, HashMap<Integer, RoomRecord>>();
        bookings = new HashMap<String, Booking>();
        admins = new ArrayList<String>();
        students = new ArrayList<String>();

        admins.add("DVLA0001");
        admins.add("DVLA0002");

        students.add("DVLS0001");
        students.add("DVLS0002");
    }

    public HashMap<String, Booking> getBookings() {
        return bookings;
    }
}
