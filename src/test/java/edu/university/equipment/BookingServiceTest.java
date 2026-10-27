package edu.university.equipment;
import static org.junit.jupiter.api.Assertions.*; import java.util.NoSuchElementException; import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;
class BookingServiceTest {
 private BookingService service; @BeforeEach void setup(){service=new BookingService();}
 @Test void successfulBookingReducesInventory(){service.create("STU1001","LAPTOP01",2);assertEquals(8,service.getEquipment("LAPTOP01").getAvailableQuantity());}
 @Test void zeroQuantityRejected(){assertThrows(IllegalArgumentException.class,()->service.create("STU1001","LAPTOP01",0));assertEquals(10,service.getEquipment("LAPTOP01").getAvailableQuantity());}
 @Test void blankBorrowerRejected(){assertThrows(IllegalArgumentException.class,()->service.create(" ","LAPTOP01",1));}
 @Test void unknownEquipmentRejected(){assertThrows(NoSuchElementException.class,()->service.create("STU1001","UNKNOWN",1));}
 @Test void ordinaryOverbookingRejected(){assertThrows(IllegalStateException.class,()->service.create("STU1001","PROJECTOR01",6));assertEquals(4,service.getEquipment("PROJECTOR01").getAvailableQuantity());}
 @Test
void confirmedBookingCanBeCancelled() {
    Booking b = service.create("STU1001", "TABLET01", 3);

    assertEquals("CONFIRMED", b.getStatus());
    assertEquals(5, service.getEquipment("TABLET01").getAvailableQuantity());

    service.cancel(b.getId());

    assertEquals("CANCELLED", service.getBooking(b.getId()).getStatus());
    assertEquals(8, service.getEquipment("TABLET01").getAvailableQuantity());
}

@Test
void repeatedCancellationDoesNotRestoreInventoryAgain() {
    Booking b = service.create("STU1001", "TABLET01", 3);

    service.cancel(b.getId());

    assertThrows(
        IllegalStateException.class,
        () -> service.cancel(b.getId())
    );

    assertEquals("CANCELLED", service.getBooking(b.getId()).getStatus());
    assertEquals(8, service.getEquipment("TABLET01").getAvailableQuantity());
}

@Test
void nonConfirmedBookingCannotBeCancelled() {
    Booking b = service.create("STU1001", "TABLET01", 2);
    b.setStatus("PENDING");

    assertThrows(
        IllegalStateException.class,
        () -> service.cancel(b.getId())
    );

    assertEquals("PENDING", service.getBooking(b.getId()).getStatus());
    assertEquals(6, service.getEquipment("TABLET01").getAvailableQuantity());
}
}