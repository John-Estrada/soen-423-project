package rm
import java.io.Serializable

class RoomRecord(val roomNumber: Int=0, val date: String="", val timeslot: String=""): Serializable {
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is RoomRecord) return false
        return other.roomNumber == roomNumber && other.date == date && other.timeslot == timeslot
    }
}

