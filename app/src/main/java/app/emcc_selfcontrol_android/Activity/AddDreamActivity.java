package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private EditText dream_name,need_time,mest_view;
    private Button cancel,ok;

    private static final int START_RILI=1;
    private static final int END_RILI=2;

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
        mest_view=(EditText) findViewById(R.id.mest_view);

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
                    Toast.makeText(getApplicationContext(),"先编辑开始时间",Toast.LENGTH_SHORT).show();
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
            {
                if (flag==START_RILI){
                    start_rili_value.setText(getStringDate(date));
                    startData=getStringDate(date);
                }else if (flag==END_RILI){
                    end_rili_value.setText(getStringDate(date));
                    endData=getStringDate(date);
                    calculate(startData,endData);
                }

            }
        });
        dialog.show();
    }
   /**
     * 将长时间格式字符串转换为时间 yyyy-MM-dd
     *
     */
    public static String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd ");
        String dateString = formatter.format(date);

        return dateString;
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
            int i=daysBetween(startData, endData);
            mgoal_view.setText(Double.parseDouble(need_time.getText().toString())/i+"");
        }catch (ParseException e){

        }
    }

    private void modifyDream(){
        if(StringUtils.isEmpty(dream_name.getText().toString())||StringUtils.isEmpty(need_time.getText().toString())
               ||StringUtils.isEmpty(mest_view.getText().toString())
                ||StringUtils.isEmpty(end_rili_value.getText().toString())||StringUtils.isEmpty(start_rili_value.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"请编辑所有信息",Toast.LENGTH_SHORT).show();
            return;
        }
        SharePrefrerncesUtil.put(this,"dream_name",dream_name.getText().toString());
        SharePrefrerncesUtil.put(this,"need_time",need_time.getText().toString());
        SharePrefrerncesUtil.put(this,"everyday_goal",mgoal_view.getText().toString());
        SharePrefrerncesUtil.put(this,"everyday_rest",mest_view.getText().toString());
        SharePrefrerncesUtil.put(this,"end_rili_Time",end_rili_value.getText().toString());
        SharePrefrerncesUtil.put(this,"start_rili_Time",start_rili_value.getText().toString());


    }

   private void calculate(int count){



   }
   private void setRegion(final EditText et){
       et.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start>0){
                    if (MIN_VALUE!=-1&&MAX_VALUE!=-1){
                        int num=Integer.parseInt(s.toString());
                        if (num>MAX_VALUE){
                            s=String.valueOf(MAX_VALUE);
                            et.setText(s);
                        }
                        else if (num<MIN_VALUE){
                            s=String.valueOf(MIN_VALUE);
                            return;
                        }

                    }

                }

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });


   }

}