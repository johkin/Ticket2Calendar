package se.acrend.sj2cal.calendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.acrend.sj2cal.model.EventBase;
import se.acrend.sj2cal.preference.PrefsHelper;
import se.acrend.sj2cal.util.DateUtil;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.ListAdapter;

public abstract class CalendarHelper {

  private static final int level = Build.VERSION.SDK_INT;

  private static CalendarHelper instance = null;

  protected String getBaseUrl() {
    if (level < Build.VERSION_CODES.FROYO) {
      return "content://calendar/";
    } else {
      return "content://com.android.calendar/";
    }
  }

  public static CalendarHelper getInstance() {
    if (instance == null) {
      if (level >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        instance = new CalendarHelperIcs();
      } else {
        instance = new CalendarHelperDefault();
      }
    }
    return instance;
  }

  public abstract ListAdapter getCalendarList(final Context context);

  public Uri addEvent(final EventBase ticket, final Context context) {
    long calendarId = PrefsHelper.getCalendarId(context);
    if (calendarId == -1) {
      return null;
    }
    ContentValues event = new ContentValues();
    event.put("calendar_id", calendarId);
    
    event.put("title", ticket.getTitle());

    event.put("description", ticket.toString());
    event.put("eventLocation", ticket.getFrom());
    event.put("dtstart", ticket.getDeparture().getTimeInMillis());
    event.put("dtend", ticket.getArrival().getTimeInMillis());

    setEventValues(event);

    Uri eventsUri = Uri.parse(getBaseUrl() + "events");
    Uri result = context.getContentResolver().insert(eventsUri, event);

    return result;
  }

  void setEventValues(final ContentValues event) {
    event.put("eventStatus", 1);
    event.put("visibility", 0);
    event.put("eventTimezone", DateUtil.SWEDISH_TIMEZONE.getID());
  }

  public List<Long> findEvents(final String ticketCode, final String ticketType, final Context context) {

    long calendarId = PrefsHelper.getCalendarId(context);
    if (calendarId == -1) {
      return Collections.emptyList();
    }

    String[] projection = new String[] { "_id" };
    Uri eventsUri = Uri.parse(getBaseUrl() + "events");

    Cursor managedCursor = context.getContentResolver().query(eventsUri, projection,
        "calendar_id = ? and description like ?",
        new String[] { Long.toString(calendarId), "%" + ticketCode + "%" + ticketType + "%" }, null);

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

  public boolean removeEvent(final long eventId, final Context context) {
    int count = 0;
    Uri uri = Uri.parse(getBaseUrl() + "events");
    Uri eventsUri = ContentUris.withAppendedId(uri, eventId);
    count = context.getContentResolver().delete(eventsUri, null, null);
    return count > 0;
  }
}
