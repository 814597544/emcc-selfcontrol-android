package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import app.emcc_selfcontrol_android.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lenovo on 2015/3/20.
 */
public class UserInfoActivity extends Activity{
    TextView titleName;
    private CircleImageView circleIcon;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        titleName=(TextView) findViewById(R.id.title);
        titleName.setText("个人中心");
        circleIcon=(CircleImageView) findViewById(R.id.circleIcon);
        ImageLoader.getInstance().displayImage("https://coding.net/static/fruit_avatar/Fruit-1.png", circleIcon);
    }
}
