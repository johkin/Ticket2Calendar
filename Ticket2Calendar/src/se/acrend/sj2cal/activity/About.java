package se.acrend.sj2cal.activity;

import com.google.analytics.tracking.android.EasyTracker;

import se.acrend.sj2cal.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class About extends Activity {

  @Override
  public void onStart() {
    super.onStart();
    EasyTracker.getInstance().activityStart(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    EasyTracker.getInstance().activityStop(this);
  }

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.about);
  }

  @Override
  public void onAttachedToWindow() {
    super.onAttachedToWindow();

    openOptionsMenu();
  }

  @Override
  public boolean onCreateOptionsMenu(final Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.close_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
    case R.id.exit:
      this.finish();
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }

}
