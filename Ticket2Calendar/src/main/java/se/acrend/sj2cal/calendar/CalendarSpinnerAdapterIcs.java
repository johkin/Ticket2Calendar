package se.acrend.sj2cal.calendar;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.provider.CalendarContract;

@TargetApi(14)
public class CalendarSpinnerAdapterIcs extends AbstractCalendarSpinnerAdapter {

  private final String[] columns = new String[]{CalendarContract.Calendars.NAME,
      CalendarContract.Calendars.OWNER_ACCOUNT, CalendarContract.Calendars.CALENDAR_COLOR};

  public CalendarSpinnerAdapterIcs(final Context context, final Cursor cursor) {
    super(context, cursor);

    nameCol = cursor.getColumnIndex(getColumnNames()[0]);
    accountCol = cursor.getColumnIndex(getColumnNames()[1]);
    colorCol = cursor.getColumnIndex(getColumnNames()[2]);
  }

  @Override
  protected String[] getColumnNames() {
    return columns;
  }

}
