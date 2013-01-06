package se.acrend.sj2cal.activity;

import com.google.analytics.tracking.android.EasyTracker;

import se.acrend.sj2cal.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class GoSMS extends Activity {

  private Button linkButton;
  
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

    setContentView(R.layout.gosms);

    linkButton = (Button) findViewById(R.id.linkButton);
    linkButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(final View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri
            .parse("https://github.com/johkin/Ticket2Calendar/wiki/GoSMS")));
      }
    });
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
    case R.id.about:
      startActivity(new Intent(Intent.ACTION_VIEW,
          Uri.parse("https://github.com/johkin/Ticket2Calendar/wiki/GoSMS")));
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }
}
