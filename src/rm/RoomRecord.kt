package rm

class RoomRecord(val roomNumber: Int, val date: String, val timeslot: String) {
    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is RoomRecord) return false
        return other.roomNumber == roomNumber && other.date == date && other.timeslot == timeslot
    }
}

