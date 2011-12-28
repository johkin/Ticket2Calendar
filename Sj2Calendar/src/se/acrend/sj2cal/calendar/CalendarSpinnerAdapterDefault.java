package se.acrend.sj2cal.calendar;

import android.content.Context;
import android.database.Cursor;

public class CalendarSpinnerAdapterDefault extends AbstractCalendarSpinnerAdapter {

  private final String[] columns = new String[] { "name", "ownerAccount", "color" };

  public CalendarSpinnerAdapterDefault(final Context context, final Cursor cursor) {
    super(context, cursor);
  }

  @Override
  protected String[] getColumnNames() {
    return columns;
  }

}
