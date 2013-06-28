package se.acrend.sj2cal.calendar;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.widget.CursorAdapter;
import android.widget.Toast;

public class CalendarHelperDefault extends CalendarHelper {

  @Override
  public CursorAdapter getCalendarList(final Context context) {

    String[] projection = new String[] { "_id", "name", "ownerAccount", "color" };
    Uri calendars = Uri.parse(getBaseUrl() + "calendars");

    Cursor managedCursor = context.getContentResolver().query(calendars, projection, "selected=1 and access_level=700",
        null, null);

    CursorAdapter adapter = null;

    if ((managedCursor != null) && managedCursor.moveToFirst()) {
      adapter = new CalendarSpinnerAdapterDefault(context, managedCursor);
    } else {
      Toast.makeText(context, "Hittade ingen kalender", Toast.LENGTH_LONG).show();
    }
    return adapter;
  }

}
