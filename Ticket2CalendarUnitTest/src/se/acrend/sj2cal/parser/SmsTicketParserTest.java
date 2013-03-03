package se.acrend.sj2cal.parser;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import se.acrend.sj2cal.model.SmsTicket;
import se.acrend.sj2cal.util.TimeSource;

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
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-05-11 16:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-05-11 17:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(30, ticket.getSeat());
    assertEquals("SPG9352F0002", ticket.getCode());
    assertEquals(538, ticket.getTrain());
    assertEquals("http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=20130511,538", ticket.getUrl());
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
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2012-12-30 23:55:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2012-12-31 01:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(30, ticket.getSeat());
    assertEquals("SPG9352F0002", ticket.getCode());
    assertEquals(538, ticket.getTrain());
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
  public void testParseNextYear() throws Exception {
    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljettNextYear.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-01-06 16:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-01-06 17:51:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
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
    assertEquals("Linköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-04-28 12:00:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Södertälje Syd", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-04-28 13:33:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(1, ticket.getCar());
    assertEquals(11, ticket.getSeat());
    assertEquals("QPB0497Q0001", ticket.getCode());
    assertEquals(10530, ticket.getTrain());
    assertEquals(message, ticket.getMessage());
  }
  
  @Test
  public void testSmsBiljettWithColons() throws Exception {

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljett1.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Linköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-04-28 12:00:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Södertälje Syd", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-04-28 13:33:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(1, ticket.getCar());
    assertEquals(11, ticket.getSeat());
    assertEquals("QPB0497Q0001", ticket.getCode());
    assertEquals(10530, ticket.getTrain());
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
  }

  @Test
  public void testSupportsNotSL() throws Exception {
    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SLBiljett.txt"));

    assertEquals(false, parser.supports("SJ Biljett", message));
  }

  @Test
  public void testParseLocale() throws Exception {

    String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SmsBiljett.txt"));

    SmsTicket ticket = parser.parse(message);
    assertEquals("Norrköping C", ticket.getFrom());
    assertEquals(Timestamp.valueOf("2013-05-11 16:24:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
    assertEquals("Stockholm C", ticket.getTo());
    assertEquals(Timestamp.valueOf("2013-05-11 17:39:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
    assertEquals(2, ticket.getCar());
    assertEquals(30, ticket.getSeat());
    assertEquals("SPG9352F0002", ticket.getCode());
    assertEquals(538, ticket.getTrain());
    assertEquals("http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=20130511,538", ticket.getUrl());
  }
}
