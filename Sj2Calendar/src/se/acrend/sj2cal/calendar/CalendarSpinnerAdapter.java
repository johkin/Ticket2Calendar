package se.acrend.sj2cal.calendar;

import se.acrend.sj2cal.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CalendarSpinnerAdapter extends SimpleCursorAdapter {

  private static final String[] columns = new String[] { "name", "ownerAccount", "color" };

  private final int nameCol;
  private final int accountCol;
  private final int colorCol;

  public CalendarSpinnerAdapter(final Context context, final Cursor cursor) {
    super(context, 0, cursor, new String[] {}, new int[] {});
    nameCol = cursor.getColumnIndex(columns[0]);
    accountCol = cursor.getColumnIndex(columns[1]);
    colorCol = cursor.getColumnIndex(columns[2]);
  }

  @Override
  public Object getItem(final int position) {
    Cursor cursor = getCursor();

    cursor.moveToPosition(position);
    String name = cursor.getString(nameCol);
    if (name == null) {
      name = "<" + cursor.getString(accountCol) + ">";
    }
    return name;
  }

  @Override
  public View newView(final Context context, final Cursor c, final ViewGroup parent) {
    final LayoutInflater inflater = LayoutInflater.from(context);
    View v = inflater.inflate(R.layout.calendars_dropdown_item, parent, false);

    return v;
  }

  @Override
  public void bindView(final View v, final Context context, final Cursor cursor) {

    String name = cursor.getString(nameCol);
    String account = cursor.getString(accountCol);
    int colorValue = cursor.getInt(colorCol);

    if (name == null || name.trim().length() == 0) {
      name = "<" + account + ">";
    }

    View colorBar = v.findViewById(R.id.color);
    if (colorBar != null) {
      colorBar.setBackgroundDrawable(getColorChip(colorValue));
    }
    TextView name_text = (TextView) v.findViewById(R.id.calendar_name);
    name_text.setText(name);
    TextView email_text = (TextView) v.findViewById(R.id.account_name);
    if (email_text != null) {
      email_text.setText(account);
    }
  }

  private static final int CLEAR_ALPHA_MASK = 0x00FFFFFF;
  private static final int HIGH_ALPHA = 255 << 24;
  private static final int MED_ALPHA = 180 << 24;
  private static final int LOW_ALPHA = 150 << 24;

  /* The corner should be rounded on the top right and bottom right */
  private static final float[] CORNERS = new float[] { 0, 0, 5, 5, 5, 5, 0, 0 };

  private Drawable getColorChip(int color) {
    /*
     * We want the color chip to have a nice gradient using the color of the
     * calendar. To do this we use a GradientDrawable. The color supplied has an
     * alpha of FF so we first do: color & 0x00FFFFFF to clear the alpha. Then
     * we add our alpha to it. We use 3 colors to get a step effect where it
     * starts off very light and quickly becomes dark and then a slow transition
     * to be even darker.
     */
    color &= CLEAR_ALPHA_MASK;
    int startColor = color | HIGH_ALPHA;
    int middleColor = color | MED_ALPHA;
    int endColor = color | LOW_ALPHA;
    int[] colors = new int[] { startColor, middleColor, endColor };
    GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
    d.setCornerRadii(CORNERS);
    return d;
  }

}
