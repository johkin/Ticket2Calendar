package se.acrend.sj2cal.model;

import java.text.SimpleDateFormat;

import se.acrend.sj2cal.util.DateUtil;

public class SmsTicket extends EventBase {

  public static final String TICKET_TYPE = "Ticket2Cal.Mobilbiljett";
  private int train;
  private String message;

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

  public String getUrl() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", DateUtil.SWEDISH_LOCALE);
    String date = dateFormat.format(getDeparture().getTime());
    return String
        .format(DateUtil.SWEDISH_LOCALE, "http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=%s,%d",
            date, train);
  }

  @Override
  public String toString() {
    return message + "\n\n" + getUrl() + "\n\n" + TICKET_TYPE;
  }

  public int getTrain() {
    return train;
  }

  public void setTrain(final int train) {
    this.train = train;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }
}
