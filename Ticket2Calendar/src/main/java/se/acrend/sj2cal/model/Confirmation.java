package se.acrend.sj2cal.model;

import java.text.SimpleDateFormat;

public class Confirmation extends EventBase {

	public static final String TICKET_TYPE = "Ticket2Cal.Bokningsbekräftelse";

	@Override
	public String getTitle() {
		String title = "Tågresa";
		if (getCar() > 0) {
			title += ", vagn " + getCar();
		}
		if (getSeat() > 0) {
			title += ", plats " + getSeat();
		}
		if ("Tågresa".equals(title)) {
			title += ", utan platsbokning";
		}
		return title;
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		return getFrom() + " " + format.format(getDeparture().getTime())
				+ " - " + getTo() + " " + format.format(getArrival().getTime())
				+ " Vagn: " + getCar() + " Plats: " + getSeat()
				+ " Biljettkod: " + getCode() + "\n\n" + TICKET_TYPE;
	}
}
