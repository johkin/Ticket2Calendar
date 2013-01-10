package se.acrend.sj2cal.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class PrefsHelper {

  public static SharedPreferences getPrefs(final Context context) {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  public static void setShowAbout(final boolean show, final Context context) {
    SharedPreferences prefs = getPrefs(context);
    Editor editor = prefs.edit();
    editor.putBoolean("showAbout", show);
    editor.commit();
  }

  public static boolean isDeleteProcessedMessages(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("deleteProcessedMessage", false);
  }

  public static boolean isReplaceTicket(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("replaceTicket", false);
  }

  public static boolean isProcessIncommingMessages(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("processIncomingMessages", false);
  }

  public static long getCalendarId(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return Long.parseLong(prefs.getString("calendarId", "-1"));
  }

  public static String getNotificationSound(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getString("notifySound", Settings.System.DEFAULT_NOTIFICATION_URI.toString());
  }

  public static boolean isNotificationVibration(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("notifyVibration", false);
  }

  public static boolean isParseSj(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("parseSJ", true);
  }

  public static boolean isParseSwebus(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("parseSwebus", true);
  }

  public static boolean isParseOresund(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("parseOresund", true);
  }

  public static PreferencesInstance getInstance(final Context context) {
    return new DefaultPreferencesInstance(context);
  }

  public static boolean isShowAbout(final Context context) {
    PackageManager manager = context.getPackageManager();
    try {
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
      SharedPreferences prefs = getPrefs(context);

      int versionCode = info.versionCode;
      int storedVersion = prefs.getInt("versionCode", -1);
      if (storedVersion != versionCode) {
        Editor editor = prefs.edit();
        editor.putInt("versionCode", versionCode);
        editor.commit();
        return true;
      }
      return false;
    } catch (Exception e) {
      throw new RuntimeException("Kunde inte hämta information för paket: " + context.getPackageName(), e);
    }
  }

  static class DefaultPreferencesInstance implements PreferencesInstance {

    private final Context context;

    public DefaultPreferencesInstance(final Context context) {
      this.context = context;
    }

    @Override
    public boolean isParseSj() {
      return PrefsHelper.isParseSj(context);
    }

    @Override
    public boolean isParseSwebus() {
      return PrefsHelper.isParseSj(context);
    }

    @Override
    public boolean isParseOresund() {
      return PrefsHelper.isParseOresund(context);
    }

  }
}
