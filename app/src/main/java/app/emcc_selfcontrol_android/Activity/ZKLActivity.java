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

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Adil Soomro
 *
 */
public class ZKLActivity extends BaseActivity implements View.OnClickListener{
    private  String mcurDate;
    private  Calendar curDate,tempDate;
    private Cursor cursor;
    private MyReceiver myReceiver;
    private RoundCornerProgressBar progressTwo;
    private CircleImageView circleIcon;
    private CircularBarPager mCircularBarPager;
    private TextView titleName,dream_time,rest_time,waste_time;
    private ImageView addDream,start_dream,stop_dream,no_task;
    private DoubleClickExitHelper mDoubleClickExitHelper;
    private DBAdapter db;
    private  boolean hasTask= false;
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
        circleIcon=(CircleImageView) findViewById(R.id.circleIcon);
        circleIcon.setOnClickListener(this);
        addDream=(ImageView) findViewById(R.id.add);
        addDream.setOnClickListener(this);
        start_dream=(ImageView) findViewById(R.id.start_dream);
        start_dream.setOnClickListener(this);
        stop_dream=(ImageView) findViewById(R.id.stop_dream);
        stop_dream.setOnClickListener(this);
        no_task=(ImageView) findViewById(R.id.stop_dream);
        no_task.setOnClickListener(this);

        mDoubleClickExitHelper = new DoubleClickExitHelper(this);

        ImageLoader.getInstance().displayImage("https://coding.net/static/fruit_avatar/Fruit-1.png", circleIcon);
        progressTwo = (RoundCornerProgressBar) findViewById(R.id.progress_two);
        progressTwo.setBackgroundColor(getResources().getColor(R.color.main_color));

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
            case R.id.no_task :
                Toast.makeText(ZKLActivity.this,"今日暂无任务",Toast.LENGTH_SHORT).show();
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



        db.open();
         cursor=db.getAllItem();
        cursor.moveToFirst();
         mcurDate=getStringDate(System.currentTimeMillis());
        if (cursor.getCount() > 0) {
        if(mcurDate.equals(cursor.getString(cursor
                .getColumnIndex("date")))){
            hasTask=true;
            showView();
            dream_time.setText(cursor.getString(cursor
                    .getColumnIndex("goal_time"))+"小时");
            rest_time.setText(cursor.getString(cursor
                    .getColumnIndex("rest_time"))+"小时");
            waste_time.setText(cursor.getString(cursor
                    .getColumnIndex("waste_time"))+"小时");

            if("0".equals(cursor.getString(cursor
                    .getColumnIndex("goal_time")))){
                addDream.setVisibility(View.GONE);
                start_dream.setVisibility(View.GONE);
                stop_dream.setVisibility(View.GONE);
                addDream.setVisibility(View.GONE);
                no_task.setVisibility(View.VISIBLE);
                progressTwo.setProgress(10);

            }else{
                progressTwo.setProgress((int)(getTime(cursor,"delta_time")/getTime(cursor,"need_time")*10));
            }

            cursor.close();
            db.close();
        }
        else {

                while (cursor.moveToNext()) {

                    if(mcurDate.equals(cursor.getString(cursor
                            .getColumnIndex("date")))){
                        hasTask=true;
                        showView();
                        dream_time.setText(cursor.getString(cursor
                                .getColumnIndex("goal_time")) + "小时");
                        rest_time.setText(cursor.getString(cursor
                                .getColumnIndex("rest_time")) + "小时");
                        waste_time.setText(cursor.getString(cursor
                                .getColumnIndex("waste_time")) + "小时");
                        if ("0".equals(cursor.getString(cursor
                                .getColumnIndex("goal_time")))) {
                            addDream.setVisibility(View.GONE);
                            start_dream.setVisibility(View.GONE);
                            stop_dream.setVisibility(View.GONE);
                            addDream.setVisibility(View.GONE);
                            no_task.setVisibility(View.VISIBLE);
                            progressTwo.setProgress(10);

                        } else {
                            progressTwo.setProgress((int)(getTime(cursor,"delta_time")/getTime(cursor,"need_time")*10));
                        }
                        break;
                    }
                }
            cursor.close();
            db.close();
        }
        }

        if(!hasTask){
            addDream.setVisibility(View.GONE);
            no_task.setVisibility(View.VISIBLE);
            dream_time.setText("0小时");
            rest_time.setText("24小时");
            waste_time.setText("0小时");


        }

    }
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(final Context context, Intent intent) {

            refresh();

        }
    }
    public  String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        return dateString;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
private void showView(){
    start_dream.setVisibility(View.VISIBLE);
    stop_dream.setVisibility(View.GONE);
    addDream.setVisibility(View.GONE);
    no_task.setVisibility(View.GONE);
    mCircularBarPager.setVisibility(View.VISIBLE);
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
    private double getTime(Cursor cursor,String key){
        double time=Double.parseDouble(cursor.getString(cursor
                .getColumnIndex(key)));

            return time;
    }
}
