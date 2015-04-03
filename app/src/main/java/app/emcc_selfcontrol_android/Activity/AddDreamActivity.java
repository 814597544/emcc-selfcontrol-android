package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.emcc_selfcontrol_android.DataBase.DBAdapter;
import app.emcc_selfcontrol_android.R;
import app.emcc_selfcontrol_android.UI.DateTimePickerDialog;
import app.emcc_selfcontrol_android.Utils.SharePrefrerncesUtil;
import app.emcc_selfcontrol_android.Utils.StringUtils;

/**
 * @author Adil Soomro
 *
 */
public class AddDreamActivity extends BaseActivity implements View.OnClickListener{
    private TextView titleName,end_rili_value,start_rili_value,mgoal_view;
    private LinearLayout title_return;
    private ImageView startRili,endRili;
    private EditText dream_name,need_time,rest_time;
    private Button cancel,ok;
    private DBAdapter db;
    private static final int START_RILI=1;
    private static final int END_RILI=2;
    private int betweenDays;
    private int MIN_VALUE=1;
    private int MAX_VALUE=24;
    private int totalTime;
    private String startData,endData;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_dream_layout);
        initView();
    }

    private void initView(){
        db=new DBAdapter(AddDreamActivity.this);
        titleName=(TextView) findViewById(R.id.title);
        titleName.setText("添加梦想");
        end_rili_value=(TextView) findViewById(R.id.end_rili_value);
        start_rili_value=(TextView) findViewById(R.id.start_rili_value);
        title_return=(LinearLayout) findViewById(R.id.title_return);
        title_return.setOnClickListener(this);
        startRili=(ImageView) findViewById(R.id.start_rili);
        startRili.setOnClickListener(this);
        endRili=(ImageView) findViewById(R.id.end_rili);
        endRili.setOnClickListener(this);

        dream_name=(EditText) findViewById(R.id.deram_name);
        need_time=(EditText) findViewById(R.id.need_time);
        mgoal_view=(TextView) findViewById(R.id.mgoal_view);
        rest_time=(EditText) findViewById(R.id.mest_view);
        rest_time.setText("12");
        cancel=(Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        ok=(Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.title_return :
                finish();
                break;
            case R.id.start_rili :
                show(START_RILI);
                break;
            case R.id.end_rili :
                if(!StringUtils.isEmpty(start_rili_value.getText().toString())&&!StringUtils.isEmpty(need_time.getText().toString())) {
                    show(END_RILI);
                }else
                {
                    Toast.makeText(getApplicationContext(),"先编辑所需时间",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel :
                finish();
                break;
            case R.id.ok :
                modifyDream();
                break;
            default:

                break;

        }
    }

    public void show(final int flag )
    {
        DateTimePickerDialog dialog  = new DateTimePickerDialog(this, System.currentTimeMillis());
        dialog.setOnDateTimeSetListener(new DateTimePickerDialog.OnDateTimeSetListener()
        {
            public void OnDateTimeSet(AlertDialog dialog, long date)
            {   try {
                if (flag==START_RILI){

                            if (isBefore(getStringDate(System.currentTimeMillis()),getStringDate(date))) {
                                start_rili_value.setText(getStringDate(date));
                                startData = getStringDate(date);
                            }else {
                                Toast.makeText(getApplicationContext(),"请选择未来时间",Toast.LENGTH_SHORT).show();
                            }

                }else if (flag==END_RILI){

                    endData=getStringDate(date);
                    int j=Integer.parseInt(need_time.getText().toString())/24;
                    int i=Integer.parseInt(need_time.getText().toString())%24==0?j:j+1;
                    if(daysBetween(startData, endData)>=i){
                        end_rili_value.setText(getStringDate(date));
                        calculate(startData,endData);
                    }else {

                        Toast.makeText(getApplicationContext(),"时间范围不低于"+i+"天",Toast.LENGTH_SHORT).show();
                    }



                }
            }catch (Exception e){

            }

            }

        });
        dialog.show();
    }
   /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd
     *
     */
    public  String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        return dateString;
    }



    private boolean isBefore(String curTime,String selectTime) throws ParseException{

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

        Date bt=sdf.parse(curTime);

        Date et=sdf.parse(selectTime);

        if (bt.before(et)||bt.equals(et)){

        return true;

        }else{

            return false;

        }



    }


    /**
     *字符串的日期格式的计算
     */
    public static int daysBetween(String smdate,String bdate) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24)+1;

        return Integer.parseInt(String.valueOf(between_days));
    }

    private void calculate(String smdate,String bdate){
        try {
            betweenDays=daysBetween(startData, endData);
            double d=Double.parseDouble(need_time.getText().toString())/betweenDays;
            mgoal_view.setText(format(d)+"");
            if(24-format(d)>=12){
                rest_time.setText("12");
            }else {
                rest_time.setText(format(24 - format(d)) + "");
            }
        }catch (ParseException e){

        }
    }

    private void modifyDream(){
        String dreamName=dream_name.getText().toString();
        String goalTime=mgoal_view.getText().toString();
        String needTime=need_time.getText().toString();
        String restTime=rest_time.getText().toString();
        String endRili=end_rili_value.getText().toString();
        String startRili=start_rili_value.getText().toString();

        if(StringUtils.isEmpty(dreamName)||StringUtils.isEmpty(needTime)
               ||StringUtils.isEmpty(restTime)||StringUtils.isEmpty(goalTime)
                ||StringUtils.isEmpty(endRili)||StringUtils.isEmpty(startRili))
        {
            Toast.makeText(getApplicationContext(),"请编辑所有信息",Toast.LENGTH_SHORT).show();
            return;
        }
        if(Double.parseDouble(goalTime)+Double.parseDouble(restTime)>24){

            Toast.makeText(getApplicationContext(),"休息与梦想时间之和不可超过24小时",Toast.LENGTH_SHORT).show();
            return;
        }


        SharePrefrerncesUtil.put(this,"dream_name",dreamName);
        SharePrefrerncesUtil.put(this,"need_time",needTime);
        SharePrefrerncesUtil.put(this,"everyday_goal",goalTime);
        SharePrefrerncesUtil.put(this,"everyday_rest",restTime);
        SharePrefrerncesUtil.put(this,"end_rili_Time",endRili);
        SharePrefrerncesUtil.put(this, "start_rili_Time",startRili);

        initDataBase(dreamName,Double.parseDouble(goalTime)*3600+"",Double.parseDouble(restTime)*3600+"",Double.parseDouble(needTime)*3600+"",startRili,endRili);

        Intent intent = new Intent();
        intent.setAction("zkl.add.dream");
        sendBroadcast(intent);
        finish();
    }


    private void initDataBase(final String dreamName,final String goalTime,final String restTime,final String needTime,final String startTime, final String endTime){

        Log.v("--start_time="+startTime,"--------------------");
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int[] date = parseTime(startTime);
        start.set(date[0], date[1], date[2]);
        Log.v("s--date[0]="+date[0]+"-"+date[1]+"-"+date[2],"--------------------");
        date = parseTime(endTime);
        end.set(date[0], date[1], date[2]);
        db.open();
        Cursor cursor = null;
        cursor = db.getAllItem();
        Log.v("e--date[0]="+date[0]+"-"+date[1]+"-"+date[2],"--------------------");
        double xuduTime=24-Double.parseDouble(goalTime)-Double.parseDouble(restTime);
        String m ,d;
        while(start.before(end)){
            System.out.println(start.get(Calendar.YEAR)+"-"+start.get(Calendar.MONTH)+"-"+start.get(Calendar.DATE));
            if(start.get(Calendar.MONTH)>9){
                m=start.get(Calendar.MONTH)+"";
            }else{
                m="0"+start.get(Calendar.MONTH);
            }
            if(start.get(Calendar.DATE)>9){
                d=start.get(Calendar.DATE)+"";
            }else{
                d="0"+start.get(Calendar.DATE);
            }
            db.insertItem(dreamName, start.get(Calendar.YEAR)+"-"+m+"-"+d, "0", restTime, format(xuduTime)*3600+"",goalTime,needTime,"0");
            start.add(Calendar.DATE, 1);
        }
        cursor.close();
        db.close();
    }

    private int[] parseTime(final String timeString){
        final int [] ret = new int[3];
        int index = 0;
        for(final String field : timeString.split("-")){
            ret[index] = Integer.parseInt(field);
            index++;
        }
        return ret;
    }


    public double format(double f){
        BigDecimal b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}