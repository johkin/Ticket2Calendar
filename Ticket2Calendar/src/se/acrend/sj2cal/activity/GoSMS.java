package se.acrend.sj2cal.activity;

import se.acrend.sj2cal.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GoSMS extends Activity {

  private Button linkButton;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.gosms);

    TextView header = (TextView) findViewById(R.id.goSmsHeader);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      header.setVisibility(View.GONE);
    }

    linkButton = (Button) findViewById(R.id.linkButton);
    linkButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(final View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/johkin/Ticket2Calendar/wiki/GoSMS")));
      }
    });
  }
}
