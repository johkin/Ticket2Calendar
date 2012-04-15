package se.acrend.sj2cal.model;

import java.text.SimpleDateFormat;

public class SmsTicket extends EventBase {

  public static final String TICKET_TYPE = "Ticket2Cal.Mobilbiljett";
  private int train;
  private String message;

  public String getUrl() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String date = dateFormat.format(getDeparture().getTime());
    return String.format("http://www5.trafikverket.se/taginfo/WapPages/TrainShow.aspx?train=%s,%d", date, train);
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
