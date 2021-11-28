package frontend;

import javafx.util.Pair;
import john.com.drrs.util.Booking;
import mock.MockRM;
import mock.MockSequencer;
import rm.Response;
import util.Constants;
import util.Udp;

import javax.jws.WebService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

@WebService(endpointInterface = "frontend.FrontendInterface")
public class FrontendImpl implements FrontendInterface {
    Semaphore timeOutMutex;
    int dummyReplicaId;
    ArrayList<ArrayList<Pair<Integer, Response>>> responses;
    HashMap<Integer, Pair<Long, Response>> timeoutQueue;
    MockSequencer mockSequencer;

    public FrontendImpl() {
        timeOutMutex = new Semaphore(1);
        dummyReplicaId = 100;    // TODO: 2021-11-25 replace this with real replica id - included in the response?
        responses = new ArrayList<ArrayList<Pair<Integer, Response>>>();
        timeoutQueue = new HashMap<Integer, Pair<Long, Response>>();
        mockSequencer = new MockSequencer();    // TODO: 2021-11-27 replace this with the real sequencer

        Udp.listen(Constants.FRONTEND_PORT, (response) -> {
            return handleRequest((Response) response);
        });
    }

    //this method needs to return a serializable for some reason - I think it has to do with the Udp.listen kotlin code but I don't know enough about kotlin to fix this
    public Serializable handleRequest(Response response) {
        try {
            timeOutMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //if the response id is not already in the queue, add it with value null (to indicate that the response is not ready)
        // TODO: 2021-11-27 move this to handling reply from sequencer after request forwarded
        if (!timeoutQueue.containsKey(response.getId())) {
            timeoutQueue.put(response.getId(), new Pair<Long, Response>(System.currentTimeMillis(), null));
        }

        //add space for new responses
        while (responses.size() < response.getId()) {
            responses.add(null);
        }

        //for handling null pointer exception
        try {
            responses.get(response.getId());
        } catch (Exception e) {
            responses.add(response.getId(), new ArrayList<Pair<Integer, Response>>());
        }

        // TODO: 2021-11-25 use a real replica id here
        //add the new pair to this index's list of responses
        responses.get(response.getId()).add(new Pair<>(dummyReplicaId++, response));

        //if all responses have been received, get the majority and put it in the timeout queue
        if (responses.get(response.getId()).size() >= Constants.NUMBER_OF_REPLICAS) {
            timeoutQueue.put(response.getId(), new Pair<>(timeoutQueue.get(response.getId()).getKey(), getMajority(responses.get(response.getId()))));
        }

        timeOutMutex.release();
        return response;
    }

    //called when all replicas have given a response
    public Response getMajority(ArrayList<Pair<Integer, Response>> allResponses) {
        //create an array with the number of responses equal to the response at this index
        // TODO: 2021-11-25 probably change this to a hashmap with <Response, count>, but hashCode() and equals() not working after deserialization
        int[] counts = new int[allResponses.size()];

        //count the number of equal responses and add them to the array
        for (int i = 0; i < allResponses.size(); i++) {
            for (int j = 0; j < allResponses.size(); j++) {
                if (i != j) {
                    if (allResponses.get(i).getValue().equals(allResponses.get(j).getValue())) {
                        counts[i]++;
                    }
                }
            }
        }

        //iterate through counts to find the response with the highest count of other responses equal to it - choose this as output for the client
        int highest = 0;
        int chosenIndex = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > highest) {
                highest = counts[i];
                chosenIndex = i;
            }
        }

