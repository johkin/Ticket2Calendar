package se.acrend.sj2cal.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

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

  public static void setProcessIncommingMessages(final boolean process, final Context context) {
    SharedPreferences prefs = getPrefs(context);
    Editor editor = prefs.edit();
    editor.putBoolean("processIncomingMessages", process);
    editor.commit();
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

  public static boolean isProcessIncommingMessages(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("processIncomingMessages", false);
  }

  public static boolean isShowAbout(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getBoolean("showAbout", true);
  }

  public static long getCalendarId(final Context context) {
    SharedPreferences prefs = getPrefs(context);
    return prefs.getLong("calendarId", -1);
  }
}
