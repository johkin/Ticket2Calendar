package se.acrend.sj2cal.parser;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import groovy.lang.Binding;
import groovy.lang.GString;
import groovy.lang.GroovyShell;
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

  String readTemplate(Map values) throws Exception {
    Binding binding = new Binding(values);
    GroovyShell shell = new GroovyShell(binding);

    String template = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/SjTicketTemplate.groovy"));

    shell.evaluate(template);

    GString result = (GString) binding.getVariable("result");

    return result.toString();
  }

  @Test
  public void testParse() throws Exception {
    Map<String, String> values = new HashMap<String, String>();

    values.put("date", "25 jun");
    values.put("from", "Falun C");
    values.put("to", "Stockholm C");
    values.put("departure", "06.11");
    values.put("arrival", "08.44");
    values.put("train", "661");
    values.put("car", "2");
    values.put("seat", "28");
    values.put("code", "BMS4899S0001");

    String message = readTemplate(values);

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

    Map<String, String> values = new HashMap<String, String>();

    values.put("date", "29 jun");
    values.put("from", "Kiruna stn");
    values.put("to", "Örnsköldsvik C");
    values.put("departure", "15.46");
    values.put("arrival", "01.00");
    values.put("train", "93");
    values.put("car", "16");
    values.put("seat", "81");
    values.put("code", "CET0845K0001");

    String message = readTemplate(values);

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

    Map<String, String> values = new HashMap<String, String>();

    values.put("date", "31 dec");
    values.put("from", "Norrköping C");
    values.put("to", "Stockholm C");
    values.put("departure", "23.55");
    values.put("arrival", "01.39");
    values.put("train", "538");
    values.put("car", "2");
    values.put("seat", "30");
    values.put("code", "SPG9352F0002");

    String message = readTemplate(values);

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
  public void testSmsBiljettLateSmsArrival() throws Exception {

    parser.setTimeSource(new TimeSource() {

      @Override
      public Calendar getCalendar() {
        Calendar now = Calendar.getInstance();
        now.setTime(Timestamp.valueOf("2013-04-28 12:10:0.000"));
        return now;
      }

    });

    Map<String, String> values = new HashMap<String, String>();

    values.put("date", "25 jun");
    values.put("from", "Arvika stn");
    values.put("to", "Stockholm C");
    values.put("departure", "05.41");
    values.put("arrival", "08.39");
    values.put("train", "620");
    values.put("car", "7");
    values.put("seat", "42");
    values.put("code", "ALP2817O0001");

    String message = readTemplate(values);

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

    Map<String, String> values = new HashMap<String, String>();

    values.put("date", "28 apr");
    values.put("from", "Linköping C");
    values.put("to", "Södertälje Syd");
    values.put("departure", "12.00");
    values.put("arrival", "13.33");
    values.put("train", "10530");
    values.put("car", "");
    values.put("seat", "");
    values.put("code", "QPB0497Q0001");

    String message = readTemplate(values);

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
    Map<String, String> values = new HashMap<String, String>();

    values.put("date", "28 apr");
    values.put("from", "Linköping C");
    values.put("to", "Södertälje Syd");
    values.put("departure", "12.00");
    values.put("arrival", "13.33");
    values.put("train", "10530");
    values.put("car", "");
    values.put("seat", "");
    values.put("code", "QPB0497Q0001");

    String message = readTemplate(values);

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

    Map<String, String> values = new HashMap<String, String>();

    values.put("date", "25 jun");
    values.put("from", "Falun C");
    values.put("to", "Stockholm C");
    values.put("departure", "06.11");
    values.put("arrival", "08.44");
    values.put("train", "661");
    values.put("car", "2");
    values.put("seat", "28");
    values.put("code", "BMS4899S0001");

    String message = readTemplate(values);

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
