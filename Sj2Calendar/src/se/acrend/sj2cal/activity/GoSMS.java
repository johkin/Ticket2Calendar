package se.acrend.sj2cal.activity;

import se.acrend.sj2cal.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GoSMS extends Activity {

  private Button linkButton;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.about);

    linkButton = (Button) findViewById(R.id.linkButton);
    linkButton.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri
            .parse("http://getsatisfaction.com/acrend/topics/gosms_biljetten_hamnar_inte_i_kalendern")));
      }
    });
  }

}
