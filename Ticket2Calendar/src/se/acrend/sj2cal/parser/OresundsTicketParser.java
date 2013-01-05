package se.acrend.sj2cal.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.acrend.sj2cal.BuildConfig;
import se.acrend.sj2cal.model.SmsTicket;
import se.acrend.sj2cal.preference.PreferencesInstance;
import se.acrend.sj2cal.util.DateUtil;

public class OresundsTicketParser extends MessageParserBase implements MessageParser {

//	19 dec kl 17:53
//	FÖRKÖPT ENKEL
//	MIKAEL WALLIN, Vuxen
//	Ö-Tåg, Tåg 1074
//	Avg: Helsingborg C 17:53
//	Ank: Göteborg C 20:15
//	Slutdest: Göteborg C
//	2 Klass
//	Vagn: 14, Plats: 246
//	Resan kan inte ombokas/återbetalas
//	Biljetten visas med giltig id-handling
//	Bilj. Nr PMX5728X0002

	private SimpleDateFormat format = null;

	private PreferencesInstance preferencesInstance;

	public OresundsTicketParser(PreferencesInstance preferencesInstance) {
		super();
		this.preferencesInstance = preferencesInstance;
		format = new SimpleDateFormat("yyyydd MMMHH:mm",
				DateUtil.SWEDISH_LOCALE);
		format.setTimeZone(DateUtil.SWEDISH_TIMEZONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.acrend.sj2cal.parser.MessageParser#supports(java.lang.String)
	 */
	@Override
	public boolean supports(final String sender, final String message) {
		if (BuildConfig.DEBUG) {
			return message.contains("Ö-Tåg");
		}
		if (preferencesInstance.isParseOresund()) {
			return "Biljett 1a1".equalsIgnoreCase(sender);
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

		String date = findValue(message, "(\\d{1,2} \\D{3}) kl .*", "datum");

		String from = findValue(message, "Avg: (.*) \\d{1,2}\\:", "avreseort");
		String fromTime = findValue(message, "Avg: .* (\\d{1,2}\\:\\d{2})",
				"avgångstid");

		String to = findValue(message, "Ank: (.*) \\d{1,2}\\:", "ankomstort");
		String toTime = findValue(message, "Ank: .* (\\d{1,2}\\:\\d{2})",
				"ankomsttid");
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
			throw new IllegalArgumentException(
					"Kunde inte tolka datum. Meddelande: " + message, e);
		}
		String train = findValue(message, "[Ö|O]-T[å|a]g, T[å|a]g (\\d*)", "tågnr");
		ticket.setTrain(Integer.parseInt(train));

		String car = findValue(message, "Vagn: (\\d*),", "vagn");
		String seat = findValue(message, "Plats: (\\d*)", "plats");

		if ((car != null) && (car.length() > 0)) {
			ticket.setCar(Integer.parseInt(car));
		}
		if ((seat != null) && (seat.length() > 0)) {
			ticket.setSeat(Integer.parseInt(seat));
		}

		ticket.setCode(findValue(message, "Bilj. Nr (\\D{3}\\d{4}\\D\\d{4})",
				"Biljettkod"));

		ticket.setMessage(message);

		ticket.validate();

		return ticket;
	}

	private Calendar createCalendar(final String date, final String time) {
		Calendar cal = Calendar.getInstance();
		Calendar now = Calendar.getInstance();
		int currentYear = now.get(Calendar.YEAR);
		try {
			cal.setTime(format.parse(currentYear + date + time));
			// Om biljetten utfärdades före idag har vi gjort fel, lägg på ett
			// år
			if (cal.before(now)) {
				cal.add(Calendar.YEAR, 1);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("Kunde inte tolka datum.", e);
		}
		return cal;
	}
}
