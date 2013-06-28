package se.acrend.sj2cal.activity;

import se.acrend.sj2cal.R;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity {

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.about);

    TextView header = (TextView) findViewById(R.id.aboutHeader);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      header.setVisibility(View.GONE);
    }

    Button close = (Button) findViewById(R.id.aboutClose);
    close.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(final View v) {
        About.this.finish();
      }
    });
  }
}
