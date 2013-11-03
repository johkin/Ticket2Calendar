package se.acrend.sj2cal.parser;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Calendar;

import se.acrend.sj2cal.model.SmsTicket;
import se.acrend.sj2cal.util.TimeSource;

import static org.junit.Assert.assertEquals;

public class ResPlusTicketParserTest {

    private ResPlusTicketParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new ResPlusTicketParser(new TestPreferencesInstance());

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
    public void testParse1() throws Exception {
        String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/ResPlus1.txt"));

        SmsTicket ticket = parser.parse(message);
        assertEquals("Stockholm C", ticket.getFrom());
        assertEquals(Timestamp.valueOf("2013-10-09 20:05:00.0"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
        assertEquals("Arlanda Norra", ticket.getTo());
        assertEquals(Timestamp.valueOf("2013-10-09 20:25:00.0"), new Timestamp(ticket.getArrival().getTimeInMillis()));
        assertEquals(0, ticket.getCar());
        assertEquals(0, ticket.getSeat());
        assertEquals("PLB4188J0002", ticket.getCode());
        assertEquals(7862, ticket.getTrain());
        assertEquals("http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=20131009,7862", ticket.getUrl());
    }

    @Test
    public void testParse2() throws Exception {

        String message = IOUtils.toString(this.getClass().getResourceAsStream("/testdata/sms/ResPlus2.txt"));

        SmsTicket ticket = parser.parse(message);
        assertEquals("Arlanda Norra", ticket.getFrom());
        assertEquals(Timestamp.valueOf("2013-10-14 09:35:00"), new Timestamp(ticket.getDeparture().getTimeInMillis()));
        assertEquals("Stockholm C", ticket.getTo());
        assertEquals(Timestamp.valueOf("2013-10-14 09:55:00"), new Timestamp(ticket.getArrival().getTimeInMillis()));
        assertEquals(0, ticket.getCar());
        assertEquals(0, ticket.getSeat());
        assertEquals("PLB4188J0003", ticket.getCode());
        assertEquals(7821, ticket.getTrain());
    }
}