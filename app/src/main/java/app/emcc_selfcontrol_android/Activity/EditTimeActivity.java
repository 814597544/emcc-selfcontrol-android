package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import app.emcc_selfcontrol_android.R;

/**
 * Created by lenovo on 2015/3/2.
 */
public class EditTimeActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_time);
    }
}
