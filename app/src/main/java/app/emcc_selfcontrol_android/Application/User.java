package app.emcc_selfcontrol_android.Application;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录
 * 
 * @author fan
 * 
 */
public class User implements Serializable {

	private static final long serialVersionUID = 5880128217414516285L;
	// 错误代码
	private String erron;
	// 错误信息
	private String msg;
	// 用户唯一标识
	private String key;
	// 邮箱
	private String email;
	// 真实姓名
	private String realName;
	// 昵称
	private String nickname;
	// 性别
	private String gender;
	// 职业
	private String industry;
	// 收入
	private String income;
	// 手机号
	private String mobile;
	// 省编号
	private String provideCode;
	// 市
	private String cityCode;
	// 区
	private String aeraCode;
	// 详细地址
	private String detailAddr;
	// 邮编
	private String postcode;
	// 用户头像
	private String head_img;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getProvideCode() {
		return provideCode;
	}

	public void setProvideCode(String provideCode) {
		this.provideCode = provideCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAeraCode() {
		return aeraCode;
	}

	public void setAeraCode(String aeraCode) {
		this.aeraCode = aeraCode;
	}

	public String getDetailAddr() {
		return detailAddr;
	}

	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		if (erron.equals("0")) {
			this.msg = "登录成功";
		} else if (erron.equals("1")) {
			this.msg = "非法请求";
		} else if (erron.equals("2")) {
			this.msg = "用戶名不能为空";
		} else if (erron.equals("3")) {
			this.msg = "密码不能为空";
		}else if (erron.equals("4")) {
            this.msg = "用户名或者密码错误";
        } else {

			this.msg = msg;
		}
	}

	public String getErron() {
		return erron;
	}

	public void setErron(String erron) {
		this.erron = erron;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public static User parse(String str) throws JSONException {
		User user = null;
		JSONObject jsonObj;
		jsonObj = new JSONObject(str);
		String retCode = jsonObj.optString("errorno");
		user = new User();
		user.setErron(retCode);
		user.setMsg(jsonObj.optString("message"));
		if (!retCode.equals("0")) {
			return user;
		}
		user.setKey(jsonObj.optString("key"));
		user.setEmail(jsonObj.optString("email"));
		user.setRealName(jsonObj.optString("RealName"));
		user.setNickname(jsonObj.optString("Nickname"));
		user.setGender(jsonObj.optString("gender"));
		user.setIndustry(jsonObj.optString("Industry"));
		user.setIncome(jsonObj.optString("Income"));
		user.setMobile(jsonObj.optString("Mobile"));
		user.setProvideCode(jsonObj.optString("ProvideCode"));
		user.setCityCode(jsonObj.optString("CityCode"));
		user.setAeraCode(jsonObj.optString("AeraCode"));
		user.setDetailAddr(jsonObj.optString("DetailAddr"));
		user.setPostcode(jsonObj.optString("Postcode"));
		user.setHead_img(jsonObj.optString("HEAD_IMG"));
		return user;
	}

}
