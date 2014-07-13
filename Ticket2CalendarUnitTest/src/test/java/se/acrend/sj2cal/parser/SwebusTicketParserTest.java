package se.acrend.sj2cal.parser;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import se.acrend.sj2cal.model.SwebusTicket;
import se.acrend.sj2cal.util.TimeSource;

import static org.junit.Assert.assertEquals;

public class SwebusTicketParserTest {

  private SwebusTicketParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new SwebusTicketParser(new TestPreferencesInstance());

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
    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SwebusBiljett.txt"));

    SwebusTicket ticket = parser.parse(message);
    assertEquals("JÖNKÖPING ReseC", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2011-10-14 16:35:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("LINKÖPING Fjärrbussterm", ticket.getTo());
    assertEquals(Timestamp.valueOf("2011-10-14 18:15:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals("1NBNYMU-0", ticket.getCode());
    assertEquals(830, ticket.getTrain());
  }
}
