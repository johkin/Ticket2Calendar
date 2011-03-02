package se.acrend.sj2cal.model;

import android.content.ContentValues;

public class SmsTicket extends EventBase {

  private int train;
  private String message;

  @Override
  public void updateEventInformation(final ContentValues values) {
    values.put("description", this.toString());
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
