package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import app.emcc_selfcontrol_android.R;
/**
 * Created by lenovo on 2015/3/4.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private Button bt_sns_login;
    private Button bt_sns_register;
    private LinearLayout title_return;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        bt_sns_login=(Button)findViewById(R.id.bt_sns_login);
        bt_sns_register=(Button)findViewById(R.id.bt_sns_register);
        title_return=(LinearLayout) findViewById(R.id.title_return);
        bt_sns_login.setOnClickListener(this);
        bt_sns_register.setOnClickListener(this);
        title_return.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_sns_login :
                startActivity(new Intent(LoginActivity.this,UserInfoActivity.class));
                break;
            case R.id.bt_sns_register :
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.title_return :
                finish();
                break;
        }
    }
}
