package app.emcc_selfcontrol_android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import app.emcc_selfcontrol_android.Application.Messages;
import app.emcc_selfcontrol_android.Application.MyAPP;
import app.emcc_selfcontrol_android.R;

/**
 * Created by lenovo on 2015/3/23.
 */
public class RegisterActivity extends BaseActivity implements
        View.OnClickListener{
    private LinearLayout title_return;
    private EditText user_name,password,password2,user_email;
    String userName,passWord,passWord2,userEmail;
    private Button finish_r;
    MyAPP appContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        
        findView();
    }

    private void findView() {
        appContext= (MyAPP) getApplication();
        title_return=(LinearLayout) findViewById(R.id.title_return);
        title_return.setOnClickListener(this);

        user_name= (EditText) findViewById(R.id.user_name);
        password= (EditText) findViewById(R.id.password);
        password2= (EditText) findViewById(R.id.password2);
        user_email= (EditText) findViewById(R.id.user_email);
        finish_r= (Button) findViewById(R.id.finish_r);
        finish_r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName=user_name.getText().toString().trim();
                passWord=password.getText().toString().trim();
                passWord2=password2.getText().toString().trim();
                userEmail=user_email.getText().toString().trim();
                if ("".equals(userName)){
                    Toast.makeText(RegisterActivity.this, "用户名不可以为空！", Toast.LENGTH_SHORT).show();
                }else if (passWord.length()<6){
                    Toast.makeText(RegisterActivity.this, "密码不能少于6位！", Toast.LENGTH_SHORT).show();
                }else if (!passWord.equals(passWord2)){
                    Toast.makeText(RegisterActivity.this, "密码不一致！", Toast.LENGTH_SHORT).show();
                }else{
                    new Thread() {
                        public void run() {
                            Message msg = new Message();
                            try {
                                Messages m = appContext.register(userName, passWord,userEmail);
                                msg.obj = m;
                                msg.what = m != null ? 1 : -1;
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                // e.printStackTrace();
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

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            MyAPP ac = (MyAPP) getApplication();
            if (msg.what == 1) {

                Messages m = (Messages) msg.obj;
                if ("0".equals(m.getErron())) {
                    ac.cleanLoginInfo();// 清除登录信息
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(RegisterActivity.this, "恭喜您注册成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, m.getMsg(), Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == -1)  {
                ac.cleanLoginInfo();// 清除登录信息
                Toast.makeText(RegisterActivity.this, "网络出现异常", Toast.LENGTH_SHORT).show();
            }
            else if (msg.what == -2)  {
                Toast.makeText(RegisterActivity.this, (String)msg.obj, Toast.LENGTH_SHORT).show();
            }
            else if (msg.what == -3)  {
                Toast.makeText(RegisterActivity.this, "网络出现异常", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
