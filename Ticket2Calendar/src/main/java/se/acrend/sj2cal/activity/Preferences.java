package se.acrend.sj2cal.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import se.acrend.sj2cal.R;
import se.acrend.sj2cal.application.Ticket2CalApp;
import se.acrend.sj2cal.calendar.CalendarListPreference;

public class Preferences extends PreferenceActivity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.preferences);

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        removePreferenceForKey("notification");
      removePreferenceForKey("deleteProcessedMessage");
    } else {
        Ticket2CalApp application = (Ticket2CalApp) getApplication();
        if (application.isGoSmsInstalled()) {
            startActivity(new Intent().setClass(this, GoSMS.class));
        }
    }

    final CheckBoxPreference processIncoming = (CheckBoxPreference) getPreferenceScreen().findPreference(
        "processIncomingMessages");
    CalendarListPreference calendarId = (CalendarListPreference) getPreferenceScreen().findPreference("calendarId");

    Cursor cursor = calendarId.getCursor();
    if (cursor != null) {
      startManagingCursor(cursor);
    }
    calendarId.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

      @Override
      public boolean onPreferenceChange(final Preference preference, final Object newValue) {
        enableProcessIncoming(processIncoming, newValue);

        return true;
      }
    });

    enableProcessIncoming(processIncoming, calendarId.getValue());
    }

  private void enableProcessIncoming(final CheckBoxPreference processIncoming, final Object newValue) {
    if (newValue == null) {
      processIncoming.setChecked(false);
      processIncoming.setEnabled(false);
    }
    String value = (String) newValue;
    int id = Integer.parseInt(value);
    if (id != -1) {
      processIncoming.setEnabled(true);
    } else {
      processIncoming.setChecked(false);
      processIncoming.setEnabled(false);
    }
  }

    void removePreferenceForKey(String key) {
        Preference p = findPreference(key);
        PreferenceScreen screen = getPreferenceScreen();
        screen.removePreference(p);
    }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
    case R.id.exit:
      this.finish();
      return true;
    case R.id.about:
      startActivity(new Intent().setClass(this, About.class));
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }
}
