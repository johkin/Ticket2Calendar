package se.acrend.sj2cal.activity;

import com.google.analytics.tracking.android.EasyTracker;

import se.acrend.sj2cal.R;
import se.acrend.sj2cal.application.Sj2CalApp;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Preferences extends PreferenceActivity {
  
  @Override
  public void onStart() {
    super.onStart();
    EasyTracker.getInstance().activityStart(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    EasyTracker.getInstance().activityStop(this);
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.preferences);

    final CheckBoxPreference processIncoming = (CheckBoxPreference) getPreferenceScreen().findPreference(
        "processIncomingMessages");
    ListPreference calendarId = (ListPreference) getPreferenceScreen().findPreference("calendarId");
    calendarId.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

      @Override
      public boolean onPreferenceChange(final Preference preference, final Object newValue) {
        enableProcessIncoming(processIncoming, newValue);

        return true;
      }
    });

    enableProcessIncoming(processIncoming, calendarId.getValue());

    Sj2CalApp application = (Sj2CalApp) getApplication();
    if (application.isGoSmsInstalled()) {
      startActivity(new Intent().setClass(this, GoSMS.class));
    }
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
