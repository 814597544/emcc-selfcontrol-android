package app.emcc_selfcontrol_android.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.TextView;

import app.emcc_selfcontrol_android.Application.AppManager;
import app.emcc_selfcontrol_android.R;
import app.emcc_selfcontrol_android.Utils.DoubleClickExitHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableButtonMenu;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableMenuOverlay;


/**
 * @author Adil Soomro
 *
 */
@SuppressWarnings("deprecation")
public class HomeActivity extends TabActivity {
    private ExpandableMenuOverlay menuOverlay;
    private PopupWindow popupWindow;

	TabHost tabHost;
	/** Called when the activity is first created. */
	/*11111*/
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        AppManager.getAppManager().addActivity(this);

        menuOverlay = (ExpandableMenuOverlay) findViewById(R.id.button_menu);

		tabHost = getTabHost();
		setTabs();
        menuOverlay.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
            @Override
            public void onClick(ExpandableButtonMenu.MenuButton action) {
                switch (action) {
                    case MID:
                        startActivity3();
                        break;
                    case LEFT:
                        startActivity1();
                        break;
                    case RIGHT:
                        startActivity2();
                        break;
                }
            }
        });
	}
	private void setTabs()
	{
		addTab("自控力", R.drawable.zkl_unclick, ZKLActivity.class);
		addTab("精英圈", R.drawable.zone_unclick, ZoneActivity.class);
	}
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

private void initPopupWindows(View v){

    View pop_view=getLayoutInflater().inflate(R.layout.edit_time,null,false);
    popupWindow=new PopupWindow(pop_view,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,true );
    popupWindow.setFocusable(true);
    popupWindow.setOutsideTouchable(true);
    popupWindow.update();
    ColorDrawable dw = new ColorDrawable(0000000000);
    popupWindow.setBackgroundDrawable(dw);
    popupWindow.showAsDropDown(v);


}
private void startActivity1(){
    Intent intent=new Intent(HomeActivity.this,AddDreamActivity.class);
    startActivity(intent);

}
    private void startActivity2(){
        Intent intent=new Intent(HomeActivity.this,CalendarActivity.class);
        startActivity(intent);

    }
    private void startActivity3(){
        Intent intent=new Intent(HomeActivity.this,EditTimeActivity.class);
        startActivity(intent);

    }

}