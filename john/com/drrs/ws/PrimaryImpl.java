package com.drrs.ws;

import com.drrs.util.Booking;
import com.drrs.util.Utility;

import javax.jws.WebService;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

@WebService(endpointInterface = "com.drrs.ws.Primary")
public class PrimaryImpl implements Primary {
    private final HashMap<String, Secondary> servers;

    private final Semaphore bookingMutex;
    private final Semaphore createRoomMutex;
    private final Semaphore deleteRoomMutex;
    private final Semaphore changeReservationMutex;
    private final Semaphore cancelReservationMutex;

    public PrimaryImpl() {
        SecondaryImplService service = new SecondaryImplService();
        Secondary secondary = service.getSecondaryImplPort();

        SecondaryImplService serviceDVL = new SecondaryImplService();
        Secondary dvl = serviceDVL.getSecondaryImplPort();

        SecondaryImplService serviceWST = new SecondaryImplService();
        Secondary wst = serviceWST.getSecondaryImplPort();

        SecondaryImplService serviceKKL = new SecondaryImplService();
        Secondary kkl = serviceKKL.getSecondaryImplPort();

        servers = new HashMap<String, Secondary>();

        servers.put("default", secondary);
        servers.put("DVL", dvl);
        servers.put("WST", wst);
        servers.put("KKL", kkl);

        bookingMutex = new Semaphore(1);
        cancelReservationMutex = new Semaphore(1);
        changeReservationMutex = new Semaphore(1);
        createRoomMutex = new Semaphore(1);
        deleteRoomMutex = new Semaphore(1);
    }

    public String test(String x) {
        SecondaryImplService service = new SecondaryImplService();
        Secondary secondary = service.getSecondaryImplPort();

        String out = secondary.secondaryTest();
        return "Result: " + out;
    }

    public boolean createRoom(String id, int roomNumber, String dateRep, String timeSlotsRep) {
        try {
            createRoomMutex.acquire();
            try {
                String whichServer = id.substring(0, 3);
                if (!Utility.getValidServerNames().contains(whichServer)) {
                    System.out.println("Admin id is not in the correct format");
                    return false;
                }
                System.out.println(whichServer);

                System.out.println("Hello create room");
                Secondary sv = getServer(whichServer);

                return sv.createRoom(id, roomNumber, dateRep, timeSlotsRep);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                createRoomMutex.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRoom(String id, int roomNumber, String dateRep, String timeSlotsRep) {
        try {
            deleteRoomMutex.acquire();
            try {
                String whichServer = id.substring(0, 3);
                if (!Utility.getValidServerNames().contains(whichServer)) {
                    System.out.println("Admin id is not in the correct format");
                    return false;
                }
                Secondary sv = getServer(whichServer);

                return sv.deleteRoom(id, roomNumber, dateRep, timeSlotsRep);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                deleteRoomMutex.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String bookRoom(String id, String campus, int roomNumber, String dateRep, String timeSlotsRep) {
        try {
            bookingMutex.acquire();
            try {
                System.out.println("Hello booking room");   //debug
                if (!Utility.getValidServerNames().contains(campus)) {
                    System.out.println("Campus name is not in the correct format");
                    return "Invalid campus";
                }
                Secondary sv = getServer(campus);

                return sv.bookRoom(id, campus, roomNumber, dateRep, timeSlotsRep);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                bookingMutex.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "This room could not be booked";
    }

    @Override
    public int getAvailableTimeSlots(String dateRep) {
        Secondary sv = getServer("DVL");    // TODO: 2021-10-20 implement multiple servers

        return sv.getAvailableTimeSlots(dateRep);
    }

    @Override
    public boolean cancelBooking(String id, String campus, String bookingID) {
        try {
            cancelReservationMutex.acquire();
            try {
                if (!Utility.getValidServerNames().contains(campus)) {
                    System.out.println("Campus name is not in the correct format");
                    return false;
                }
                Secondary sv = getServer(campus);

                boolean result = false;

                try {
                    result = sv.cancelBooking(id, campus, bookingID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return result;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cancelReservationMutex.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean changeReservation(String id, String bookingID, String newCampus, String oldCampus, int newRoom, String newtimeSlotsRep) {
        try {
            changeReservationMutex.acquire();
            try {
                System.out.println("Begin res change");

                //getBooking(bookingID)
                //if booking exists
                    //try to book new
                    //if successful, cancel old
                    //else, return failure

                System.out.println("GETTING BOOKING");
                Secondary sv = getServer(newCampus);
                boolean result = sv.changeReservation(id, bookingID, newCampus, oldCampus, newRoom, newtimeSlotsRep);
                System.out.println("GOT BOOKING");

                return result;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                changeReservationMutex.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private Secondary getServer(String name) {
        return servers.get(name);
    }
}
