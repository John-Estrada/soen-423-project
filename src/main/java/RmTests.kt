import rm.*
import util.Constants
import util.Udp
import java.net.SocketTimeoutException

fun main(args: Array<String>) {
    val replicas = ReplicaManager.startProcesses();

    ReplicaManager.stopRandomProcess();

    for (port in Constants.RM_PORTS) {
        try {
            Udp.send<Request>(port, Request(-1, ReplicaError(listOf(replicas[0]))), 1000)
        } catch (e: SocketTimeoutException) {
            // Give up
        }
    }

    ReplicaManager.stopRandomProcess();

    for (port in Constants.RM_PORTS) {
        try {
            Udp.send<Request>(port, Request(-1, ReplicaError(listOf(replicas[1]))), 1000)
        } catch (e: SocketTimeoutException) {
            // Give up
        }
    }
}