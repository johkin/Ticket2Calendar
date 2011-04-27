package se.acrend.sj2cal.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PrefsHelper {

  public static SharedPreferences getPrefs(final Context context) {
    return context.getSharedPreferences("Sj2Calendar", 0);
  }

  public static void setCalendarId(final long id, final Context context) {
    SharedPreferences prefs = getPrefs(context);
    Editor editor = prefs.edit();
    editor.putLong("calendarId", id);
    editor.commit();
  }

  public static void setDeleteProcessedMessages(final boolean delete, final Context context) {
    SharedPreferences prefs = getPrefs(context);
    Editor editor = prefs.edit();
    editor.putBoolean("deleteProcessedMessage", delete);
    editor.commit();
  }

  public static void setReplaceTicket(final boolean replace, final Context context) {
    SharedPreferences prefs = getPrefs(context);
    Editor editor = prefs.edit();
    editor.putBoolean("replaceTicket", replace);
    editor.commit();
  }

  public static void setProcessIncommingMessages(final boolean process, final Context context) {
    SharedPreferences prefs = getPrefs(context);
    Editor editor = prefs.edit();
    editor.putBoolean("processIncomingMessages", process);
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

  public static boolean isShowAbout(final Context context) {
    PackageManager manager = context.getPackageManager();
    try {
      PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
      SharedPreferences prefs = getPrefs(context);

      int versionCode = info.versionCode;
      int storedVersion = prefs.getInt("versionCode", -1);
      if (storedVersion < versionCode) {
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

  public static long getCalendarId(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getLong("calendarId", -1);
  }
}
