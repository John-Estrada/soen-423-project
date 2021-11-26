package rm

import java.io.Serializable
import java.util.*

class Database : Serializable {
    private var rooms = ArrayList<RoomRecord>()
    private var bookings = ArrayList<Booking>()

    // ================================
    // Admin methods
    // ================================
    fun createRoom(args: CreateRoom): Boolean {
        val (adminId, roomNumber, date, timeslots) = args

        for (timeslot in timeslots) {
            val room = RoomRecord(roomNumber, date, timeslot)
            assert(!rooms.contains(room), { "Room already exists." })
        }

        for (timeslot in timeslots) {
            val room = RoomRecord(roomNumber, date, timeslot)
            rooms.add(room)
        }

        return true;
    }

    fun deleteRoom(args: DeleteRoom): Boolean {
        val (adminId, roomNumber, date, timeslots) = args

        for (timeslot in timeslots) {
            val room = RoomRecord(roomNumber, date, timeslot)
            assert(rooms.contains(room), { "Room to delete does not exist." })
        }

        for (timeslot in timeslots) {
            val room = RoomRecord(roomNumber, date, timeslot)
            rooms.remove(room)
            bookings.removeIf { booking -> booking.room == room }
        }

        return true
    }

    // ================================
    // Student methods
    // ================================
    fun bookRoom(args: BookRoom): Boolean {
        val (studentId, campus, roomNumber, date, timeslots) = args;

        // TODO: Generate new booking IDs
        val bookingId = "10000";

        // TODO: Count number of bookings PER WEEK
        assert(
                bookings.filter { booking -> booking.studentId == studentId }.size + timeslots.size <= 3,
                { "Student cannot make that many bookings." }
        )

        for (timeslot in timeslots) {
            val room = RoomRecord(roomNumber, date, timeslot)
            assert(rooms.contains(room), { "Room to book does not exist." })
            assert(bookings.find { booking -> booking.room == room } == null, { "Room has already been booked." })
        }

        for (timeslot in timeslots) {
            val room = RoomRecord(roomNumber, date, timeslot)
            val booking = Booking(room, studentId, bookingId)
            bookings.add(booking)
        }

        return true
    }

    fun cancelBooking(args: CancelBooking): Boolean {
        val (studentId, bookingId) = args

        assert(bookings.find { booking -> booking.bookingId == bookingId } != null,
                { "Booking $bookingId does not exist." })
        assert(
                bookings.find { booking -> booking.bookingId == bookingId }!!.studentId == studentId,
                { "Booking $bookingId does not belong to student $studentId." }
        )

        bookings.removeIf { booking -> booking.bookingId == bookingId }

        return true;
    }

    fun getAvailableTimeslots(args: GetAvailableTimeslots): Int {
        val (date) = args
        val totalRoomsAtDate = rooms.filter { room -> room.date == date }.size
        val bookedRoomsAtDate = bookings.filter { booking -> booking.room.date == date }.size
        return totalRoomsAtDate - bookedRoomsAtDate
    }
}