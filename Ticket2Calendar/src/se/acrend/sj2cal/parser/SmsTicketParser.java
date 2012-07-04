package se.acrend.sj2cal.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.acrend.sj2cal.BuildConfig;
import se.acrend.sj2cal.model.SmsTicket;
import se.acrend.sj2cal.util.DateUtil;

public class SmsTicketParser extends MessageParserBase implements MessageParser {

	// 11 jan kl 16:24
	// +'220572436'+
	// +'903765246'+
	// +'373740923'+
	// +'692092924'+
	// ÅRSKORT GULD
	// JOHAN KINDGREN
	// Avg. Norrköping C 16.24
	// Ank. Stockholm C 17.39
	// Tåg: 538
	// VU, 1 klass Kan återbetalas
	// Vagn 2, plats 30
	// Personlig biljett giltig med ID
	// Internet/Bilj.nr. SPG9352F0002
	// 010 624 472 391 895 723 215

	private SimpleDateFormat format = null;

	public SmsTicketParser() {
		super();
		format = new SimpleDateFormat("yyyydd MMMHH.mm",
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
			return message.contains("+'") && message.contains("'+")
					&& message.contains("Tåg:");
		}
		return "SJ Biljett".equalsIgnoreCase(sender);
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

		String from = findValue(message, "Avg. (.*) \\d{1,2}\\.", "avreseort");
		String fromTime = findValue(message, "Avg. .* (\\d{1,2}\\.\\d{2})",
				"avgångstid");

		String to = findValue(message, "Ank. (.*) \\d{1,2}\\.", "ankomstort");
		String toTime = findValue(message, "Ank. .* (\\d{1,2}\\.\\d{2})",
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
		String train = findValue(message, "T[å|a]g: (\\d*)", "tågnr");
		ticket.setTrain(Integer.parseInt(train));

		String car = findValue(message, "Vagn (\\d*),", "vagn");
		String seat = findValue(message, "plats (\\d*)", "plats");

		if ((car != null) && (car.length() > 0)) {
			ticket.setCar(Integer.parseInt(car));
		}
		if ((seat != null) && (seat.length() > 0)) {
			ticket.setSeat(Integer.parseInt(seat));
		}

		ticket.setCode(findValue(message, "Bilj.nr. (\\D{3}\\d{4}\\D\\d{4})",
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
