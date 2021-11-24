package rm

import util.Udp
import java.io.Serializable
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class ReplicaManager(val port: Int, val id: Int, private var replica: Database = Database()) {

    init {
        Udp.listenSync<Request>(port) { req -> handleRequest(req) }
    }

    companion object {
        @JvmStatic
        fun startProcess(id: Int, port: Int): Thread {
            return thread { ReplicaManager(id, port); }
        }
    }

    private fun restoreData(args: RestoreData): Boolean {
        val (backup) = args;
        replica = backup;
        return true
    }

    private fun handleReplicaError(args: ReplicaError): Serializable {
        val (replicas) = args

        // Find if it's the primary replica
        var isPrimary = true;
        for (i in 0..id) {
            if (replicas.find { it.id == i } != null) continue;
            isPrimary = false;
            break;
        }

        // Fix broken replicas
        for (replica in replicas) {
            try {
                val ok = Udp.send<String>(replica.port, RestoreData(this.replica), 1000);
            } catch (e: SocketTimeoutException) {
                // TODO: Handle process failure
            }

        }

        return "OK"
    }

    private fun handleRequest(request: Request): Response {
        val (id, method) = request;
        var data: Serializable;

        try {
            data = when (method) {
                // Admin
                is CreateRoom -> replica.createRoom(method);
                is DeleteRoom -> replica.deleteRoom(method);

                // Student
                is BookRoom -> replica.bookRoom(method);
                is CancelBooking -> replica.cancelBooking(method);
                is GetAvailableTimeslots -> replica.getAvailableTimeslots(method);

                // RM stuff
                is ReplicaError -> handleReplicaError(method);
                is RestoreData -> restoreData(method);

                // Debug stuff
                is KillProcess -> {
                    Thread.currentThread().interrupt();
                    true
                }

                else -> throw Error("Request method doesn't exist!")
            }
        } catch (e: Exception) {
            data = e;
        }

        return Response(id, data);
    }
}


