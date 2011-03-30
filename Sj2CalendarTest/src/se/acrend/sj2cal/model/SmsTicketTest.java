package se.acrend.sj2cal.model;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class SmsTicketTest {

  private SmsTicket ticket;

  @Before
  public void setUp() throws Exception {
    ticket = new SmsTicket();
    ticket.setMessage("...Norrköping...");
    ticket.setFrom("Norrköping");
    ticket.setTo("Stockholm");
    Calendar dep = Calendar.getInstance();
    dep.setTimeInMillis(Timestamp.valueOf("2011-01-11 10:00:00").getTime());
    ticket.setDeparture(dep);
    Calendar arr = Calendar.getInstance();
    arr.setTimeInMillis(Timestamp.valueOf("2011-01-11 11:33:00").getTime());
    ticket.setArrival(arr);
  }

  @Test
  public void testToString() {
    String string = ticket.toString();

    assertEquals(true, string.contains("Norrköping"));
  }

}
