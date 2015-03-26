package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;

import app.emcc_selfcontrol_android.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lenovo on 2015/3/24.
 */
public class DreamedActivity extends Activity{
    private CircleImageView circleIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dreamed);
        circleIcon=(CircleImageView) findViewById(R.id.circleIcon);
        ImageLoader.getInstance().displayImage("https://coding.net/static/fruit_avatar/Fruit-1.png", circleIcon);
    }
}
