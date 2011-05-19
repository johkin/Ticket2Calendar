package se.acrend.sj2cal.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.acrend.sj2cal.model.EventBase;
import se.acrend.sj2cal.preference.PrefsHelper;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.ListAdapter;
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

  public static ListAdapter getCalendarList(final Context context) {

    String[] projection = new String[] { "_id", "name", "ownerAccount", "color" };
    Uri calendars = Uri.parse(getBaseUrl() + "calendars");

    Cursor managedCursor = context.getContentResolver().query(calendars, projection, "selected=1 and access_level=700",
        null, null);

    ListAdapter adapter = null;

    if (managedCursor != null && managedCursor.moveToFirst()) {
      adapter = new CalendarSpinnerAdapter(context, managedCursor);
    } else {
      Toast.makeText(context, "Hittade ingen kalender", Toast.LENGTH_LONG).show();
    }
    return adapter;
  }

  public static boolean addEvent(final EventBase ticket, final Context context) {
    long calendarId = PrefsHelper.getCalendarId(context);
    if (calendarId == -1) {
      return false;
    }
    ContentValues event = new ContentValues();
    event.put("calendar_id", calendarId);
    String title = "Tågresa";
    if (ticket.getCar() > 0) {
      title += ", vagn " + ticket.getCar();
    }
    if (ticket.getSeat() > 0) {
      title += ", plats " + ticket.getSeat();
    }
    if ("Tågresa".equals(title)) {
      title += ", utan platsbokning";
    }
    event.put("title", title);

    event.put("description", ticket.toString());
    event.put("eventLocation", ticket.getFrom());
    event.put("dtstart", ticket.getDeparture().getTimeInMillis());
    event.put("dtend", ticket.getArrival().getTimeInMillis());
    event.put("eventStatus", 1);
    event.put("visibility", 0);

    ticket.updateEventInformation(event);

    Uri eventsUri = Uri.parse(getBaseUrl() + "events");
    context.getContentResolver().insert(eventsUri, event);

    return true;
  }

  public static List<Long> findEvents(final String ticketCode, final Context context) {

    long calendarId = PrefsHelper.getCalendarId(context);
    if (calendarId == -1) {
      return Collections.emptyList();
    }

    String[] projection = new String[] { "_id" };
    Uri eventsUri = Uri.parse(getBaseUrl() + "events");

    Cursor managedCursor = context.getContentResolver().query(eventsUri, projection,
        "calendar_id = ? and description like ?", new String[] { Long.toString(calendarId), "%" + ticketCode + "%" },
        null);

    if (managedCursor == null) {
      return Collections.emptyList();
    }

    List<Long> eventIds = new ArrayList<Long>();
    int colIndex = managedCursor.getColumnIndex(projection[0]);
    while (managedCursor.moveToNext()) {
      eventIds.add(managedCursor.getLong(colIndex));
    }

    return eventIds;
  }

  public static boolean removeEvent(final long eventId, final Context context) {
    int count = 0;
    Uri uri = Uri.parse(getBaseUrl() + "events");
    Uri eventsUri = ContentUris.withAppendedId(uri, eventId);
    count = context.getContentResolver().delete(eventsUri, null, null);
    return count > 0;
  }
}
