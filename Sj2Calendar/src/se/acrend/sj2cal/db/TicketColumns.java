package se.acrend.sj2cal.db;

import android.provider.BaseColumns;

public interface TicketColumns extends BaseColumns {

  public static final String TICKET_CODE = "ticketCode";
  public static final String TICKET_TEXT = "ticketText";
  public static final String TRAIN_NO = "trainNo";
  public static final String DEPARTURE = "departure";
  public static final String ARRIVAL = "arrival";
  public static final String FROM = "fromStation";
  public static final String TO = "toStation";
  public static final String NOTIFY = "notify";
  public static final String DELAYED_MINUTES = "delayedMinutes";
  public static final String CALENDAR_EVENT_ID = "calendarEventId";

}
