package se.acrend.sj2cal.model;

import java.util.Calendar;

import se.acrend.sj2cal.util.ValidatorHelper;
import android.content.ContentValues;

public abstract class EventBase {

  private String from;
  private String to;
  private Calendar departure;
  private Calendar arrival;
  private int car;
  private int seat;
  private String code;

  public EventBase() {
  }

  public abstract void updateEventInformation(ContentValues values);

  public String getFrom() {
    return from;
  }

  public void setFrom(final String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(final String to) {
    this.to = to;
  }

  public Calendar getDeparture() {
    return departure;
  }

  public void setDeparture(final Calendar departure) {
    this.departure = departure;
  }

  public Calendar getArrival() {
    return arrival;
  }

  public void setArrival(final Calendar arrival) {
    this.arrival = arrival;
  }

  public int getCar() {
    return car;
  }

  public void setCar(final int car) {
    this.car = car;
  }

  public int getSeat() {
    return seat;
  }

  public void setSeat(final int seat) {
    this.seat = seat;
  }

  public String getCode() {
    return code;
  }

  public void setCode(final String code) {
    this.code = code;
  }

  public void validate() {
    ValidatorHelper.notEmpty(from, "Från");
    ValidatorHelper.notEmpty(to, "Till");
    ValidatorHelper.notEmpty(code, "Internetkod");

    ValidatorHelper.inRange(car, 0, 50, "Vagn");
    ValidatorHelper.inRange(seat, 0, 200, "Säte");

    ValidatorHelper.notEmpty(departure, "Avgång");
    ValidatorHelper.notEmpty(arrival, "Ankomst");

    if (departure.after(arrival)) {
      throw new IllegalArgumentException("Avgång kan inte vara efter ankomst.");
    }
  }

  @Override
  public String toString() {
    return "Biljettkod: " + code;
  }
}