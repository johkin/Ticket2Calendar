package se.acrend.sj2cal.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.acrend.sj2cal.model.SwebusTicket;
import se.acrend.sj2cal.util.DateUtil;

public class SwebusTicketParser extends MessageParserBase implements MessageParser {

  // 1NBNYMU-0
  // 1 Ungdom den 14 oktober 2011
  // Linje 830
  // 16:35 JÖNKÖPING ReseC -
  // 18:15 LINKÖPING Fjärrbussterm
  // Välkommen ombord!

  private SimpleDateFormat format = null;

  public SwebusTicketParser() {
    super();
    format = new SimpleDateFormat("dd MMMM yyyyHH:mm", DateUtil.SWEDISH_LOCALE);
    format.setTimeZone(DateUtil.SWEDISH_TIMEZONE);
  }

  /*
   * (non-Javadoc)
   * 
   * @see se.acrend.sj2cal.parser.MessageParser#supports(java.lang.String)
   */
  @Override
  public boolean supports(final String sender, final String message) {
    return "Swebus".equalsIgnoreCase(sender);
  }

  /*
   * (non-Javadoc)
   * 
   * @see se.acrend.sj2cal.parser.MessageParser#parse(java.lang.String)
   */
  @Override
  public SwebusTicket parse(final String message) {
    SwebusTicket ticket = new SwebusTicket();

    String code = findValue(message, "(\\S{7}-\\d{1})", "biljettkod");

    String date = findValue(message, "den (\\d{1,2} \\D{4,12} \\d{4})", "datum");

    String from = findValue(message, "\\d{1,2}\\:\\d{2} (.*) -", "avreseort");
    String fromTime = findValue(message, "(\\d{1,2}\\:\\d{2}) .* -", "avgångstid");

    String to = findValue(message, "-\\s*\\d{1,2}\\:\\d{2} (.*)\\s*Välkommen", "ankomstort");
    String toTime = findValue(message, "-\\s*(\\d{1,2}\\:\\d{2}) .*\\s*Välkommen", "ankomsttid");
    try {
      Calendar departure = createCalendar(date, fromTime);
      ticket.setFrom(from);
      ticket.setDeparture(departure);

      Calendar arrival = createCalendar(date, toTime);
      if (arrival.before(departure)) {
        arrival.add(Calendar.DAY_OF_YEAR, 1);
      }

      ticket.setTo(to);
      ticket.setArrival(arrival);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Kunde inte tolka datum. Meddelande: " + message, e);
    }
    String train = findValue(message, "Linje (\\d*)", "bussnr");
    ticket.setTrain(Integer.parseInt(train));
    ticket.setCode(code);

    ticket.setMessage(message);

    ticket.validate();

    return ticket;
  }

  private Calendar createCalendar(final String date, final String time) {
    Calendar cal = Calendar.getInstance();
    try {
      cal.setTime(format.parse(date + time));
    } catch (ParseException e) {
      throw new IllegalArgumentException("Kunde inte tolka datum.", e);
    }
    return cal;
  }
}
