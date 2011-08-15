package se.acrend.sj2cal.activity;

import se.acrend.sj2cal.R;
import se.acrend.sj2cal.preference.PrefsHelper;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class Preferences extends PreferenceActivity {

  private static final String TAG = "Preferences";

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    addPreferencesFromResource(R.xml.preferences);

    PackageManager packageManager = getPackageManager();

    try {
      ApplicationInfo applicationInfo = packageManager.getApplicationInfo("com.jb.gosms", PackageManager.GET_META_DATA);
      startActivity(new Intent().setClass(this, GoSMS.class));
    } catch (NameNotFoundException e) {
      Log.d(TAG, "GoSMS not installed.");
    }

    if (PrefsHelper.isShowAbout(getApplicationContext())) {
      startActivity(new Intent().setClass(this, About.class));
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
