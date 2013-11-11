package se.acrend.sj2cal.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;

import java.util.HashMap;
import java.util.Map;

public class StatisticsService extends IntentService {

  private static final String TAG = "StatisticsService";

  private EasyTracker easyTracker;

  public StatisticsService() {
    super("StatisticsService");
  }

  @Override
  protected void onHandleIntent(final Intent intent) {
      String category = intent.getStringExtra("category");
      String action = intent.getStringExtra("action");
      String label = intent.getStringExtra("label");

      Log.d(TAG, "Send  statistics: " + category + ", " + action + ", " + label);

      easyTracker.send(MapBuilder.createEvent(category, action, label, null).build());
  }

  @Override
  public void onCreate() {
    super.onCreate();
    easyTracker = EasyTracker.getInstance(this);
  }

}
