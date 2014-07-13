package se.acrend.sj2cal.parser;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import se.acrend.sj2cal.model.SmsTicket;
import se.acrend.sj2cal.util.TimeSource;

import static org.junit.Assert.assertEquals;

public class OresundsTicketParserTest {

  private OresundsTicketParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new OresundsTicketParser(new TestPreferencesInstance());

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
    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/OresundsBiljett.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Helsingborg C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-03-20 16:53:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Göteborg C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-03-20 19:15:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(11, ticket.getCar());
    assertEquals(62, ticket.getSeat());
    assertEquals("CML1778O0002", ticket.getCode());
    assertEquals(1068, ticket.getTrain());
    assertEquals("http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=20130320,1068", ticket.getUrl());
  }

}
