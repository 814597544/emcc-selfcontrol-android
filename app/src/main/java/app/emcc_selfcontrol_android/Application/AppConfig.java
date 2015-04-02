package app.emcc_selfcontrol_android.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

/**
 * 
 * @author 杨新武
* @date : 2015-1-19 上午9:53:49
* @description app配置类
 *
 */
@SuppressLint("NewApi")
public class AppConfig {

	private final static String APP_CONFIG = "config"; //配置文件名
	private Context mContext;  //上下文
	private static AppConfig appConfig; 

	/**
	 * 获取AppConfig实例 单例模式
	 * @param context
	 * @return
	 */
	public static AppConfig getAppConfig(Context context) {
		if (appConfig == null) {
			appConfig = new AppConfig();
			appConfig.mContext = context;
		}
		return appConfig;
	}

	/**
	 * 获取Preference设置
	 */
	public static SharedPreferences getSharedPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 根据key获取保存的配置
	 * @param key 
	 * @return
	 */
	public String get(String key) {
		Properties props = get();
		return (props != null) ? props.getProperty(key) : null;
	}

	/**
	 * 获取Properties
	 * @return
	 */
	public Properties get() {
		FileInputStream fis = null;
		Properties props = new Properties();
		try {
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			fis = new FileInputStream(dirConf.getPath() + File.separator
					+ APP_CONFIG);
			props.load(fis);
		} catch (Exception e) {
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return props;
	}

	
	
	/**
	 * 保存Properties文件
	 * @param p
	 */
	private void setProps(Properties p) {
		FileOutputStream fos = null;
		try {
			File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
			File conf = new File(dirConf, APP_CONFIG);
			fos = new FileOutputStream(conf);
			p.store(fos, null);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	
	public void set(Properties ps) {
		Properties props = get();
		props.putAll(ps);
		setProps(props);
	}

	public void set(String key, String value) {
		Properties props = get();
		props.setProperty(key, value);
		setProps(props);
	}

	public void remove(String... key) {
		Properties props = get();
		for (String k : key)
			props.remove(k);
		setProps(props);
	}
}
