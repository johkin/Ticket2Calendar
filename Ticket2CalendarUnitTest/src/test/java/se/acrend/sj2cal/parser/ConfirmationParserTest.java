package se.acrend.sj2cal.parser;

import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import se.acrend.sj2cal.model.EventBase;
import se.acrend.sj2cal.util.TimeSource;

import static org.junit.Assert.assertEquals;

public class ConfirmationParserTest {

  private ConfirmationParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new ConfirmationParser(new TestPreferencesInstance());

    parser.setTimeSource(new TimeSource() {

      @Override
      public Calendar getCalendar() {
        Calendar now = Calendar.getInstance();
        now.setTime(Timestamp.valueOf("2013-02-18 11:10:0.000"));
        return now;
      }

    });
  }

  @Test
  public void testParse() throws Exception {
    String message = "* SJ * BKD3723G0002 Datum: 110114 Avg: Norrköping C 16.24 Ank: Stockholm C 17.39 Vagn: 2 Plats: 25 Internet ombord X2000/Dubbeldäckare Kod: BKD3723G0002";

    EventBase ticket = parser.parse(message);
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2011-01-14 16:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2011-01-14 17:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(25, ticket.getSeat());
    assertEquals("BKD3723G0002", ticket.getCode());
  }

  @Test
  public void testParseNextDay() throws Exception {
    String message = "* SJ * BKD3723G0002 Datum: 110114 Avg: Norrköping C 23.24 Ank: Stockholm C 01.39 Vagn: 2 Plats: 25 Internet ombord X2000/Dubbeldäckare Kod: BKD3723G0002";

    EventBase ticket = parser.parse(message);
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2011-01-14 23:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2011-01-15 01:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(25, ticket.getSeat());
    assertEquals("BKD3723G0002", ticket.getCode());
  }

  @Test
  public void testParseNewYearsDay() throws Exception {
    String message = "* SJ * BKD3723G0002 Datum: 111231 Avg: Norrköping C 23.24 Ank: Stockholm C 01.39 Vagn: 2 Plats: 25 Internet ombord X2000/Dubbeldäckare Kod: BKD3723G0002";

    EventBase ticket = parser.parse(message);
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2011-12-31 23:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2012-01-01 01:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(25, ticket.getSeat());
    assertEquals("BKD3723G0002", ticket.getCode());
  }

  @Test
  public void testSupports() throws Exception {
    String message = "* SJ * BKD3723G0002 Datum: 110114 Avg: Norrköping C 16.24 Ank: Stockholm C 17.39 Vagn: 2 Plats: 25 Internet ombord X2000/Dubbeldäckare Kod: BKD3723G0002";

    assertEquals(true, parser.supports("SJ", message));
  }

  @Test
  public void testParseIllForatted() throws Exception {
    String message = "* SJ * BKD3723G0002 Datum: 110114 Avg: Norrköping C 16.24 Ank: Stockholm C 17.39 Vagn: 2 Plats: 25";

    EventBase ticket = parser.parse(message);

    ticket.validate();

    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2011-01-14 16:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2011-01-14 17:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(25, ticket.getSeat());
    assertEquals("BKD3723G0002", ticket.getCode());
  }
}
