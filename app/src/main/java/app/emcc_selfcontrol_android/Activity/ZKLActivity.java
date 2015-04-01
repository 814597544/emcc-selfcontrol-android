package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.emcc_selfcontrol_android.Adapter.CircularPagerAdapter;
import app.emcc_selfcontrol_android.DataBase.DBAdapter;
import app.emcc_selfcontrol_android.R;
import app.emcc_selfcontrol_android.Utils.DoubleClickExitHelper;
import app.emcc_selfcontrol_android.Utils.SharePrefrerncesUtil;
import app.emcc_selfcontrol_android.Utils.StringUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.OrangeGangsters.circularbarpager.library.CircularBarPager;
import com.nineoldandroids.animation.Animator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.Calendar;

/**
 * @author Adil Soomro
 *
 */
public class ZKLActivity extends BaseActivity implements View.OnClickListener{

    private MyReceiver myReceiver;
    private RoundCornerProgressBar progressTwo;
    private CircleImageView circleIcon;
    private CircularBarPager mCircularBarPager;
    private TextView titleName,dream_time,rest_time,waste_time;
    private ImageView addDream,start_dream,stop_dream;
    private DoubleClickExitHelper mDoubleClickExitHelper;
    private DBAdapter db;
    /**
     * The animation time in milliseconds that we take to display the steps taken
     */
    private static final int BAR_ANIMATION_TIME = 1000;
    private int progress2 = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zkl_layout);
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("zkl.add.dream");
        registerReceiver(myReceiver, filter);
        db=new DBAdapter(ZKLActivity.this);
        dream_time=(TextView) findViewById(R.id.dream_time);
        rest_time=(TextView) findViewById(R.id.rest_time);
        waste_time=(TextView) findViewById(R.id.waste_time);
        titleName=(TextView) findViewById(R.id.title);
        titleName.setText("自控力");
        Log.v("-------------------自控力","");
        circleIcon=(CircleImageView) findViewById(R.id.circleIcon);
        circleIcon.setOnClickListener(this);
        addDream=(ImageView) findViewById(R.id.add);
        addDream.setOnClickListener(this);
        start_dream=(ImageView) findViewById(R.id.start_dream);
        start_dream.setOnClickListener(this);
        stop_dream=(ImageView) findViewById(R.id.stop_dream);
        stop_dream.setOnClickListener(this);


        mDoubleClickExitHelper = new DoubleClickExitHelper(this);

        ImageLoader.getInstance().displayImage("https://coding.net/static/fruit_avatar/Fruit-1.png", circleIcon);
        progressTwo = (RoundCornerProgressBar) findViewById(R.id.progress_two);
        progressTwo.setBackgroundColor(getResources().getColor(R.color.custom_progress_background));

        updateProgressTwo();
        initViews();
        init();


    }

     private void init(){


         if(!"".equals(SharePrefrerncesUtil.get(ZKLActivity.this,"dream_name",""))){
             refresh();
         }else{
             addDream.setVisibility(View.VISIBLE);
         }

     }







    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.add :
                startActivity(new Intent(ZKLActivity.this,AddDreamActivity.class));
                break;
            case R.id.circleIcon :
                startActivity(new Intent(ZKLActivity.this,LoginActivity.class));
                break;
            case R.id.start_dream :
                start();
                break;
            case R.id.stop_dream :
                stop();
                break;
            default:

                break;

        }
    }

    private void updateProgressTwo() {
        progressTwo.setProgress(progress2);
        updateProgressTwoColor();
    }

    private void updateProgressTwoColor() {
        if(progress2 <= 3) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_red_progress));
        } else if(progress2 > 3 && progress2 <= 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_orange_progress));
        } else if(progress2 > 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_green_progress));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mCircularBarPager.getCircularBar().animateProgress(0, 75, 1000);
    }

    private void initViews(){
        mCircularBarPager = (CircularBarPager) findViewById(R.id.circularBarPager);

        View[] views = new View[2];
        views[0] = new CircularInnerViewActivity(this);
        views[1] = new CircularInnerViewActivity(this);

        mCircularBarPager.setViewPagerAdapter(new CircularPagerAdapter(this, views));

        ViewPager viewPager = mCircularBarPager.getViewPager();
        viewPager.setClipToPadding(true);

        CirclePageIndicator circlePageIndicator = mCircularBarPager.getCirclePageIndicator();
        circlePageIndicator.setFillColor(getResources().getColor(R.color.light_grey));
        circlePageIndicator.setPageColor(getResources().getColor(R.color.very_light_grey));
        circlePageIndicator.setStrokeColor(getResources().getColor(R.color.transparent));

        //Do stuff based on animation
        mCircularBarPager.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //TODO do stuff
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //Do stuff based on when pages change
        circlePageIndicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                if(mCircularBarPager!= null && mCircularBarPager.getCircularBar() != null){
                    switch (position){
                        case 0:
                            mCircularBarPager.getCircularBar().animateProgress(-25, 100, BAR_ANIMATION_TIME);
                            break;
                        case 1:
                            mCircularBarPager.getCircularBar().animateProgress(100, -75, BAR_ANIMATION_TIME);
                            break;
                        default:
                            mCircularBarPager.getCircularBar().animateProgress(0, 75, BAR_ANIMATION_TIME);
                            break;
                    }
                }
            }
        });
    }

    private void start(){
        start_dream.setVisibility(View.GONE);
        stop_dream.setVisibility(View.VISIBLE);
    }

    private void stop(){
        start_dream.setVisibility(View.VISIBLE);
        stop_dream.setVisibility(View.GONE);
    }
    private void refresh(){

        start_dream.setVisibility(View.VISIBLE);
        stop_dream.setVisibility(View.GONE);
        addDream.setVisibility(View.GONE);
        mCircularBarPager.setVisibility(View.VISIBLE);

        db.open();
        Cursor cursor=db.getAllItem();
        cursor.moveToFirst();
        String mcurDate=AddDreamActivity.getStringDate(System.currentTimeMillis());
        Calendar curDate= StringUtils.formatTime(mcurDate);

        if(cursor.getCount()>0&&curDate.equals(StringUtils.formatTime(cursor.getString(cursor
                .getColumnIndex("date"))))){
            dream_time.setText(cursor.getString(cursor
                    .getColumnIndex("goal_time"))+"小时");
            rest_time.setText(cursor.getString(cursor
                    .getColumnIndex("rest_time"))+"小时");
            waste_time.setText(cursor.getString(cursor
                    .getColumnIndex("waste_time"))+"小时");
        }
        else{
        while (cursor.moveToNext()) {
            if(curDate.equals(StringUtils.formatTime(cursor.getString(cursor
                    .getColumnIndex("date"))))){
                dream_time.setText(cursor.getString(cursor
                        .getColumnIndex("goal_time")) + "小时");
                rest_time.setText(cursor.getString(cursor
                        .getColumnIndex("rest_time")) + "小时");
                waste_time.setText(cursor.getString(cursor
                        .getColumnIndex("waste_time"))+"小时");
                break;
            }
        }
        }

        db.close();
    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {

            refresh();

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    /**
     * 监听返回--是否退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否退出应用
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        } else {
            flag = super.onKeyDown(keyCode, event);
        }
        return flag;
    }
}
