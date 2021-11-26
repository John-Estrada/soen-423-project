package mock;

import rm.*;
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
}
