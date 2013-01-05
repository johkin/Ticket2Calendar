package se.acrend.sj2cal.parser;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import se.acrend.sj2cal.model.SmsTicket;

public class OresundsTicketParserTest {

  private OresundsTicketParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new OresundsTicketParser(new TestPreferencesInstance());
  }

  @Test
  public void testParse() throws Exception {
    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/OresundsBiljett.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Helsingborg C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-12-19 17:53:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Göteborg C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-12-19 20:15:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(14, ticket.getCar());
    assertEquals(246, ticket.getSeat());
    assertEquals("PMX5728X0002", ticket.getCode());
    assertEquals(1074, ticket.getTrain());
    assertEquals("http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=20131219,1074", ticket.getUrl());
  }

}
