package se.acrend.sj2cal.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.acrend.sj2cal.BuildConfig;
import se.acrend.sj2cal.model.SmsTicket;
import se.acrend.sj2cal.preference.PreferencesInstance;
import se.acrend.sj2cal.util.DateUtil;

public class SmsTicketParser extends MessageParserBase implements MessageParser {

//  25 jun
//  Falun C - Stockholm C
//  Avg 06.10 - Ank 08.44
//  SJ, tåg 661
//  Vagn 2
//  Plats 28
//  2 klass
//  STEFAN JOHANSSON
//  Vuxen
//  Årskort Silver
//  Kan återbetalas
//  Bilj.nr BMS4899S0001
//  Personlig biljett ID krävs
//  +'915012676'+
//  +'539701260'+
//  +'376933123'+
//  +'799333295'+
//  217 540 525 434 872 476 932

  private SimpleDateFormat format = null;

  private final PreferencesInstance preferencesInstance;
  
  public SmsTicketParser(final PreferencesInstance preferencesInstance) {
    super();
    this.preferencesInstance = preferencesInstance;
    format = new SimpleDateFormat("yyyydd MMMHHmm", DateUtil.SWEDISH_LOCALE);
    format.setTimeZone(DateUtil.SWEDISH_TIMEZONE);
  }

  @Override
  public String getParserName() {
    return "SJ Biljett";
  }

  /*
   * (non-Javadoc)
   * 
   * @see se.acrend.sj2cal.parser.MessageParser#supports(java.lang.String)
   */
  @Override
  public boolean supports(final String sender, final String message) {
    if (BuildConfig.DEBUG) {
      return message.contains("+'") && message.contains("'+") && message.contains("tåg");
    }
    if (preferencesInstance.isParseSj()) {
      return "SJ Biljett".equalsIgnoreCase(sender) || sender.startsWith("SJBILJ");
    }
    return false;
  }

  /*
   * (non-Javadoc)
   * 
   * @see se.acrend.sj2cal.parser.MessageParser#parse(java.lang.String)
   */
  @Override
  public SmsTicket parse(final String message) {
    SmsTicket ticket = new SmsTicket();

    String date = findValue(message, "^(\\d{1,2} \\D{3})$", "datum");

    String from = findValue(message, "^(.*) - .*$", "avreseort");
    String to = findValue(message, "^.* - (.*)$", "ankomstort");

    String fromTime = findValue(message, "^Avg (\\d{1,2}[\\.:]\\d{2})", "avgångstid");

    String toTime = findValue(message, "Ank (\\d{1,2}[\\.:]\\d{2})$", "ankomsttid");
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
    String train = findValue(message, "t[å|a]g (\\d*)$", "tågnr");
    ticket.setTrain(Integer.parseInt(train));

    String car = findValue(message, "Vagn (\\d*)$", "vagn", false);
    String seat = findValue(message, "Plats (\\d*)$", "plats", false);

    if ((car != null) && (car.length() > 0)) {
      ticket.setCar(Integer.parseInt(car));
    }
    if ((seat != null) && (seat.length() > 0)) {
      ticket.setSeat(Integer.parseInt(seat));
    }

    ticket.setCode(findValue(message, "Bilj.nr (\\D{3}\\d{4}\\D\\d{4})", "Biljettkod"));

    ticket.setMessage(message);

    ticket.validate();

    return ticket;
  }

  private Calendar createCalendar(final String date, final String time) {
    Calendar cal = getTimeSource().getCalendar();
    Calendar now = getTimeSource().getCalendar();
    int currentYear = now.get(Calendar.YEAR);
    try {
      String timeWithoutDelimiter = time.replace(":", "").replace(".", "");
      String dateWithoutDelimiter = date.replace("/", "");
      String complete = currentYear + dateWithoutDelimiter + timeWithoutDelimiter;
      cal.setTime(format.parse(complete));
    } catch (ParseException e) {
      throw new IllegalArgumentException("Kunde inte tolka datum.", e);
    }
    return cal;
  }
}
