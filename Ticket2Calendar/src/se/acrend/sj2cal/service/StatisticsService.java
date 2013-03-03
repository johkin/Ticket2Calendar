package se.acrend.sj2cal.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

public class StatisticsService extends IntentService {

  private static final String TAG = "StatisticsService";

  private EasyTracker easyTracker;

  public StatisticsService() {
    super("StatisticsService");
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
    Tracker tracker = null;
    try {
      tracker = EasyTracker.getTracker();

      String category = intent.getStringExtra("category");
      String action = intent.getStringExtra("action");
      String label = intent.getStringExtra("label");

      Log.d(TAG, "Send  statistics: " + category + ", " + action + ", " + label);

      tracker.trackEvent(category, action, label, null);
    } finally {
      if (tracker != null) {
        tracker.close();
      }
    }
  }

  @Override
  public void onCreate() {
    super.onCreate();
    easyTracker = EasyTracker.getInstance();
    easyTracker.setContext(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (easyTracker != null) {
      easyTracker.dispatch();
    }
  }

}
