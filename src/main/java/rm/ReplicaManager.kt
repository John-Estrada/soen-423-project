package rm

import util.Constants
import util.Udp
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class ReplicaManager(private val port: Int, private var db: Database = Database()) {
    var replicas: List<ReplicaInfo> = ArrayList<ReplicaInfo>();
    var currentId: Int = 0;

    companion object {
        // Only used for debug
        private val replicas = ArrayList<ReplicaInfo>()

        @JvmStatic
        fun startProcesses(): List<ReplicaInfo> {
            val replicaInfos = ArrayList<ReplicaInfo>()
            for (port in Constants.RM_PORTS) {
                val info = startProcess(port);
                replicaInfos.add(info);
            }

            for (replica in replicaInfos) {
                while (true) {
                    try {
                        Udp.send<String>(replica.port, Request(-1, ReplicaMap(replicaInfos)), 500);
                        break;
                    } catch (e: SocketTimeoutException) {
                        continue;
                    }
                }
            }

            return replicaInfos;
        }

        @JvmStatic
        fun startProcess(port: Int): ReplicaInfo {
            val t = thread { ReplicaManager(port); }
            replicas.add(ReplicaInfo(port, t.id))
            return ReplicaInfo(port, t.id);
        }

        @JvmStatic
        fun stopProcess(processId: Long) {
            replicas.removeIf { it.threadId == processId }
            for (thread in Thread.getAllStackTraces().keys) {
                if (thread.id == processId) {
                    thread.interrupt();
                    while (thread.isAlive) Thread.yield();
                    break;
                } else continue;
            }
        }

        @JvmStatic
        fun stopRandomProcess() {
            val threadId = replicas[1].threadId
            val port = replicas[1].port
            stopProcess(threadId);
            println("Killed RM $port")
        }





        @JvmStatic
        fun corruptRandomProcess() {
            val threadId = replicas[0].threadId
            val port = replicas[0].port
            replicas.removeIf { it.threadId == threadId }
            Udp.send<String>(port, Request(-1, CorruptProcess()))
            println("Corrupted RM $port")
        }
    }

    init {
        println("RM running on port $port");
        Udp.listenSync<Request>(port) { req -> handleRequest(req) }
    }

    private fun handleRequest(request: Request): Response {
        val (id, method) = request;
        var data: String;
        currentId = id;

        try {
            data = when (method) {
                // Admin
                is CreateRoom -> db.createRoom(method);
                is DeleteRoom -> db.deleteRoom(method);

                // Student
                is BookRoom -> db.bookRoom(method);
                is CancelBooking -> db.cancelBooking(method);
                is GetAvailableTimeslots -> db.getAvailableTimeslots(method);

                // RM stuff
                is ReplicaMap -> mapReplicas(method);
                is ReplicaError -> handleReplicaError(method);
                is RestoreData -> restoreData(method);
                is DeclareRm -> {
                    val (port, threadId) = method;
                    val newReplicaInfo = ArrayList<ReplicaInfo>()
                    newReplicaInfo.addAll(replicas)
                    newReplicaInfo.add(ReplicaInfo(port, threadId));
                    replicas = newReplicaInfo;
                    if (replicas.size == 4) {
                        val response = Response(id, "RM restored")
                        println("Sent response to FE")
                        Udp.send<String>(Constants.FRONTEND_PORT, response);
                    }
                    "OK"
                };

                // Debug stuff
                is KillProcess -> {
                    Thread.currentThread().interrupt();
                    while (Thread.currentThread().isAlive) Thread.yield();
                    "OK"
                }

                is CorruptProcess -> {
                    this.db = Database()
                    "OK"
                }

                else -> throw Error("Request method doesn't exist!")
            }
        } catch (e: Exception) {
            if (e.message != null) {
                data = e.message!!
            } else {
                data = "Unknown error"
            }
        }

        val response = Response(id, data)

        if (id !== -1) {
            Udp.send<String>(Constants.FRONTEND_PORT, response);
        }

        return response;
    }

    private fun mapReplicas(args: ReplicaMap): String {
        replicas = args.map;
        return "OK";
    }

    private fun restoreData(args: RestoreData): String {
        val (backup) = args;
        db = backup;

        return "OK"
    }

    private fun handleReplicaError(args: ReplicaError): String {
        val (errorReplicas) = args

        // Remove error replicas from list of replicas
        replicas = replicas.filter { !errorReplicas.contains(it) };

        // Find if it's the primary replica
        var isPrimary = (getPrimaryPort() == port);

        if (isPrimary) {
            val newReplicas = ArrayList<ReplicaInfo>()
            newReplicas.addAll(replicas);
            println("Found primary RM $port");
            // Fix broken replicas
            for (errorReplica in errorReplicas) {
                var attempt = 0
                var process: ReplicaInfo = errorReplica;
                while (true) {
                    // First, try to restore it
                    try {
                        Udp.send<String>(errorReplica.port, Request(-1, ReplicaMap(replicas)), 1000);
                        Udp.send<String>(errorReplica.port, Request(-1, RestoreData(db)));
                        newReplicas.add(process);
                        break;
                    } catch (e: SocketTimeoutException) {
                        if (attempt % 3 == 0) {
                            // Else, kill it and restart it
                            stopProcess(errorReplica.threadId);
                            process = startProcess(errorReplica.port);
                        }
                        attempt += 1
                        continue
                    }
                }
            }
            replicas = newReplicas;

            for (replica in replicas) {
                if (replica.port == this.port) continue;
                while (true) {
                    try {
                        Udp.send<String>(replica.port, Request(-1, ReplicaMap(replicas)), 500);
                        break;
                    } catch (e: SocketTimeoutException) {
                        continue;
                    }
                }
            }
        }

        return "OK"
    }

    private fun getPrimaryPort(): Int {
        var primary = replicas[0].port
        for (replica in replicas) {
            if (replica.port < primary) {
                primary = replica.port;
            }
        }
        return primary
    }

}


