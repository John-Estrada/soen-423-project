package frontend;

import main.java.sequencer.Sequencer;
import rm.*;
import util.Constants;
import util.Udp;

import javax.jws.WebService;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

@WebService(endpointInterface = "frontend.FrontendInterface")
public class FrontendImpl implements FrontendInterface {
    Semaphore timeOutMutex;
    int dummyReplicaId;
    ArrayList<ArrayList<AbstractMap.SimpleEntry<Integer, Response>>> responses;
    HashMap<Integer, AbstractMap.SimpleEntry<Long, Response>> timeoutQueue;
    Sequencer sequencer;

    public FrontendImpl() {
        timeOutMutex = new Semaphore(1);
        dummyReplicaId = 100;
        responses = new ArrayList<ArrayList<AbstractMap.SimpleEntry<Integer, Response>>>();
        timeoutQueue = new HashMap<Integer, AbstractMap.SimpleEntry<Long, Response>>();
        ReplicaManager.startProcesses();
        sequencer = new Sequencer(Constants.SEQUENCER_PORT);

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
        if (!timeoutQueue.containsKey(response.getId())) {
            timeoutQueue.put(response.getId(), new AbstractMap.SimpleEntry<Long, Response>(System.currentTimeMillis(), null));
        }

        //add space for new responses
        while (responses.size() < response.getId()) {
            responses.add(null);
        }

        //for handling null pointer exception
        try {
            responses.get(response.getId());
        } catch (Exception e) {
            responses.add(response.getId(), new ArrayList<AbstractMap.SimpleEntry<Integer, Response>>());
        }

        //add the new pair to this index's list of responses
        responses.get(response.getId()).add(new AbstractMap.SimpleEntry<>(dummyReplicaId++, response));

        //if all responses have been received, get the majority and put it in the timeout queue
        if (responses.get(response.getId()).size() >= Constants.NUMBER_OF_REPLICAS) {
            timeoutQueue.put(response.getId(), new AbstractMap.SimpleEntry<>(timeoutQueue.get(response.getId()).getKey(), getMajority(responses.get(response.getId()))));
        }

        timeOutMutex.release();
        return response;
    }

