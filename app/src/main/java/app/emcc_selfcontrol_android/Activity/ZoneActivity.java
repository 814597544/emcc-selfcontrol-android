package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import app.emcc_selfcontrol_android.R;

/**
 * @author Adil Soomro
 *
 */
public class ZoneActivity extends Activity {
	TextView titleName;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zone_actvity);
        titleName=(TextView) findViewById(R.id.title);
        titleName.setText("精英圈");
    }

}
