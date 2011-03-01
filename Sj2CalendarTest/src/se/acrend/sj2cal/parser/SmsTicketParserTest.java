package se.acrend.sj2cal.parser;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;

import se.acrend.sj2cal.model.SmsTicket;

public class SmsTicketParserTest {

  private final String message = "11 jan kl 16:24\n" + "+'220572436'+\n+'903765246'+\n+'373740923'+\n'692092924'+\n"
      + "ÅRSKORT GULD \nJOHAN KINDGREN\nAvg. Norrköping C 16.24\nAnk. Stockholm C 17.39\n"
      + "Tåg: 538\nVU, 1 klass Kan återbetalas\nVagn 2, plats 30\nPersonlig biljett giltig med ID\n"
      + "Internet/Bilj.nr. SPG9352F0002\n010 624 472 391 895 723 215";

  private SmsTicketParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new SmsTicketParser();
  }

  @Test
  public void testParse() throws Exception {

    SmsTicket ticket = parser.parse(message);
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2011-01-11 16:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2011-01-11 17:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(30, ticket.getSeat());
    assertEquals("SPG9352F0002", ticket.getCode());
    assertEquals(538, ticket.getTrain());
  }

  @Test
  public void testSupports() throws Exception {
    assertEquals(true, parser.supports(message));
  }

  // @Test
  // public void testParseIllForatted() throws Exception {
  //
  // EventBase ticket = parser.parse(message);
  //
  // ticket.validate();
  //
  // assertEquals("Norrköping C", ticket.getFrom());
  // assertEquals(Timestamp.valueOf("2011-01-14 16:24:00"),
  // ticket.getDeparture());
  // assertEquals("Stockholm C", ticket.getTo());
  // assertEquals(Timestamp.valueOf("2011-01-14 17:39:00"),
  // ticket.getArrival());
  // assertEquals(2, ticket.getCar());
  // assertEquals(25, ticket.getSeat());
  // assertEquals("BKD3723G0002", ticket.getCode());
  // }
}
