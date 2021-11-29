package main.java.sequencer

import rm.Method
import rm.Request
import util.Constants
import util.Udp
import java.io.Serializable

class Sequencer (val port: Int) {
    private var sendCounter = 0

    init {
        Udp.listen<Method>(port) { req -> handleMethod(req) }
    }

    private fun handleMethod(req: Method): Serializable {
        // TODO: Change static ports to dynamic ports
        for (port in Constants.RM_PORTS){
            var builtRequest = Request(sendCounter, req)
           Udp.send<Request>(port,builtRequest)
        }
        return sendCounter++
    }

}