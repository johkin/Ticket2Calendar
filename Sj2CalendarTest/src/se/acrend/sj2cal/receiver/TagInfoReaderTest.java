package se.acrend.sj2cal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.test.AndroidTestCase;

public class TagInfoReaderTest extends AndroidTestCase {

  public void testRead() throws Exception {
    Receiver r = new Receiver();
    getContext().registerReceiver(r,
        new IntentFilter("se.acrend.sj2cal.Delayed"));

    Intent intent = new Intent("se.acrend.sj2cal.READ_TAG_INFO");
    intent.putExtra("trainNo", 10546);
    intent.putExtra("stationName", "Stockholm C");
    getContext().sendBroadcast(intent);

    Thread.sleep(3000);

    assertNotNull(r.intent);
  }

  class Receiver extends BroadcastReceiver {

    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
      this.intent = intent;
    }

  }
}
