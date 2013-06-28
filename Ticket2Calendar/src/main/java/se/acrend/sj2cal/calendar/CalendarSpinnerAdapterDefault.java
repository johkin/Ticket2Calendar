package se.acrend.sj2cal.calendar;

import android.content.Context;
import android.database.Cursor;

public class CalendarSpinnerAdapterDefault extends AbstractCalendarSpinnerAdapter {

  private final String[] columns = new String[] { "name", "ownerAccount", "color" };

  public CalendarSpinnerAdapterDefault(final Context context, final Cursor cursor) {
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
