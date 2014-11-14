package se.acrend.sj2cal.calendar;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CursorAdapter;

public class CalendarListPreference extends ListPreference {

  private static final String TAG = "CalendarListPreference";

  private CursorAdapter adapter;

  private long calendarId = -1;

  public CalendarListPreference(final Context context) {
    super(context);
  }

  public CalendarListPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  void updateSummary() {
    try {
      calendarId = Long.parseLong(getPersistedString("-1"));
    } catch (NumberFormatException ignore) {
      Log.d(TAG, "Kunde inte läsa calendarId: " + getValue());
    }
    String summary = "<Ingen kalender vald>";
    if (calendarId != -1) {
      for (int pos = 0; pos < getAdapter().getCount(); pos++) {
        if (getAdapter().getItemId(pos) == calendarId) {
          summary = (String) getAdapter().getItem(pos);
        }
      }
    }
    setSummary(summary);
  }

  @Override
  protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
    super.onSetInitialValue(restoreValue, defaultValue);
    updateSummary();
  }

  @Override
  protected void onPrepareDialogBuilder(final Builder builder) {
    builder.setSingleChoiceItems(getAdapter(), -1, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(final DialogInterface dialog, final int which) {
        calendarId = getAdapter().getItemId(which);
        CalendarListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
        dialog.dismiss();
      }
    });
    builder.setPositiveButton(null, null);
  }

  @Override
  protected void onDialogClosed(final boolean positiveResult) {
    super.onDialogClosed(positiveResult);
    String strVal = Long.toString(calendarId);
    if (positiveResult && callChangeListener(strVal)) {
      setValue(strVal);
      updateSummary();
    }
  }

  protected CursorAdapter getAdapter() {
    if (adapter == null) {
      adapter = CalendarHelper.getInstance().getCalendarList(getContext());
    }
    return adapter;
  }


  public Cursor getCursor() {
    if (getAdapter() != null) {
      return getAdapter().getCursor();
    }
    return null;
  }
}
