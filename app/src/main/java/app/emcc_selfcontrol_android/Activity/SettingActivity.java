package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import app.emcc_selfcontrol_android.R;

/**
 * Created by lenovo on 2015/3/25.
 */
public class SettingActivity extends BaseActivity implements
        View.OnClickListener{
private LinearLayout title_return;
@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
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
