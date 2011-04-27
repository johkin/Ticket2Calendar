package se.acrend.sj2cal.activity;

import se.acrend.sj2cal.R;
import se.acrend.sj2cal.calendar.CalendarHelper;
import se.acrend.sj2cal.util.PrefsHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Settings extends Activity {
  private CheckBox scanIncoming;
  private CheckBox deleteProcessed;
  private CheckBox replaceTickets;
  private Spinner calendarList;

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.settings);

    scanIncoming = (CheckBox) findViewById(R.id.ScanIncoming);
    deleteProcessed = (CheckBox) findViewById(R.id.DeleteProcessedMsgs);
    replaceTickets = (CheckBox) findViewById(R.id.ReplaceTickets);

    scanIncoming.setChecked(PrefsHelper.isProcessIncommingMessages(this));
    scanIncoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        PrefsHelper.setProcessIncommingMessages(isChecked, Settings.this);

      }
    });
    deleteProcessed.setChecked(PrefsHelper.isDeleteProcessedMessages(this));
    deleteProcessed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        PrefsHelper.setDeleteProcessedMessages(isChecked, Settings.this);
      }
    });
    replaceTickets.setChecked(PrefsHelper.isReplaceTicket(this));
    replaceTickets.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        PrefsHelper.setReplaceTicket(isChecked, Settings.this);
      }
    });
    createCalendarList();

    if (PrefsHelper.isShowAbout(getApplicationContext())) {
      showHelp();
    }
  }

  private void showHelp() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    final AlertDialog dialog = builder.setTitle(R.string.about_header).setMessage(R.string.about_text)
        .setCancelable(true).setIcon(R.drawable.sj2cal_bw).create();

    dialog.show();
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
    case R.id.exit:
      this.finish();
      return true;
    case R.id.about:
      showHelp();
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

  private void createCalendarList() {
    calendarList = (Spinner) findViewById(R.id.CalendarList);

    SpinnerAdapter adapter = CalendarHelper.getCalendarList(getApplicationContext());
    calendarList.setAdapter(adapter);

    long calendarId = PrefsHelper.getCalendarId(getApplicationContext());
    for (int pos = 0; pos < calendarList.getCount(); pos++) {
      long itemId = adapter.getItemId(pos);
      if (itemId == calendarId) {
        calendarList.setSelection(pos);
        break;
      }
    }
    calendarList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(final AdapterView<?> parent, final View view, final int pos, final long id) {
        PrefsHelper.setCalendarId(id, Settings.this.getApplicationContext());
      }

      @Override
      public void onNothingSelected(final AdapterView<?> parent) {
        PrefsHelper.setCalendarId(-1, Settings.this.getApplicationContext());
      }
    });
  }
}