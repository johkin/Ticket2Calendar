package se.acrend.sj2cal.content;

import se.acrend.sj2cal.model.SmsTicket;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class ProviderHelper {

  private static final String TAG = "ProviderHelper";

  public static TicketAdapter getTicketAdapter(final Context context) {

    String[] projection = new String[] { "_id", "ticketCode", "departure", "arrival", "fromStation", "toStation" };
    Uri tickets = Uri.parse("content://se.acrend.sj2cal/tickets");

    Cursor managedCursor = context.getContentResolver().query(tickets, projection, null, null, null);

    TicketAdapter adapter = null;

    if (managedCursor != null && managedCursor.moveToFirst()) {
      adapter = new TicketAdapter(context, managedCursor);
    } else {
      Toast.makeText(context, "Hittade inga biljetter", Toast.LENGTH_LONG).show();
    }
    return adapter;
  }

  public static long addEvent(final SmsTicket ticket, Long calendarEventId, final Context context) {
    try {

      ContentValues event = new ContentValues();
      event.put("ticketCode", ticket.getCode());
      event.put("fromStation", ticket.getFrom());
      event.put("toStation", ticket.getTo());
      event.put("trainNo", ticket.getTrain());
      event.put("arrival", ticket.getArrival().getTimeInMillis());
      event.put("departure", ticket.getDeparture().getTimeInMillis());
      event.put("ticketText", ticket.toString());

      Uri eventsUri = Uri.parse("content://se.acrend.sj2cal/tickets");
      Uri result = context.getContentResolver().insert(eventsUri, event);
      Log.d(TAG, result.toString());
      Log.d(TAG, result.getLastPathSegment());
      return Long.parseLong(result.getLastPathSegment());
    } catch (IllegalArgumentException e) {
      Log.e("Cal Event", "Fel vid kalender.", e);
      return -1;
    }
  }

  // public static List<Long> findEvents(final String ticketCode, final Context
  // context) {
  //
  // long calendarId = PrefsHelper.getCalendarId(context);
  // if (calendarId == -1) {
  // return Collections.emptyList();
  // }
  //
  // String[] projection = new String[] { "_id" };
  // Uri eventsUri = Uri.parse(getBaseUrl() + "events");
  //
  // Cursor managedCursor = context.getContentResolver().query(eventsUri,
  // projection,
  // "calendar_id = " + calendarId + " and description like '%" + ticketCode +
  // "%'", null, null);
  //
  // if (managedCursor == null) {
  // return Collections.emptyList();
  // }
  //
  // List<Long> eventIds = new ArrayList<Long>();
  // int colIndex = managedCursor.getColumnIndex(projection[0]);
  // while (managedCursor.moveToNext()) {
  // eventIds.add(managedCursor.getLong(colIndex));
  // }
  //
  // return eventIds;
  // }
  //
  // public static boolean removeEvent(final long eventId, final Context
  // context) {
  //
  // Uri eventsUri = Uri.parse(getBaseUrl() + "events");
  // int count = context.getContentResolver().delete(eventsUri, "_id = ?", new
  // String[] { Long.toString(eventId) });
  // return count > 0;
  // }
}
