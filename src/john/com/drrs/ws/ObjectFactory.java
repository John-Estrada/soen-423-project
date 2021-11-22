
package john.com.drrs.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.drrs.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ChangeReservationResponse_QNAME = new QName("http://ws.drrs.com/", "changeReservationResponse");
    private final static QName _BookRoom_QNAME = new QName("http://ws.drrs.com/", "bookRoom");
    private final static QName _SecondaryTestResponse_QNAME = new QName("http://ws.drrs.com/", "secondaryTestResponse");
    private final static QName _ChangeReservation_QNAME = new QName("http://ws.drrs.com/", "changeReservation");
    private final static QName _GetAvailableTimeSlotsResponse_QNAME = new QName("http://ws.drrs.com/", "getAvailableTimeSlotsResponse");
    private final static QName _CreateRoomResponse_QNAME = new QName("http://ws.drrs.com/", "createRoomResponse");
    private final static QName _GetBookingResponse_QNAME = new QName("http://ws.drrs.com/", "getBookingResponse");
    private final static QName _GetBooking_QNAME = new QName("http://ws.drrs.com/", "getBooking");
    private final static QName _DeleteRoom_QNAME = new QName("http://ws.drrs.com/", "deleteRoom");
    private final static QName _CancelBookingResponse_QNAME = new QName("http://ws.drrs.com/", "cancelBookingResponse");
    private final static QName _SecondaryTest_QNAME = new QName("http://ws.drrs.com/", "secondaryTest");
    private final static QName _DeleteRoomResponse_QNAME = new QName("http://ws.drrs.com/", "deleteRoomResponse");
    private final static QName _CreateRoom_QNAME = new QName("http://ws.drrs.com/", "createRoom");
    private final static QName _GetAvailableTimeSlots_QNAME = new QName("http://ws.drrs.com/", "getAvailableTimeSlots");
    private final static QName _BookRoomResponse_QNAME = new QName("http://ws.drrs.com/", "bookRoomResponse");
    private final static QName _CancelBooking_QNAME = new QName("http://ws.drrs.com/", "cancelBooking");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.drrs.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BookRoomResponse }
     * 
     */
    public BookRoomResponse createBookRoomResponse() {
        return new BookRoomResponse();
    }

    /**
     * Create an instance of {@link GetAvailableTimeSlots }
     * 
     */
    public GetAvailableTimeSlots createGetAvailableTimeSlots() {
        return new GetAvailableTimeSlots();
    }

    /**
     * Create an instance of {@link CancelBooking }
     * 
     */
    public CancelBooking createCancelBooking() {
        return new CancelBooking();
    }

    /**
     * Create an instance of {@link SecondaryTest }
     * 
     */
    public SecondaryTest createSecondaryTest() {
        return new SecondaryTest();
    }

    /**
     * Create an instance of {@link CancelBookingResponse }
     * 
     */
    public CancelBookingResponse createCancelBookingResponse() {
        return new CancelBookingResponse();
    }

    /**
     * Create an instance of {@link DeleteRoom }
     * 
     */
    public DeleteRoom createDeleteRoom() {
        return new DeleteRoom();
    }

    /**
     * Create an instance of {@link GetBooking }
     * 
     */
    public GetBooking createGetBooking() {
        return new GetBooking();
    }

    /**
     * Create an instance of {@link CreateRoom }
     * 
     */
    public CreateRoom createCreateRoom() {
        return new CreateRoom();
    }

    /**
     * Create an instance of {@link DeleteRoomResponse }
     * 
     */
    public DeleteRoomResponse createDeleteRoomResponse() {
        return new DeleteRoomResponse();
    }

    /**
     * Create an instance of {@link GetAvailableTimeSlotsResponse }
     * 
     */
    public GetAvailableTimeSlotsResponse createGetAvailableTimeSlotsResponse() {
        return new GetAvailableTimeSlotsResponse();
    }

    /**
     * Create an instance of {@link ChangeReservation }
     * 
     */
    public ChangeReservation createChangeReservation() {
        return new ChangeReservation();
    }

    /**
     * Create an instance of {@link GetBookingResponse }
     * 
     */
    public GetBookingResponse createGetBookingResponse() {
        return new GetBookingResponse();
    }

    /**
     * Create an instance of {@link CreateRoomResponse }
     * 
     */
    public CreateRoomResponse createCreateRoomResponse() {
        return new CreateRoomResponse();
    }

    /**
     * Create an instance of {@link ChangeReservationResponse }
     * 
     */
    public ChangeReservationResponse createChangeReservationResponse() {
        return new ChangeReservationResponse();
    }

    /**
     * Create an instance of {@link BookRoom }
     * 
     */
    public BookRoom createBookRoom() {
        return new BookRoom();
    }

    /**
     * Create an instance of {@link SecondaryTestResponse }
     * 
     */
    public SecondaryTestResponse createSecondaryTestResponse() {
        return new SecondaryTestResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeReservationResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "changeReservationResponse")
    public JAXBElement<ChangeReservationResponse> createChangeReservationResponse(ChangeReservationResponse value) {
        return new JAXBElement<ChangeReservationResponse>(_ChangeReservationResponse_QNAME, ChangeReservationResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BookRoom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "bookRoom")
    public JAXBElement<BookRoom> createBookRoom(BookRoom value) {
        return new JAXBElement<BookRoom>(_BookRoom_QNAME, BookRoom.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecondaryTestResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "secondaryTestResponse")
    public JAXBElement<SecondaryTestResponse> createSecondaryTestResponse(SecondaryTestResponse value) {
        return new JAXBElement<SecondaryTestResponse>(_SecondaryTestResponse_QNAME, SecondaryTestResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ChangeReservation }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "changeReservation")
    public JAXBElement<ChangeReservation> createChangeReservation(ChangeReservation value) {
        return new JAXBElement<ChangeReservation>(_ChangeReservation_QNAME, ChangeReservation.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailableTimeSlotsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "getAvailableTimeSlotsResponse")
    public JAXBElement<GetAvailableTimeSlotsResponse> createGetAvailableTimeSlotsResponse(GetAvailableTimeSlotsResponse value) {
        return new JAXBElement<GetAvailableTimeSlotsResponse>(_GetAvailableTimeSlotsResponse_QNAME, GetAvailableTimeSlotsResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRoomResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "createRoomResponse")
    public JAXBElement<CreateRoomResponse> createCreateRoomResponse(CreateRoomResponse value) {
        return new JAXBElement<CreateRoomResponse>(_CreateRoomResponse_QNAME, CreateRoomResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBookingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "getBookingResponse")
    public JAXBElement<GetBookingResponse> createGetBookingResponse(GetBookingResponse value) {
        return new JAXBElement<GetBookingResponse>(_GetBookingResponse_QNAME, GetBookingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBooking }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "getBooking")
    public JAXBElement<GetBooking> createGetBooking(GetBooking value) {
        return new JAXBElement<GetBooking>(_GetBooking_QNAME, GetBooking.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRoom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "deleteRoom")
    public JAXBElement<DeleteRoom> createDeleteRoom(DeleteRoom value) {
        return new JAXBElement<DeleteRoom>(_DeleteRoom_QNAME, DeleteRoom.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelBookingResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "cancelBookingResponse")
    public JAXBElement<CancelBookingResponse> createCancelBookingResponse(CancelBookingResponse value) {
        return new JAXBElement<CancelBookingResponse>(_CancelBookingResponse_QNAME, CancelBookingResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SecondaryTest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "secondaryTest")
    public JAXBElement<SecondaryTest> createSecondaryTest(SecondaryTest value) {
        return new JAXBElement<SecondaryTest>(_SecondaryTest_QNAME, SecondaryTest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteRoomResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "deleteRoomResponse")
    public JAXBElement<DeleteRoomResponse> createDeleteRoomResponse(DeleteRoomResponse value) {
        return new JAXBElement<DeleteRoomResponse>(_DeleteRoomResponse_QNAME, DeleteRoomResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateRoom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "createRoom")
    public JAXBElement<CreateRoom> createCreateRoom(CreateRoom value) {
        return new JAXBElement<CreateRoom>(_CreateRoom_QNAME, CreateRoom.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailableTimeSlots }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "getAvailableTimeSlots")
    public JAXBElement<GetAvailableTimeSlots> createGetAvailableTimeSlots(GetAvailableTimeSlots value) {
        return new JAXBElement<GetAvailableTimeSlots>(_GetAvailableTimeSlots_QNAME, GetAvailableTimeSlots.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BookRoomResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "bookRoomResponse")
    public JAXBElement<BookRoomResponse> createBookRoomResponse(BookRoomResponse value) {
        return new JAXBElement<BookRoomResponse>(_BookRoomResponse_QNAME, BookRoomResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelBooking }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws.drrs.com/", name = "cancelBooking")
    public JAXBElement<CancelBooking> createCancelBooking(CancelBooking value) {
        return new JAXBElement<CancelBooking>(_CancelBooking_QNAME, CancelBooking.class, null, value);
    }

}
