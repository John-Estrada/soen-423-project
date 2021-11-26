package mock;

import javafx.util.Pair;
import rm.Response;
import util.Constants;
import util.Udp;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Semaphore;

public class MockFE {
    static Semaphore timeOutMutex = new Semaphore(1);
    static int dummyReplicaId = 100;    // TODO: 2021-11-25 replace this with real replica id - included in the response?
    static ArrayList<ArrayList<Pair<Integer, Response>>> responses = new ArrayList<ArrayList<Pair<Integer, Response>>>();
    static HashMap<Integer, Pair<Long, Response>> timeoutQueue = new HashMap<Integer, Pair<Long, Response>>();

    // TODO: 2021-11-25 handle forwarding client requests to the sequencer

    public static void main(String[] args) {
        Udp.listen(Constants.FRONTEND_PORT, (response) -> {
            return handleRequest((Response) response);
        });

        new Thread(MockFE::watchTimeoutQueue).start();
    }

    //this method needs to return a serializable for some reason - I think it has to do with the Udp.listen kotlin code but I don't know enough about kotlin to fix this
    public static Serializable handleRequest(Response response) {
        try {
            timeOutMutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //if the response id is not already in the queue, add it with value null (to indicate that the response is not ready)
        if (!timeoutQueue.containsKey(response.getId())) {
            timeoutQueue.put(response.getId(), new Pair<Long, Response>(System.currentTimeMillis(), null));
        }

        //add space for new responses
        while(responses.size() < response.getId()) {
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
    public static Response getMajority(ArrayList<Pair<Integer, Response>> allResponses) {
        //create an array with the number of responses equal to the response at this index
        // TODO: 2021-11-25 probably change this to a hashmap with <Response, count>, but hashCode() and equals() not working after deserialization
        int[] counts = new int[allResponses.size()];

        //count the number of equal responses and add them to the array
        for (int i = 0; i<allResponses.size(); i++) {
            for (int j = 0; j < allResponses.size(); j++) {
                if (i!=j) {
                    if (allResponses.get(i).getValue().equals(allResponses.get(j).getValue())) {
                        counts[i]++;
                    }
                }
            }
        }

        //iterate through counts to find the response with the highest count of other responses equal to it - choose this as output for the client
        int highest = 0;
        int chosenIndex = 0;
        for (int i = 0; i< counts.length; i++) {
            if (counts[i] > highest) {
                highest = counts[i];
                chosenIndex = i;
            }
        }

        return allResponses.get(chosenIndex).getValue();
    }

    //iterate over timeout queue - when response is no longer null, call handleResponseReadyForClient. if response is still null when timeout exceeded, call handleTimeout
    public static void watchTimeoutQueue() {
        while(true){
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

    public static void handleResponseReadyForClient(int key) {
        System.out.println("Response " + key + " ready");
        // TODO: 2021-11-25 implement return to client
        timeoutQueue.remove(key);
    }

    public static void handleTimeout(int key) {
        System.out.println(key + " timed out");
        // TODO: 2021-11-25 implement informing primary replica of a failure
        timeoutQueue.remove(key);
    }
}
