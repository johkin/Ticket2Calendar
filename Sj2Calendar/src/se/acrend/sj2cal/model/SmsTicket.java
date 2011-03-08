package se.acrend.sj2cal.model;

import java.text.SimpleDateFormat;

import android.content.ContentValues;

public class SmsTicket extends EventBase {

  private int train;
  private String message;

  @Override
  public void updateEventInformation(final ContentValues values) {
    String description = message + "\n\n" + getUrl();
    values.put("description", description);
  }

  public String getUrl() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String date = dateFormat.format(this.getDeparture().getTime());
    return String.format("http://m.trafikverket.se/TAGTRAFIK/WapPages/TrainShow.aspx?train=%s,%d", date, train);
    // http://m.trafikverket.se/TAGTRAFIK/WapPages/TrainShow.aspx?train=20110307,10523
  }

  @Override
  public String toString() {
    return message;
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
