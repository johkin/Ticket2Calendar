package se.acrend.sj2cal.preference;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.Preference;
import android.util.AttributeSet;

public class LinkPreference extends Preference {

  private String value;

  public LinkPreference(final Context context) {
    super(context);
  }

  public LinkPreference(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LinkPreference(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);
    value = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "value");
  }

  @Override
  protected void onClick() {
    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(value)));
  }
}
