package se.acrend.sj2cal.activity;

import se.acrend.sj2cal.R;
import se.acrend.sj2cal.calendar.CalendarHelper;
import se.acrend.sj2cal.util.PrefsHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class Home extends Activity {
  private CheckBox scanIncoming;
  private CheckBox deleteProcessed;
  private Spinner calendarList;

  /** Called when the activity is first created. */

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    scanIncoming = (CheckBox) findViewById(R.id.ScanIncoming);
    deleteProcessed = (CheckBox) findViewById(R.id.DeleteProcessedMsgs);

    scanIncoming.setChecked(PrefsHelper.isProcessIncommingMessages(this));
    scanIncoming.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        PrefsHelper.setProcessIncommingMessages(isChecked, Home.this);

      }
    });
    deleteProcessed.setChecked(PrefsHelper.isDeleteProcessedMessages(this));
    deleteProcessed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        PrefsHelper.setDeleteProcessedMessages(isChecked, Home.this);

      }
    });
    createCalendarList();

    Button closeButton = (Button) findViewById(R.id.Close);
    closeButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(final View v) {
        Home.this.finish();
      }
    });

    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    final AlertDialog dialog = builder.setTitle(R.string.about_header).setMessage(R.string.about_text)
        .setCancelable(true).setIcon(R.drawable.sj2cal_bw).create();

    if (PrefsHelper.isShowAbout(getApplicationContext())) {
      dialog.show();
      PrefsHelper.setShowAbout(false, getApplicationContext());
    }

    Button aboutButton = (Button) findViewById(R.id.About);
    aboutButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(final View v) {
        dialog.show();
      }
    });
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
        PrefsHelper.setCalendarId(id, Home.this.getApplicationContext());
      }

      @Override
      public void onNothingSelected(final AdapterView<?> parent) {
        PrefsHelper.setCalendarId(-1, Home.this.getApplicationContext());
      }
    });
  }
}