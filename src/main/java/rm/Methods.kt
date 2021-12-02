package rm

import java.io.Serializable

interface Method : Serializable;

// Admin methods
data class CreateRoom(val adminId: String, val roomNumber: Int, val date: String, val timeslots: Array<String>) :
    Method;
data class DeleteRoom(val adminId: String, val roomNumber: Int, val date: String, val timeslots: Array<String>) :
    Method;

// Student methods
data class BookRoom(
    val studentId: String,
    val campus: String,
    val roomNumber: Int,
    val date: String,
    val timeslots: Array<String>
) : Method;
data class CancelBooking(val studentId: String, val bookingId: String) : Method;
data class GetAvailableTimeslots(val date: String) : Method;

// RM stuff
/** Send a map of all replicas and their ports to a replica */
data class ReplicaMap(val map: List<ReplicaInfo>) : Method
data class ReplicaError(val replicas: List<ReplicaInfo>) : Method {
    constructor(replicas: Array<ReplicaInfo>) : this(replicas.toList()) {}
};
data class DeclareRm(val port: Int, val threadId: Long) : Method;
data class RestoreData(val backup: Database) : Method;

class KillProcess : Method;
class CorruptProcess : Method;

data class ReplicaInfo(val port: Int, val threadId: Long) : Serializable;