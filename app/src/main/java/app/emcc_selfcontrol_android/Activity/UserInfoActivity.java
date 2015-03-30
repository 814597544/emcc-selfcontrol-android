package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import app.emcc_selfcontrol_android.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lenovo on 2015/3/20.
 */
public class UserInfoActivity extends BaseActivity{
    TextView titleName;
    RelativeLayout dreaming;
    RelativeLayout dreamed;
    RelativeLayout setting;
    private LinearLayout title_return;
    private CircleImageView circleIcon;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        dreaming=(RelativeLayout)findViewById(R.id.dreaming);
        dreamed=(RelativeLayout)findViewById(R.id.dreamed);
        setting=(RelativeLayout)findViewById(R.id.setting);
        titleName=(TextView) findViewById(R.id.title);
        title_return=(LinearLayout) findViewById(R.id.title_return);
        titleName.setText("个人中心");
        circleIcon=(CircleImageView) findViewById(R.id.circleIcon);
        ImageLoader.getInstance().displayImage("https://coding.net/static/fruit_avatar/Fruit-1.png", circleIcon);
        dreaming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this,DreamingActivity.class));
            }
        });
        dreamed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this,DreamedActivity.class));
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserInfoActivity.this,SettingActivity.class));
            }
        });
        title_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
    }
}
