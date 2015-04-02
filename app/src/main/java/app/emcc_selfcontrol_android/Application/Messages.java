package app.emcc_selfcontrol_android.Application;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Messages {

	/**
	 * 0 默认 什么都没有 1 注册 2订单
	 */
	private int type = 0;
	// 错误代码
	private String erron;
	// 错误信息
	private String msg;
	// 用户唯一标识
	private String key;

	/**
	 * 
	 */
	public Messages() {
	}

	public Messages(int bl) {
		this.type = bl;
	}

	public String getErron() {
		return erron;
	}

	public void setErron(String erron) {
		this.erron = erron;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		if (type == 0) {
			this.msg = msg;
		} else if (type == 1) {

			if (erron.equals("0")) {
				this.msg = "注册成功";
			} else if (erron.equals("1")) {
				this.msg = "非法请求";
			} else if (erron.equals("2")) {
				this.msg = "用户名不能为空";
			} else if (erron.equals("3")) {
				this.msg = "密码不能为空";
			} else if (erron.equals("4")) {
				this.msg = "邮箱不能为空";
			} else if (erron.equals("5")) {
				this.msg = "用户名不合法";
			} else if (erron.equals("6")) {
				this.msg = "密码不合法";
			} else if (erron.equals("7")) {
                this.msg = "邮箱输入不合法";
            } else if (erron.equals("8")) {
                this.msg = "该用户名已被注册";
            } else if (erron.equals("9")) {
                this.msg = "该邮箱已被注册";
            } else if (erron.equals("10")) {
                this.msg = "系统异常请稍候再试";
            } else {
				this.msg = msg;
			}
		} else {
			this.msg = msg;
		}

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static Messages parse(String str) throws JSONException {
		Log.v("----getMsg-------", str);
		Messages msg = new Messages();
		JSONObject jsonObj;
		jsonObj = new JSONObject(str);
		String code = jsonObj.optString("errorno");

		msg.setErron(code);
		msg.setMsg(jsonObj.optString("message"));

		return msg;
	}
}
