package main.java.sequencer

import rm.Method
import rm.Request
import util.Constants
import util.Udp
import java.io.Serializable
import java.net.SocketTimeoutException

class Sequencer(port: Int) {
    private var sendCounter = 0

    init {
        println("Sequencer running on port $port");
        Udp.listen<Method>(port) { req -> handleMethod(req) }
    }

    private fun handleMethod(req: Method): Serializable {
        // TODO: Change static ports to dynamic ports
        val rmRequest = Request(sendCounter, req);
        for (port in Constants.RM_PORTS) {
            try {
                Udp.send<Request>(port, rmRequest, 1000)
            } catch (e: SocketTimeoutException) {
                // Give up
            }
        }
        return sendCounter++
    }

}