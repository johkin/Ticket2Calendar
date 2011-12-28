package se.acrend.sj2cal.calendar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.widget.ListAdapter;
import android.widget.Toast;

public class CalendarHelperIcs extends CalendarHelper {

  @Override
  public ListAdapter getCalendarList(final Context context) {

    String[] projection = new String[] { BaseColumns._ID, CalendarContract.Calendars.NAME,
        CalendarContract.Calendars.OWNER_ACCOUNT, CalendarContract.Calendars.CALENDAR_COLOR };
    Uri calendars = Uri.parse(getBaseUrl() + "calendars");

    Cursor managedCursor = context.getContentResolver().query(calendars, projection,
        CalendarContract.Calendars.VISIBLE + "=1 and " + CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL + "=700",
        null, null);

    ListAdapter adapter = null;

    if ((managedCursor != null) && managedCursor.moveToFirst()) {
      adapter = new CalendarSpinnerAdapterIcs(context, managedCursor);
    } else {
      Toast.makeText(context, "Hittade ingen kalender", Toast.LENGTH_LONG).show();
    }
    return adapter;
  }

}
