package app.emcc_selfcontrol_android.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import app.emcc_selfcontrol_android.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.mitre.ascv.AndroidSegmentedControlView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends FragmentActivity {
    private CaldroidFragment caldroidFragment;
    private AndroidSegmentedControlView tabs;
    private ViewSwitcher mviewSwitcher;
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
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
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

        setCustomResourceForDates();
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();
        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();


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
                        "Long click " + formatter.format(date),
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
}
