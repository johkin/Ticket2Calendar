package se.acrend.sj2cal.model;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class ConfirmationTest {

  private Confirmation confirmation;

  @Before
  public void setUp() throws Exception {
    confirmation = new Confirmation();
    confirmation.setFrom("Norrköping");
    confirmation.setTo("Stockholm");
    Calendar dep = Calendar.getInstance();
    dep.setTimeInMillis(Timestamp.valueOf("2011-01-11 10:00:00").getTime());
    confirmation.setDeparture(dep);
    Calendar arr = Calendar.getInstance();
    arr.setTimeInMillis(Timestamp.valueOf("2011-01-11 11:44:00").getTime());
    confirmation.setArrival(arr);
  }

  @Test
  public void testToString() {
    String string = confirmation.toString();

    assertEquals(true, string.contains("Norrköping"));
  }

}
