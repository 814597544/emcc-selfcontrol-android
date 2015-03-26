package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import app.emcc_selfcontrol_android.R;

/**
 * @author Adil Soomro
 *
 */
public class AddDreamActivity extends Activity implements View.OnClickListener{
    private TextView titleName;
    private LinearLayout title_return;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dream_layout);
        titleName=(TextView) findViewById(R.id.title);
        titleName.setText("添加梦想");
        title_return=(LinearLayout) findViewById(R.id.title_return);
        title_return.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_return :
                finish();
                break;
            default:

                break;

        }
    }
}