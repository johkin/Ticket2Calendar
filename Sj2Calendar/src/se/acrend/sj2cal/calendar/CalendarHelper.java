package se.acrend.sj2cal.calendar;

import se.acrend.sj2cal.model.EventBase;
import se.acrend.sj2cal.util.PrefsHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class CalendarHelper {

  private static final int level = Build.VERSION.SDK_INT;

  private static String getBaseUrl() {
    if (level < 8) {
      return "content://calendar/";
    } else {
      return "content://com.android.calendar/";
    }
  }

  public static SpinnerAdapter getCalendarList(final Context context) {

    String[] projection = new String[] { "_id", "name", "ownerAccount", "color" };
    Uri calendars = Uri.parse(getBaseUrl() + "calendars");

    Cursor managedCursor = context.getContentResolver().query(calendars, projection, "selected=1", null, null);

    SpinnerAdapter adapter = null;

    if (managedCursor != null && managedCursor.moveToFirst()) {
      adapter = new CalendarSpinnerAdapter(context, managedCursor);
    } else {
      Toast.makeText(context, "Hittade ingen kalender", Toast.LENGTH_LONG).show();
    }
    return adapter;
  }

  public static boolean addEvent(final EventBase ticket, final Context context) {
    try {
      long calendarId = PrefsHelper.getCalendarId(context);

      ContentValues event = new ContentValues();
      event.put("calendar_id", calendarId);
      event.put("title", "Tågresa " + ticket.getCar() + ", " + ticket.getSeat());

      event.put("description", ticket.toString());
      event.put("eventLocation", ticket.getFrom());
      event.put("dtstart", ticket.getDeparture().getTimeInMillis());
      event.put("dtend", ticket.getArrival().getTimeInMillis());
      event.put("eventStatus", 1);
      event.put("visibility", 0);

      ticket.updateEventInformation(event);

      Uri eventsUri = Uri.parse(getBaseUrl() + "events");
      context.getContentResolver().insert(eventsUri, event);
      Toast.makeText(context, "Lade till biljett " + ticket.getCode(), Toast.LENGTH_LONG).show();
    } catch (IllegalArgumentException e) {
      Log.e("Cal Event", "Fel vid kalender.", e);
      Toast.makeText(context, "Kunde inte lägga till biljett: " + ticket.getCode() + ". Fel: " + e.getMessage(),
          Toast.LENGTH_LONG).show();
      return false;
    }
    return true;
  }

}
