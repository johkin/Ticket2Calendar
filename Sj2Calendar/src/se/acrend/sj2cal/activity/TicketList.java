package se.acrend.sj2cal.activity;

import se.acrend.sj2cal.R;
import se.acrend.sj2cal.content.ProviderHelper;
import se.acrend.sj2cal.content.TicketAdapter;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TicketList extends ListActivity {

  @Override
  public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    TicketAdapter ticketAdapter = ProviderHelper.getTicketAdapter(this);

    setListAdapter(ticketAdapter);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.options_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
    case R.id.exit:
      this.finish();
      return true;
    case R.id.about:
      // showHelp();
      return true;
    case R.id.setting:
      startActivity(new Intent().setClass(this, Settings.class));
      return true;
    default:
      return super.onOptionsItemSelected(item);
    }
  }
}
