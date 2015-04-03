package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.emcc_selfcontrol_android.Adapter.CircularPagerAdapter;
import app.emcc_selfcontrol_android.Application.MyAPP;
import app.emcc_selfcontrol_android.DataBase.DBAdapter;
import app.emcc_selfcontrol_android.Interface.UpdateState;
import app.emcc_selfcontrol_android.R;
import app.emcc_selfcontrol_android.UI.MyBindService;
import app.emcc_selfcontrol_android.Utils.DoubleClickExitHelper;
import app.emcc_selfcontrol_android.Utils.SharePrefrerncesUtil;
import app.emcc_selfcontrol_android.Utils.StringUtils;
import de.hdodenhof.circleimageview.CircleImageView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.github.OrangeGangsters.circularbarpager.library.CircularBarPager;
import com.nineoldandroids.animation.Animator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.CirclePageIndicator;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class ZKLActivity extends BaseActivity implements View.OnClickListener{
    private  String mcurDate;
    private  Calendar curDate,tempDate;
    private Cursor cursor;
    private MyReceiver myReceiver;
    private RoundCornerProgressBar progressTwo;
    private CircleImageView circleIcon;
    private CircularBarPager mCircularBarPager;
    private TextView titleName,dream_time,rest_time,waste_time,show_finishtime;
    private ImageView addDream,start_dream,stop_dream,no_task;
    private DoubleClickExitHelper mDoubleClickExitHelper;
    private DBAdapter db;
    private  boolean hasTask= false;
    private MyAPP myAPP;
    private CircularInnerViewActivity mCircularInnerViewActivity;
    private UpdateState mCallBack;
    private TimerTask task = null;
    private Timer time = null	;
    boolean test=false;
    private int deltaTime,goalTime,needTime;
    private Boolean isBinder=false;
    private int totalDeltaTime=0;
    private ImageView img1,img2,img3;

    /**
     * The animation time in milliseconds that we take to display the steps taken
     */
    private static final int BAR_ANIMATION_TIME = 1000;
    MyBindService.MyBinder binder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder=null;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (MyBindService.MyBinder) service;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zkl_layout);
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("zkl.add.dream");
        registerReceiver(myReceiver, filter);

        db=new DBAdapter(ZKLActivity.this);
        myAPP=(MyAPP)getApplication();
        dream_time=(TextView) findViewById(R.id.dream_time);
        rest_time=(TextView) findViewById(R.id.rest_time);
        waste_time=(TextView) findViewById(R.id.waste_time);
        titleName=(TextView) findViewById(R.id.title);
        titleName.setText("自控力");
        show_finishtime=(TextView) findViewById(R.id.show_finishtime);

        circleIcon=(CircleImageView) findViewById(R.id.circleIcon);
        circleIcon.setOnClickListener(this);
        addDream=(ImageView) findViewById(R.id.add);
        addDream.setOnClickListener(this);
        start_dream=(ImageView) findViewById(R.id.start_dream);
        start_dream.setOnClickListener(this);
        stop_dream=(ImageView) findViewById(R.id.stop_dream);
        stop_dream.setOnClickListener(this);
        no_task=(ImageView) findViewById(R.id.no_task);
        no_task.setOnClickListener(this);
        img1=(ImageView) findViewById(R.id.img1);
        img2=(ImageView) findViewById(R.id.img2);
        img3=(ImageView) findViewById(R.id.img3);
        mDoubleClickExitHelper = new DoubleClickExitHelper(this);

        ImageLoader.getInstance().displayImage("https://coding.net/static/fruit_avatar/Fruit-1.png", circleIcon);
        progressTwo = (RoundCornerProgressBar) findViewById(R.id.progress_two);
        progressTwo.setBackgroundColor(getResources().getColor(R.color.main_color));
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
                if(myAPP.isLogin()){
                    startActivity(new Intent(ZKLActivity.this,UserInfoActivity.class));
                }
                else{
                    startActivity(new Intent(ZKLActivity.this,LoginActivity.class));
                }
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

    /*private void updateProgressTwo() {
        updateProgressTwoColor();
    }*/
 /*   private void updateProgressTwoColor() {
        if(progress2 <= 3) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_red_progress));
        } else if(progress2 > 3 && progress2 <= 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_orange_progress));
        } else if(progress2 > 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_green_progress));
        }
    }*/

    private void initViews(){
        mCircularBarPager = (CircularBarPager) findViewById(R.id.circularBarPager);
        View[] views = new View[1];
        mCircularInnerViewActivity = new CircularInnerViewActivity(this);
        views[0]=mCircularInnerViewActivity;
        mCallBack=(UpdateState)mCircularInnerViewActivity;
        mCircularBarPager.setViewPagerAdapter(new CircularPagerAdapter(this, views));
        ViewPager viewPager = mCircularBarPager.getViewPager();
        viewPager.setClipToPadding(true);
        CirclePageIndicator circlePageIndicator = mCircularBarPager.getCirclePageIndicator();
        circlePageIndicator.setFillColor(getResources().getColor(R.color.light_grey));
        circlePageIndicator.setPageColor(getResources().getColor(R.color.very_light_grey));
        circlePageIndicator.setStrokeColor(getResources().getColor(R.color.transparent));

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
       /* refresh();*/
        mCallBack.updateDreamToole("暂停");
        start_dream.setVisibility(View.GONE);
        stop_dream.setVisibility(View.VISIBLE);
        final Intent intent = new Intent();
        intent.setAction("com.emcc.zkl.furao");
        getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
        isBinder=true;
        todayFinish(totalDeltaTime);
        startTimer();
    }

    private void stop(){
        db.open();
        totalDeltaTime=totalDeltaTime+binder.getCount();
        boolean b=  db.updateToday(SharePrefrerncesUtil.get(this,"dream_name","")+"",getStringDate(System.currentTimeMillis()),totalDeltaTime+"");
        if(b){
            Toast.makeText(ZKLActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ZKLActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
        }
        db.close();
        mCallBack.updateDreamToole("开始");
        start_dream.setVisibility(View.VISIBLE);
        stop_dream.setVisibility(View.GONE);
        stopTimer();
        if(isBinder) {
            getApplicationContext().unbindService(connection);
            isBinder=false;
        }

    }
    private void startTimer() {
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    test=true;
                    Intent intent2 = new Intent();
                    intent2.setAction("zkl.add.dream");
                    sendBroadcast(intent2);
                }
            };
        }

        if (time == null) {
            time = new Timer();
        }
        time.schedule(task, 1000, 1000);
    }

    private void stopTimer() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (time != null) {
            time.cancel();
            time = null;
        }

    }

    private void refresh(){
        db.open();
        cursor=db.getAllItem();
        cursor.moveToFirst();
        mcurDate=getStringDate(System.currentTimeMillis());
        if (cursor.getCount() > 0) {
            if(mcurDate.equals(cursor.getString(cursor
                    .getColumnIndex("date")))&&SharePrefrerncesUtil.get(this,"dream_name","").equals(cursor.getString(cursor
                    .getColumnIndex("dream_name")))){
                hasTask=true;
                showView();
                dream_time.setText(SharePrefrerncesUtil.get(this,"everyday_goal","") + "小时");
                rest_time.setText(SharePrefrerncesUtil.get(this, "everyday_rest", "")+"小时");
                waste_time.setText(24-Double.parseDouble(SharePrefrerncesUtil.get(this, "everyday_rest", "")+"")-Double.parseDouble(SharePrefrerncesUtil.get(this, "everyday_goal", "")+"")+"小时");
                needTime=(int)Double.parseDouble(cursor.getString(cursor
                        .getColumnIndex("need_time")));
                goalTime=(int)Double.parseDouble(cursor.getString(cursor
                        .getColumnIndex("goal_time")));
                goalTime=60;
                deltaTime=(int)Double.parseDouble(cursor.getString(cursor
                        .getColumnIndex("delta_time")));
                totalDeltaTime=deltaTime;
                todayFinish(deltaTime);
                if("1".equals(cursor.getString(cursor
                        .getColumnIndex("completed_goals")))){
                    addDream.setVisibility(View.GONE);
                    start_dream.setVisibility(View.GONE);
                    stop_dream.setVisibility(View.GONE);
                    addDream.setVisibility(View.GONE);
                    no_task.setVisibility(View.VISIBLE);
                    progressTwo.setProgress(10);
                    img1.setVisibility(View.GONE);
                    img2.setVisibility(View.GONE);
                    img3.setVisibility(View.VISIBLE);
                }else{
                    progressTwo.setProgress((int)((totalDeltaTime)*1.0/60*10));
                }


            }
            else {

                while (cursor.moveToNext()) {

                    if(mcurDate.equals(cursor.getString(cursor
                            .getColumnIndex("date")))&&SharePrefrerncesUtil.get(this,"dream_name","").equals(cursor.getString(cursor
                            .getColumnIndex("dream_name")))){
                        hasTask=true;

                        showView();
                        dream_time.setText(SharePrefrerncesUtil.get(this, "everyday_goal", "") + "小时");
                        rest_time.setText(SharePrefrerncesUtil.get(this, "everyday_rest", "")+"小时");
                        waste_time.setText(SharePrefrerncesUtil.get(this, "everyday_rest", "")+"小时");
                        needTime=(int)Double.parseDouble(cursor.getString(cursor
                                .getColumnIndex("need_time")));
                        goalTime=(int)Double.parseDouble(cursor.getString(cursor
                                .getColumnIndex("goal_time")));
                        goalTime=60;
                        deltaTime=(int)Double.parseDouble(cursor.getString(cursor
                                .getColumnIndex("delta_time")));
                        totalDeltaTime=deltaTime;
                        todayFinish(deltaTime);
                        if ("1".equals(cursor.getString(cursor
                                .getColumnIndex("completed_goals")))) {
                            addDream.setVisibility(View.GONE);
                            start_dream.setVisibility(View.GONE);
                            stop_dream.setVisibility(View.GONE);
                            addDream.setVisibility(View.GONE);
                            no_task.setVisibility(View.VISIBLE);
                            img1.setVisibility(View.GONE);
                            img2.setVisibility(View.GONE);
                            img3.setVisibility(View.VISIBLE);
                            progressTwo.setProgress(10);

                        } else {
                            progressTwo.setProgress((int)((totalDeltaTime)*1.0/60*10));
                        }
                        break;
                    }
                }

            }
            if (hasTask) {
                cursor = db.getItem(SharePrefrerncesUtil.get(this, "dream_name", "") + "");
                int x = (int) Double.parseDouble(cursor.getString(cursor
                        .getColumnIndex("delta_time")));
                while (cursor.moveToNext()) {
                    x += (int) Double.parseDouble(cursor.getString(cursor
                            .getColumnIndex("delta_time")));
                }
                x = (int) (x * 1.0) / 60*100;

                mCircularBarPager.getCircularBar().animateProgress(20, 100, 1000);
            }

            cursor.close();
            db.close();
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

            if (test==true) {
                todayFinish(binder.getCount() + totalDeltaTime);
                progressTwo.setProgress((int)((binder.getCount()+totalDeltaTime)*1.0/60*10));
                if ((binder.getCount() + totalDeltaTime)>=(goalTime/2)){
                    img1.setVisibility(View.GONE);
                    img2.setVisibility(View.VISIBLE);
                    img3.setVisibility(View.GONE);
                }
                if ((binder.getCount() + totalDeltaTime)>=goalTime){
                    stopTimer();
                    if(isBinder) {
                        db.open();
                        db.completedGoal(SharePrefrerncesUtil.get(ZKLActivity.this,"dream_name","")+"",getStringDate(System.currentTimeMillis()),"1");
                        db.updateToday(SharePrefrerncesUtil.get(ZKLActivity.this,"dream_name","")+"",getStringDate(System.currentTimeMillis()),(binder.getCount() + totalDeltaTime-1)+"");
                        db.close();
                        getApplicationContext().unbindService(connection);
                        isBinder=false;
                        start_dream.setVisibility(View.GONE);
                        stop_dream.setVisibility(View.GONE);
                        no_task.setVisibility(View.VISIBLE);
                        img1.setVisibility(View.GONE);
                        img2.setVisibility(View.GONE);
                        img3.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                refresh();
            }

        }
    }
    public  String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        return dateString;
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
        stopTimer();
        if(isBinder) {
            db.open();
            totalDeltaTime=totalDeltaTime+binder.getCount();
            boolean b=  db.updateToday(SharePrefrerncesUtil.get(this,"dream_name","")+"",getStringDate(System.currentTimeMillis()),totalDeltaTime-1+"");
            if(b){
                Toast.makeText(ZKLActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ZKLActivity.this,"更新失败",Toast.LENGTH_SHORT).show();
            }
            db.close();
            getApplicationContext().unbindService(connection);
            isBinder=false;
        }
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
    public double format(double f){
        BigDecimal b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
    private void todayFinish(int time){
        int H=time/3600;
        int M=(time%3600)/60;
        int S=(time%3600)%60;
        String HH,MM, SS;
        if (H<10){HH="0"+H;}else{HH=""+H;}
        if (M<10){MM="0"+M;}else{MM=""+M;}
        if (S<10){SS="0"+S;}else{SS=""+S;}
        show_finishtime.setText(HH+":"+MM+":"+SS);
    }

}