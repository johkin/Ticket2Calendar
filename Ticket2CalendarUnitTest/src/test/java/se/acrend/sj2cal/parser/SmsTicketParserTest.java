package se.acrend.sj2cal.parser;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import se.acrend.sj2cal.model.SmsTicket;
import se.acrend.sj2cal.util.TimeSource;

import static org.junit.Assert.assertEquals;

public class SmsTicketParserTest {

  private SmsTicketParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new SmsTicketParser(new TestPreferencesInstance());


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
    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljett.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Falun C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-06-25 06:11:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-06-25 08:44:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(28, ticket.getSeat());
    assertEquals("BMS4899S0001", ticket.getCode());
    assertEquals(661, ticket.getTrain());
    assertEquals("http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=20130625,661", ticket.getUrl());
  }

  @Test
  public void testParseNextDay() throws Exception {

    parser.setTimeSource(new TimeSource() {

      @Override
      public Calendar getCalendar() {
        Calendar now = Calendar.getInstance();
        now.setTime(Timestamp.valueOf("2012-12-30 11:10:0.000"));
        return now;
      }

    });

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljettNextDay.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Kiruna stn", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2012-06-29 15:46:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Örnsköldsvik C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2012-06-30 01:00:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(16, ticket.getCar());
    assertEquals(81, ticket.getSeat());
    assertEquals("CET0845K0001", ticket.getCode());
    assertEquals(93, ticket.getTrain());
  }

  @Test
  public void testParseNewYearDay() throws Exception {

    parser.setTimeSource(new TimeSource() {

      @Override
      public Calendar getCalendar() {
        Calendar now = Calendar.getInstance();
        now.setTime(Timestamp.valueOf("2012-12-31 11:10:0.000"));
        return now;
      }

    });

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljettNewYear.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2012-12-31 23:55:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-01-01 01:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(30, ticket.getSeat());
    assertEquals("SPG9352F0002", ticket.getCode());
    assertEquals(538, ticket.getTrain());
    assertEquals(message, ticket.getMessage());
  }

  @Test
  @Ignore
  public void testParseNextYear() throws Exception {

    parser.setTimeSource(new TimeSource() {

      @Override
      public Calendar getCalendar() {
        Calendar now = Calendar.getInstance();
        now.setTime(Timestamp.valueOf("2013-05-05 11:10:0.000"));
        return now;
      }

    });

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljettN extYear.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Stockholm C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2014-03-05 15:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Kumla stn", ticket.getTo());
    assertEquals(Timestamp.valueOf("2014-03-05 17:30:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(30, ticket.getSeat());
    assertEquals("SPG9352F0002", ticket.getCode());
    assertEquals(538, ticket.getTrain());
    assertEquals(message, ticket.getMessage());
  }

  @Test
  public void testSmsBiljettLateSmsArrival() throws Exception {

    parser.setTimeSource(new TimeSource() {

      @Override
      public Calendar getCalendar() {
        Calendar now = Calendar.getInstance();
        now.setTime(Timestamp.valueOf("2013-04-28 12:10:0.000"));
        return now;
      }

    });

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljett1.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Arvika stn", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-06-25 05:41:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-06-25 08:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(7, ticket.getCar());
    assertEquals(42, ticket.getSeat());
    assertEquals("ALP2817O0001", ticket.getCode());
    assertEquals(620, ticket.getTrain());
    assertEquals(message, ticket.getMessage());
  }

  @Test
  public void testSmsBiljettWithColons() throws Exception {

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljett1.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Arvika stn", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-06-25 05:41:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-06-25 08:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(7, ticket.getCar());
    assertEquals(42, ticket.getSeat());
    assertEquals("ALP2817O0001", ticket.getCode());
    assertEquals(620, ticket.getTrain());
    assertEquals(message, ticket.getMessage());
  }

  @Test
  public void testSmsBiljettNoSeat() throws Exception {

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljettNoSeat.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Linköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-04-28 12:00:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Södertälje Syd", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-04-28 13:33:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(0, ticket.getCar());
    assertEquals(0, ticket.getSeat());
    assertEquals("QPB0497Q0001", ticket.getCode());
    assertEquals(10530, ticket.getTrain());
    assertEquals(message, ticket.getMessage());
  }

  @Test
  public void testSupports() throws Exception {
    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljett.txt"));

    assertEquals(true, parser.supports("SJ Biljett", message));
    assertEquals(true, parser.supports("SJBILJ", message));
  }

  @Test
  public void testSupportsNotSL() throws Exception {
    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SLBiljett.txt"));

    assertEquals(false, parser.supports("SJ Biljett", message));
    assertEquals(false, parser.supports("SJBILJ", message));
  }

  @Test
  public void testParseLocale() throws Exception {

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljett.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Falun C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-06-25 06:11:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-06-25 08:44:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(28, ticket.getSeat());
    assertEquals("BMS4899S0001", ticket.getCode());
    assertEquals(661, ticket.getTrain());
    assertEquals("http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=20130625,661", ticket.getUrl());
  }
}
