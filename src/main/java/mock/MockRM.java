package mock;

import rm.*;
import util.Constants;
import util.Udp;

public class MockRM {
    public static void main(String[] args) {
        int responseId = 0;
        for (int i = 0; i < 10; i++) {
            if (i%3 == 0) responseId++;
            Response re = new Response(responseId, new RoomRecord(123, "12-12-2020", "timeslotstring"));
            Udp.send(6789, re);
        }
    }

    public static void sendMockResponses() {
        int responseId = 0;
        for (int i = 0; i < 10; i++) {
            if (i%3 == 0) responseId++;
            Response re = new Response(responseId, new RoomRecord(123, "12-12-2020", "timeslotstring"));
            Udp.send(6789, re);
        }
    }

    public static void sendMockResponse(int id, int count) {
        d(String.format("MockRM starting, id: %s, count: %s", id, count));
        // TODO: 2021-11-27 replica id
        int i = 0;
        while(i < count){
            Response re = new Response(id, new RoomRecord(123, "12-12-2020", "timeslotstring"));
            Udp.send(Constants.FRONTEND_PORT, re);
            i++;
        }
        d("MockRM done");
    }

    static public void d(String s) {
        System.out.println(s);
    }
}
