package se.acrend.sj2cal.calendar;

import se.acrend.sj2cal.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CalendarSpinnerAdapter extends SimpleCursorAdapter {

  private static final String[] columns = new String[] { "name", "ownerAccount", "color" };

  private static final int layout = R.layout.list_entry;

  public CalendarSpinnerAdapter(final Context context, final Cursor cursor) {
    super(context, layout, cursor, new String[] {}, new int[] {});
  }

  @Override
  public View newView(final Context context, final Cursor c, final ViewGroup parent) {

    final LayoutInflater inflater = LayoutInflater.from(context);
    View v = inflater.inflate(layout, parent, false);

    int nameCol = c.getColumnIndex(columns[0]);
    String name = c.getString(nameCol);
    int accountCol = c.getColumnIndex(columns[1]);
    String account = c.getString(accountCol);
    int colorCol = c.getColumnIndex(columns[2]);
    int colorValue = c.getInt(colorCol);

    TextView colorBox = (TextView) v.findViewById(R.id.colorBox);
    colorBox.setBackgroundColor(colorValue);

    TextView name_text = (TextView) v.findViewById(R.id.calendar_name);
    name_text.setText(name);
    TextView email_text = (TextView) v.findViewById(R.id.calendar_email);
    email_text.setText(account);

    return v;
  }

  @Override
  public View newDropDownView(final Context context, final Cursor cursor, final ViewGroup parent) {
    return newView(context, cursor, parent);
  }

  @Override
  public void bindView(final View v, final Context context, final Cursor c) {
    int nameCol = c.getColumnIndex(columns[0]);
    String name = c.getString(nameCol);
    int accountCol = c.getColumnIndex(columns[1]);
    String account = c.getString(accountCol);
    int colorCol = c.getColumnIndex(columns[2]);
    int colorValue = c.getInt(colorCol);

    TextView colorBox = (TextView) v.findViewById(R.id.colorBox);
    colorBox.setBackgroundColor(colorValue);
    TextView name_text = (TextView) v.findViewById(R.id.calendar_name);
    name_text.setText(name);
    TextView email_text = (TextView) v.findViewById(R.id.calendar_email);
    email_text.setText(account);
  }

}
