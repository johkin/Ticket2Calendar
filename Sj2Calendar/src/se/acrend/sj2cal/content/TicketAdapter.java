package se.acrend.sj2cal.content;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import se.acrend.sj2cal.R;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class TicketAdapter extends SimpleCursorAdapter {

  private static final String[] columnNames = new String[] { "ticketCode", "departure", "arrival", "fromStation",
      "toStation" };

  private final int[] columnIds = new int[columnNames.length];

  private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");

  public TicketAdapter(final Context context, final Cursor cursor) {
    super(context, 0, cursor, new String[] {}, new int[] {});

    for (int i = 0; i < columnNames.length; i++) {
      columnIds[i] = cursor.getColumnIndex(columnNames[i]);
    }
  }

  @Override
  public View newView(final Context context, final Cursor c, final ViewGroup parent) {
    final LayoutInflater inflater = LayoutInflater.from(context);
    View v = inflater.inflate(R.layout.ticket_item, parent, false);

    return v;
  }

  @Override
  public View newDropDownView(final Context context, final Cursor cursor, final ViewGroup parent) {
    final LayoutInflater inflater = LayoutInflater.from(context);
    View v = inflater.inflate(R.layout.ticket_item, parent, false);

    return v;
  }

  @Override
  public void bindView(final View v, final Context context, final Cursor cursor) {

    String code = cursor.getString(columnIds[0]);
    long departureMillis = cursor.getLong(columnIds[1]);
    long arrivalMillis = cursor.getLong(columnIds[2]);
    String from = cursor.getString(columnIds[3]);
    String to = cursor.getString(columnIds[4]);

    TextView codeText = (TextView) v.findViewById(R.id.ticketCode);
    codeText.setText(code);

    TextView fromText = (TextView) v.findViewById(R.id.fromStation);
    fromText.setText(from);
    TextView toText = (TextView) v.findViewById(R.id.toStation);
    toText.setText(to);

    Calendar departure = Calendar.getInstance();
    departure.setTimeInMillis(departureMillis);

    Calendar arrival = Calendar.getInstance();
    arrival.setTimeInMillis(arrivalMillis);

    TextView depText = (TextView) v.findViewById(R.id.departure);
    depText.setText(dateFormat.format(departure.getTime()));
    TextView arrText = (TextView) v.findViewById(R.id.arrival);
    arrText.setText(dateFormat.format(arrival.getTime()));

  }
}
