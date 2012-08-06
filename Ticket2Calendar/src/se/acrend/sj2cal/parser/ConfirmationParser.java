package se.acrend.sj2cal.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.acrend.sj2cal.model.Confirmation;
import se.acrend.sj2cal.preference.PreferencesInstance;
import se.acrend.sj2cal.util.DateUtil;

public class ConfirmationParser extends MessageParserBase implements
		MessageParser {

	// * SJ * BKD3723G0002 Datum: 110114 Avg: Norrköping C 16.24
	// Ank: Stockholm C 17.39 Vagn: 2 Plats: 25
	// Internet ombord X2000/Dubbeldäckare Kod: BKD3723G0002

	private SimpleDateFormat format = null;

	private PreferencesInstance preferencesInstance;

	public ConfirmationParser(PreferencesInstance preferencesInstance) {
		super();
		this.preferencesInstance = preferencesInstance;
		format = new SimpleDateFormat("yyMMddHH.mm", DateUtil.SWEDISH_LOCALE);
		format.setTimeZone(DateUtil.SWEDISH_TIMEZONE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.acrend.sj2cal.parser.MessageParser#supports(java.lang.String)
	 */
	@Override
	public boolean supports(final String sender, final String message) {
		if (preferencesInstance.isParseSj()) {
			return message.contains("* SJ *");
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see se.acrend.sj2cal.parser.MessageParser#parse(java.lang.String)
	 */
	@Override
	public Confirmation parse(final String message) {
		Confirmation ticket = new Confirmation();

		ticket.setCode(findValue(message, "\\* SJ \\* (\\D{3}\\d{4}\\D\\d{4})",
				"Biljettkod"));
		String date = findValue(message, "Datum: (\\d{6})", "datum");
		String from = findValue(message, "Avg: (.+) \\d{1,2}\\.\\d{2} Ank",
				"avgångsort");
		ticket.setFrom(from);
		String depTime = findValue(message, "Avg: .+ (\\d{1,2}\\.\\d{2}) Ank",
				"avgångstid");
		String to = findValue(message, "Ank: (.+) \\d{1,2}\\.", "ankomstort");
		String arrTime = findValue(message, "Ank: .+ (\\d{1,2}\\.\\d{2})",
				"ankomsttid");
		String car = findValue(message, "Vagn: (\\d{1,2})", "vagn");
		String seat = findValue(message, "Plats: (\\d{1,3})", "plats");

		try {
			Calendar departure = Calendar.getInstance();
			departure.setTime(format.parse(date + depTime));
			ticket.setDeparture(departure);
		} catch (ParseException e) {
			throw new IllegalArgumentException(
					"Kunde inte tolka avgångsdatum.", e);
		}
		ticket.setTo(to);
		try {
			Calendar arrival = Calendar.getInstance();
			arrival.setTime(format.parse(date + arrTime));
			if (arrival.before(ticket.getDeparture())) {
				arrival.add(Calendar.DAY_OF_YEAR, 1);
			}
			ticket.setArrival(arrival);
		} catch (ParseException e) {
			throw new IllegalArgumentException(
					"Kunde inte tolka ankomstdatum.", e);
		}
		if (car != null && car.length() > 0) {
			ticket.setCar(Integer.parseInt(car));
		}
		if (seat != null && seat.length() > 0) {
			ticket.setSeat(Integer.parseInt(seat));
		}

		ticket.validate();

		return ticket;
	}
}
