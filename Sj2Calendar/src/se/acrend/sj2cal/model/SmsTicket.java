package se.acrend.sj2cal.model;

import java.text.SimpleDateFormat;

import android.content.ContentValues;

public class SmsTicket extends EventBase {

  private int train;

  @Override
  public void updateEventInformation(final ContentValues values) {
    values.put("description", this.toString());
  }

  @Override
  public String toString() {
    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    return getFrom() + " " + format.format(getDeparture().getTime()) + " - " + getTo() + " "
        + format.format(getArrival().getTime()) + " Vagn: " + getCar() + " Plats: " + getSeat() + " Biljettkod: "
        + getCode();
  }

  public int getTrain() {
    return train;
  }

  public void setTrain(final int train) {
    this.train = train;
  }

}
