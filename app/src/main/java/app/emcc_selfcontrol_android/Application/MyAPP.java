package app.emcc_selfcontrol_android.Application;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONException;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import app.emcc_selfcontrol_android.Http.ApiClient;
import app.emcc_selfcontrol_android.R;

/**
 * Created by lenovo on 2015/3/19.
 */
public class MyAPP extends Application{
    public static float sScale;
    public static int sWidthDp;
    public static int sWidthPix;

    public static int sEmojiNormal;
    public static int sEmojiMonkey;

    private boolean login = false; 																	  // 是否登录
    private String loginkey = "";
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(this);
        sScale = getResources().getDisplayMetrics().density;
        sWidthPix = getResources().getDisplayMetrics().widthPixels;
        sWidthDp = (int) (sWidthPix / sScale);

        sEmojiNormal = getResources().getDimensionPixelSize(R.dimen.emoji_normal);
        sEmojiMonkey = getResources().getDimensionPixelSize(R.dimen.emoji_monkey);
    }
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .diskCacheFileCount(300)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .writeDebugLogs() // Remove for release app
                .diskCacheExtraOptions(sWidthPix / 3, sWidthPix / 3, null)
                .build();

        ImageLoader.getInstance().init(config);
    }




    /**
     * 用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * 获取登录key
     * @return
     */
    public String getkey() {
        return loginkey;
    }

    /**
     * 获取登录用户id
     *
     * @return
     */
    public String getLoginUid() {
        return this.loginkey;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        this.login = false;
        this.loginkey = "";
    }

    /*
     * 保存登录信息
     *
     * @param username
     * @param pwd
     */
    public void saveLoginInfo(final User user) {
        this.loginkey = user.getKey();
        this.login = true;
        setProperties(new Properties() {
            {

                setProperty("user.key", user.getKey());
                setProperty("user.Email", user.getEmail());
                setProperty("user.RealName", user.getRealName());// 用户头像-文件名
                setProperty("user.key", user.getUserName());

            }
        });
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginkey = "";
        this.login = false;
        removeProperty("user.key", "user.Email", "user.RealName", "user.Nickname", "user.gender", "user.Industry", "user.Income", "user.Mobile",
                "user.ProvideCode", "user.CityCode", "user.AeraCode", "user.DetailAddr", "user.Postcode");
    }


    /**
     * 设置Properties
     * @param ps
     */
    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    /**获取Properties
     * @return
     */
    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 判断网络是否连接
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }



    public User getUser(String userName, String passWord) throws IOException, JSONException, NoSuchAlgorithmException,Exception {
        User user = null;
        if (isNetworkConnected()) {
            return ApiClient.getUser(this, userName, passWord);
        }
        return user;
    }

    public Messages register(String uname, String upass,String email) {
        Messages msg = null;
        if (isNetworkConnected()) {
            try {
                return ApiClient.register(this, uname, upass,email);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msg;
    }
}
