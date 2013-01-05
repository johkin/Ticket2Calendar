package se.acrend.sj2cal.application;

import se.acrend.sj2cal.activity.About;
import se.acrend.sj2cal.preference.PrefsHelper;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Sj2CalApp extends Application {

  private static final String COM_JB_GOSMS = "com.jb.gosms";
  private static final String SJ_2_CAL = "se.acrend.sj2cal";

  private static final String TAG = "Sj2CalApp";

  @Override
  public void onCreate() {
    super.onCreate();

    if (PrefsHelper.isShowAbout(getApplicationContext())) {
      startActivity(new Intent().setClass(this, About.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
  }

  public boolean isGoSmsInstalled() {
    PackageManager packageManager = getPackageManager();
    try {
      packageManager.getApplicationInfo(COM_JB_GOSMS, PackageManager.GET_META_DATA);
      return true;
    } catch (NameNotFoundException e) {
      Log.d(TAG, "GoSMS not installed.");
      return false;
    }
  }
}