    //called when all replicas have given a response
    public Response getMajority(ArrayList<AbstractMap.SimpleEntry<Integer, Response>> allResponses) {
        //create an array with the number of responses equal to the response at this index
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

    //second method for iterating over the queue
    // wait for the specified request id, then return it for the method to get back to the frontend
    public Response waitForTimeoutQueue(int id) {
        while (true) {
            //wait until the toq contains the id of the process we are waiting for
            //acquire mutex
            //if timed out, handle timeout
            //if response ready, then return it so we can send it back to the client
            //release mutex

            if (!timeoutQueue.containsKey(id)) {
                Thread.yield();
            } else {
                try {
                    timeOutMutex.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (System.currentTimeMillis() - timeoutQueue.get(id).getKey() > Constants.TIMEOUT) {
                    handleTimeout(id);
                    timeOutMutex.release();
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
                    timeOutMutex.release();
                    return value;
                }

                timeOutMutex.release();
            }
        }
    }

    public void handleResponseReadyForClient(int key) {
        System.out.println("Response " + key + " ready");
        timeoutQueue.remove(key);
    }

    public void handleTimeout(int key) {
        ReplicaInfo[] replicaInfoObjects = new ReplicaInfo[Constants.NUMBER_OF_REPLICAS];

        for (int i : Constants.RM_PORTS) {
            try {
                // TODO: 2021-11-28 substitute for a dedicated liveness check method
                // using getAvailableTimeSlots for now because it doesn't modify anything
                Response r = Udp.send(i, new Request(999, new GetAvailableTimeslots("liveness check")));
            } catch (Exception e) {
                System.out.println("Replica " + i + " has failed - informing primary RM");
                replicaInfoObjects[i] = new ReplicaInfo(Constants.RM_PORTS[i], 0);
            }
        }

        Udp.send(Constants.RM_PORTS[Constants.PRIMARY_RM_ID], new Request(1000, new ReplicaError(replicaInfoObjects)));

        timeoutQueue.remove(key);
    }

    @Override
    public boolean createRoom(String adminId, int roomNumber, String date, String[] timeSlots) {
        String successMessage = "Rooms created";
        String failMessage = "Room already exists.";

        int requestId = Udp.send(Constants.SEQUENCER_PORT, new CreateRoom(adminId, roomNumber, date, timeSlots));

        Response r = waitForTimeoutQueue(requestId);
        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        return responseString.equals(successMessage);
    }

    @Override
    public boolean deleteRoom(String adminId, int roomNumber, String date, String[] timeSlots) {
        String successMessage = "Rooms deleted";

        int requestId = Udp.send(Constants.SEQUENCER_PORT, new DeleteRoom(adminId, roomNumber, date, timeSlots));

        Response r = waitForTimeoutQueue(requestId);

        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        return responseString.equals(successMessage);
    }

    @Override
    public Booking bookRoom(String studentId, String campus, int roomNumber, String date, String[] timeSlots) {
        String failMessage = "Room to book does not exist.";

        int requestId = Udp.send(Constants.SEQUENCER_PORT, new BookRoom(studentId, campus, roomNumber, date, timeSlots));

        Response r = waitForTimeoutQueue(requestId);

        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        if (!responseString.equals(failMessage)) return new Booking(new RoomRecord(), "", responseString);

        return new Booking();
    }

    @Override
    public boolean cancelBooking(String studentId, String bookingId) {
        String successMessage = "Booking cancelled";

        int requestId = Udp.send(Constants.SEQUENCER_PORT, new CancelBooking(studentId, bookingId));


        Response r = waitForTimeoutQueue(requestId);

        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        return responseString.equals(successMessage);
    }

    @Override
    public String getAvailableTimeSlots(String date) {
        int requestId = Udp.send(Constants.SEQUENCER_PORT, new GetAvailableTimeslots(date));
        Response r = waitForTimeoutQueue(requestId);
        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        if (r != null) return (String) r.getData();

        return null;
    }

    @Override
    public String NonPrimaryRMCrash(String date) {


        int requestId = Udp.send(Constants.SEQUENCER_PORT, new GetAvailableTimeslots(date));
        Response r = waitForTimeoutQueue(requestId);
        ReplicaManager.stopRandomProcess();  //make a rm crash
        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        if (r != null) return (String) r.getData();

        return null;
    }

    @Override
    public String twoRMsCrash(String date) {

        int requestId = Udp.send(Constants.SEQUENCER_PORT, new GetAvailableTimeslots(date));
        Response r = waitForTimeoutQueue(requestId);
        ReplicaManager.stopRandomProcess();

        ReplicaManager.stopRandomProcess();

        //ReplicaManager.corruptRandomProcess();
        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        if (r != null) return (String) r.getData();

        return null;
    }

    @Override
    public String Byzantine(String date) {
        int requestId = Udp.send(Constants.SEQUENCER_PORT, new GetAvailableTimeslots(date));
        Response r = waitForTimeoutQueue(requestId);
        ReplicaManager.corruptRandomProcess();    //make a rm have invalid data
        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        if (r != null) return (String) r.getData();

        return null;
    }

    @Override
    public String ByzantineAndRMCrash(String date) {
        int requestId = Udp.send(Constants.SEQUENCER_PORT, new GetAvailableTimeslots(date));
        Response r = waitForTimeoutQueue(requestId);
        ReplicaManager.corruptRandomProcess();    //make a rm have invalid data
        ReplicaManager.stopRandomProcess();     //make a rm crash
        String responseString = (String) r.getData();
        System.out.println(String.format("Response: %s", responseString));

        if (r != null) return (String) r.getData();

        return null;
    }


}
