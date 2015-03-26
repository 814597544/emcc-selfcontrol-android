package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import app.emcc_selfcontrol_android.R;

/**
 * @author Adil Soomro
 *
 */
public class AddDreamActivity extends Activity {
    private TextView titleName;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dream_layout);
        titleName=(TextView) findViewById(R.id.title);
        titleName.setText("添加梦想");
    }
    

}