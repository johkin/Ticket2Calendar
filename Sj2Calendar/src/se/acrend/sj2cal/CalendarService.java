package se.acrend.sj2cal;

import java.util.ArrayList;
import java.util.List;

import se.acrend.sj2cal.model.EventBase;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CalendarService extends Service {

  private List<EventBase> events;

  @Override
  public void onCreate() {
    super.onCreate();
    events = new ArrayList<EventBase>();

  }

  @Override
  public IBinder onBind(final Intent arg0) {
    // TODO Auto-generated method stub
    return null;
  }

}
