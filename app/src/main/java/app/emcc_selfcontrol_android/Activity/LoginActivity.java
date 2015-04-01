package app.emcc_selfcontrol_android.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.emcc_selfcontrol_android.Application.MyAPP;
import app.emcc_selfcontrol_android.Application.User;
import app.emcc_selfcontrol_android.R;
/**
 * Created by lenovo on 2015/3/4.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private Button bt_sns_login;
    private Button bt_sns_register;
    private LinearLayout title_return;
    // 忘记密码
    private TextView forgetPass;
    // 登录用户名
    private EditText userName;
    // 登录密码
    private EditText passWord;

    // 记住用户名
   // private  remember_user;
    private MyAPP appContext;
    // 用户唯一标识key
    private User user;
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

        findView();
        rigester();
        login();
    }

    private void login() {
        bt_sns_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 验证用户
                final String uname = userName.getText().toString().trim();
                final String upass = passWord.getText().toString().trim();
                if ("".equals(uname)) {
                    Toast.makeText(LoginActivity.this, "用户名不可以为空！", Toast.LENGTH_SHORT).show();
                    userName.requestFocus();
                } else if ("".equals(upass)) {
                    Toast.makeText(LoginActivity.this, "密码不可以为空！",Toast.LENGTH_SHORT).show();
                    passWord.requestFocus();
                }else{
                    new Thread() {
                        public void run() {
                            Message msg = new Message();
                            try {
                                user = appContext.getUser(uname, upass);
                                msg.obj = user;
                                msg.what= user!=null?1:-1;
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                //e.printStackTrace();
                                msg.obj = e;
                                msg.what = -1;
                            }
                            handler.sendMessage(msg);
                        }
                    }.start();
                }
            }
        });
    }

    private void rigester() {
    }

    private void findView() {
        appContext = (MyAPP) getApplication();
        user = new User();
        userName= (EditText) findViewById(R.id.edit_login_name);
        passWord= (EditText) findViewById(R.id.edit_login_pw);
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            MyAPP ac = (MyAPP)getApplication();

            if (msg.what == 1) {
                user = (User)msg.obj;
                //记住用户名
                if("0".equals(user.getErron())){

                    ac.saveLoginInfo(user);
                    Intent intent = new Intent();
                    intent.setAction("com.emcc.zkl.login");
                    sendBroadcast(intent);

                   // finish();
                }
                Toast.makeText(LoginActivity.this, user.getMsg(), Toast.LENGTH_SHORT).show();
            } else {

                ac.cleanLoginInfo();//清除登录信息
                Toast.makeText(LoginActivity.this, "网络出现异常", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
