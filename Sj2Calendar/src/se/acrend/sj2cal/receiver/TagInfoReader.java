package se.acrend.sj2cal.receiver;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import se.acrend.sj2cal.taginfo.TagInfoModel;
import se.acrend.sj2cal.taginfo.TagInfoParser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TagInfoReader extends BroadcastReceiver {

  private static final String TAG = "TagInfoReader";
  private static final String URL = "http://xn--tg-yia.info/%d.xml";

  @Override
  public void onReceive(final Context context, final Intent intent) {

    Log.d(TAG, "Före anrop, intent: " + intent);

    AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>() {

      @Override
      protected Void doInBackground(String... params) {

        try {
          final int trainNo = intent.getIntExtra("trainNo", -1);
          final String stationName = intent.getStringExtra("stationName");

          HttpClient client = AndroidHttpClient.newInstance("sj2cal");

          HttpGet get = new HttpGet(String.format(URL, trainNo));

          HttpResponse response = client.execute(get);
          TagInfoParser parser = new TagInfoParser();

          TagInfoModel model = parser.parse(response.getEntity().getContent());
          for (TagInfoModel.StationModel station : model.getStations()) {
            if (stationName.equals(station.getName())) {
              Log.d(TAG, "Hittade " + station.getName());
              if (station.getCalcArrival() != null) {
                Toast.makeText(context, "Sent tåg!", Toast.LENGTH_LONG).show();
              }
            }
          }
          context.sendBroadcast(new Intent("se.acrend.sj2cal.Delayed"));
          Log.d(TAG, "Efter anrop");
        } catch (IOException e) {
          Log.e(TAG, "Kunde inte läsa TågInfo.", e);
        }

        return null;
      }

    };
    task.execute(null);

  }
}
