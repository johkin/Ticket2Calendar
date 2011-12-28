package se.acrend.sj2cal.calendar;

import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;

public class CalendarSpinnerAdapterIcs extends AbstractCalendarSpinnerAdapter {

  private final String[] columns = new String[] { CalendarContract.Calendars.NAME,
      CalendarContract.Calendars.OWNER_ACCOUNT, CalendarContract.Calendars.CALENDAR_COLOR };

  public CalendarSpinnerAdapterIcs(final Context context, final Cursor cursor) {
    super(context, cursor);
  }

  @Override
  protected String[] getColumnNames() {
    return columns;
  }

}