        return allResponses.get(chosenIndex).getValue();
    }

    //iterate over timeout queue - when response is no longer null, call handleResponseReadyForClient. if response is still null when timeout exceeded, call handleTimeout
    public void watchTimeoutQueue() {
        while (true) {
            if (timeoutQueue.size() == 0) {
                Thread.yield();
            }

            try {
                timeOutMutex.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            for (Integer key : timeoutQueue.keySet()) {
                if (System.currentTimeMillis() - timeoutQueue.get(key).getKey() > Constants.TIMEOUT) {
                    handleTimeout(key);
                }

                //needed to handle null pointer exceptions
                Response value = null;
                try {
                    value = timeoutQueue.get(key).getValue();
                } catch (Exception e) {
                    System.out.println("Value is null - handle timeout");
                }
                if (value != null) {
                    handleResponseReadyForClient(key);
                }
            }

            timeOutMutex.release();
        }
    }

    //second method for iterating over the queue
    // wait for the specified request id, then return it for the method to get back to the frontend
    public Response waitForTimeoutQueue(int id) {
        while (true) {
            //wait until the toq contains the id of the process we are waiting for
            //acquire mutex
            //if timed out, handle timeout
            //if response ready, then return it so we can send it back to the client

            if (!timeoutQueue.containsKey(id)) {
                Thread.yield();
            } else {
                try {
                    d("acquiring mutex");
                    timeOutMutex.acquire();
                    d("acquired");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (System.currentTimeMillis() - timeoutQueue.get(id).getKey() > Constants.TIMEOUT) {
                    d("handling timeout");
                    handleTimeout(id);
                    return null;
                }

                Response value = null;
                try {
                    value = timeoutQueue.get(id).getValue();
                } catch (Exception e) {
                    System.out.println("Value is null - handle timeout");
                }
                if (value != null) {
                    value = timeoutQueue.get(id).getValue();
                    handleResponseReadyForClient(id);
                    d("release mutex value not null");
                    timeOutMutex.release();
                    return value;
                }

                d("release mutext value null");
                timeOutMutex.release();
            }
        }
    }

    public void handleResponseReadyForClient(int key) {
        System.out.println("Response " + key + " ready");
        timeoutQueue.remove(key);
    }

    public void handleTimeout(int key) {
        System.out.println(key + " timed out");
        // TODO: 2021-11-25 implement informing primary replica of a failure
        timeoutQueue.remove(key);
    }

    @Override
    public String test(String str) {
        Udp.send(Constants.DEBUG_PORT, new Response(1, "Test data"));
        return "Replay from the impl : " + str;
    }

    @Override
    public boolean createRoom(String adminId, int roomNumber, String date, String[] timeSlots) {
        int requestId = mockSequencer.createRoom(adminId, roomNumber, date, timeSlots); // TODO: 2021-11-27 replace with real sequencer

        // debug
        Udp.send(Constants.DEBUG_PORT, new Response(requestId, String.format("Received createRoom request with parameters: adminId: %s, roomNumber: %s, date: %s, timeSlots: %s", adminId, roomNumber, date, timeSlots)));

        // debug
        MockRM.sendMockResponse(requestId, Constants.NUMBER_OF_REPLICAS+1);

        Response r = waitForTimeoutQueue(requestId);

        // debug
        Udp.send(Constants.DEBUG_PORT, r);

        if (r!=null) return true;

        return false;
    }

    @Override
    public boolean deleteRoom(String adminId, int roomNumber, String date, String[] timeSlots) {
        int requestId = mockSequencer.deleteRoom(adminId, roomNumber, date, timeSlots); // TODO: 2021-11-27 replace with real sequencer

        MockRM.sendMockResponse(requestId, Constants.NUMBER_OF_REPLICAS);

        Response r = waitForTimeoutQueue(requestId);

        if (r!=null) return true;

        return false;
    }

    @Override
    public Booking bookRoom(String studentId, String campus, int roomNumber, String date, String[] timeSlots) {
        int requestId = mockSequencer.bookRoom(studentId, campus, roomNumber, date, timeSlots); // TODO: 2021-11-27 replace with real sequencer

        MockRM.sendMockResponse(requestId, Constants.NUMBER_OF_REPLICAS);

        Response r = waitForTimeoutQueue(requestId);

        return null;
    }

    @Override
    public boolean cancelBooking(String studentId, String bookingId) {
        int requestId = mockSequencer.cancelBooking(studentId, bookingId);

        MockRM.sendMockResponse(requestId, Constants.NUMBER_OF_REPLICAS);

        Response r = waitForTimeoutQueue(requestId);

        if (r!=null) return true;

        return false;
    }

    @Override
    public String getAvailableTimeSlots(String date) {
        int requestId = mockSequencer.getAvailableTimeSlots(date);

        MockRM.sendMockResponse(requestId, Constants.NUMBER_OF_REPLICAS);

        Response r = waitForTimeoutQueue(requestId);

        return null;
    }

    private void d(String s) {
        System.out.println(s);
    }
}
