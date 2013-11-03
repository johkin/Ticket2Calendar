package se.acrend.sj2cal.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.acrend.sj2cal.BuildConfig;
import se.acrend.sj2cal.model.SmsTicket;
import se.acrend.sj2cal.preference.PreferencesInstance;
import se.acrend.sj2cal.util.DateUtil;

public class ResPlusTicketParser extends MessageParserBase implements MessageParser {

    // 09 okt 20:05 RESPLUS
    // GUSTAV BYLUND, Student
    // Arlanda Express, Tåg 7862
    // Avg Stockholm C 20:05
    // Ank Arlanda Norra 20:25
    // Slutdest: Arlanda Norra
    // 2 Kl
    // Kan ej ombokas/återbetalas
    // Bilj. visas med giltig ID-handling
    // Biljnr PLB4188J0002

    private SimpleDateFormat format = null;

    private final PreferencesInstance preferencesInstance;

    public ResPlusTicketParser(final PreferencesInstance preferencesInstance) {
        super();
        this.preferencesInstance = preferencesInstance;
        format = new SimpleDateFormat("yyyydd MMMHHmm", DateUtil.SWEDISH_LOCALE);
        format.setTimeZone(DateUtil.SWEDISH_TIMEZONE);
    }

    @Override
    public String getParserName() {
        return "ResPlus";
    }

    /*
     * (non-Javadoc)
     *
     * @see se.acrend.sj2cal.parser.MessageParser#supports(java.lang.String)
     */
    @Override
    public boolean supports(final String sender, final String message) {
        if (BuildConfig.DEBUG) {
            return message.contains("RESPLUS");
        }
        if (preferencesInstance.isParseResPlus()) {
            return sender.startsWith("Biljett");
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

        String date = findValue(message, "(\\d{1,2} \\D{3}) \\d{1,2}[\\.:]\\d{2}", "datum");

        String from = findValue(message, "Avg (.*) \\d{1,2}[\\.:]", "avreseort");
        String fromTime = findValue(message, "Avg .* (\\d{1,2}[\\.:]\\d{2})", "avgångstid");

        String to = findValue(message, "Ank (.*) \\d{1,2}[\\.:]", "ankomstort");
        String toTime = findValue(message, "Ank .* (\\d{1,2}[\\.:]\\d{2})", "ankomsttid");
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
        String train = findValue(message, "T[å|a]g (\\d*)", "tågnr");
        ticket.setTrain(Integer.parseInt(train));

        ticket.setCode(findValue(message, "Biljnr (\\D{3}\\d{4}\\D\\d{4})", "Biljettkod"));

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
            cal.setTime(format.parse(currentYear + date + timeWithoutDelimiter));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Kunde inte tolka datum.", e);
        }
        return cal;
    }
}
