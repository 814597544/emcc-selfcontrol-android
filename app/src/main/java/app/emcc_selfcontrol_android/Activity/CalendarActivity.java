package app.emcc_selfcontrol_android.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import app.emcc_selfcontrol_android.DataBase.DBAdapter;
import app.emcc_selfcontrol_android.R;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.mitre.ascv.AndroidSegmentedControlView;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends FragmentActivity implements View.OnClickListener{
    private CaldroidFragment caldroidFragment;
    private AndroidSegmentedControlView tabs;
    private ViewSwitcher mviewSwitcher;
    private LinearLayout title_return;
    private DBAdapter db;
    private RoundCornerProgressBar progressBar1,progressBar2,progressBar3;
    private void setCustomResourceForDates() {
        Calendar cal = Calendar.getInstance();

        // Min date is last 7 days
        cal.add(Calendar.DATE, -18);
        Date blueDate = cal.getTime();

        // Max date is next 7 days
        cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 16);
        Date greenDate = cal.getTime();

        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.blue,
                    blueDate);
            caldroidFragment.setBackgroundResourceForDate(R.color.green,
                    greenDate);
            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        db=new DBAdapter(CalendarActivity.this);
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        title_return=(LinearLayout) findViewById(R.id.title_return);
        progressBar1=(RoundCornerProgressBar) findViewById(R.id.progress1);
        progressBar2=(RoundCornerProgressBar) findViewById(R.id.progress2);
        progressBar3=(RoundCornerProgressBar) findViewById(R.id.progress3);

        title_return.setOnClickListener(this);
        tabs=(AndroidSegmentedControlView)findViewById(R.id.tabs);
        mviewSwitcher=(ViewSwitcher)findViewById(R.id.mviewSwitch);
        caldroidFragment = new CaldroidFragment();
        tabs.setOnSelectionChangedListener(new AndroidSegmentedControlView.OnSelectionChangedListener() {
            @Override
            public void newSelection(String identifier, String value) {

                if ("日 历".equals(value)){
                    mviewSwitcher.setDisplayedChild(0);
                }else{
                    mviewSwitcher.setDisplayedChild(1);
                }


            }
        });
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            caldroidFragment.setArguments(args);
        }

        showGoalDate(db);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();

                showGoalDetail(db,date);
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + date.getTime(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);
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

    /**
     * Save current states of the Caldroid here
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }

    private void parseTime(final String timeString){
        Calendar start = Calendar.getInstance();
        final int [] ret = new int[3];
        int index = 0;
        for(final String field : timeString.split("-")){
            ret[index] = Integer.parseInt(field);
            index++;
        }
        start.set(ret[0], ret[1], ret[2]);

        if (caldroidFragment != null) {
            caldroidFragment.setBackgroundResourceForDate(R.color.blue,
                    start.getTime());
            caldroidFragment.setTextColorForDate(R.color.white,  start.getTime());
        }
    }

    private void showGoalDate(DBAdapter db){

        db.open();
        Cursor cursor=db.getAllItem();
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            if ("0".equals(cursor.getString(cursor
                    .getColumnIndex("completed_goals")))) {

                parseTime(cursor.getString(cursor
                        .getColumnIndex("date")));

            }
            while (cursor.moveToNext()) {
                if ("0".equals(cursor.getString(cursor
                        .getColumnIndex("completed_goals")))) {

                    parseTime(cursor.getString(cursor
                            .getColumnIndex("date")));

                }
            }
        }
        cursor.close();
        db.close();
    }

    private void showGoalDetail(DBAdapter db,Date date){

        db.open();
        Cursor cursor=db.getItem(getStringDate(date.getTime()));

        if (cursor.getCount() > 0) {
            if (getStringDate(date.getTime()).equals(cursor.getString(cursor
                    .getColumnIndex("date")))) {

                    int deltaTime=(int)Double.parseDouble(cursor.getString(cursor
                                    .getColumnIndex("delta_time")));
                    int restTime=(int)Double.parseDouble(cursor.getString(cursor
                            .getColumnIndex("rest_time")));
                    int wasteTime=(int)Double.parseDouble(cursor.getString(cursor
                            .getColumnIndex("waste_time")));

                    showGoalDetail(deltaTime,restTime,wasteTime);

                }
            }
        cursor.close();
        db.close();
        }


    private void showGoalDetail(int deltaTime,int restTime,int wasteTime){




       /* progressBar1.setProgress(progressSize(deltaTime,24*3600));
        progressBar2.setProgress(progressSize(restTime,24*3600));
        progressBar3.setProgress(progressSize(wasteTime,24*3600));*/
        progressBar1.setProgress(5);
        progressBar2.setProgress(6);
        progressBar3.setProgress(9);
        mviewSwitcher.setDisplayedChild(1);

        Toast.makeText(CalendarActivity.this,wasteTime+"",Toast.LENGTH_SHORT).show();

    }
    public  String getStringDate(Long date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);

        return dateString;
    }
    private int progressSize(int cur,int total){

        NumberFormat numberFormat = NumberFormat.getInstance();

        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(2);
        String result = numberFormat.format(format2((double)cur/(double)total*100));
        return  Integer.parseInt(result);
    }
    public double format2(double f){
        BigDecimal b   =   new   BigDecimal(f);
        double   f1   =   b.setScale(0,   BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}
