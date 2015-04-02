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
    private String userCode;   //用户编码
    private String openid;     //微信用户id
    private String userName;   // 用户名
    private String nickName;   // 昵称
    private String realName;   // 真实姓名
    private String password;   // 密码
    private String sex;        // 性别（0保密1 男 2 女）
    private String city;       // 所在城市
    private String province;   //所在省份
    private String headimgurl;   //头像地址
    private String subscribeTime;   //用户编码
    private String email;   //邮箱
    private String idCardNO;   //身份证号
    private String birthday;   //生日
    private String qq;   //qq
    private String blog;   //博客或者
    private String handPhone;   //手机
    private String telephone;   //电话
    private String fax;   //传真
    private String departmentCode;   //单位编号
    private String zipcode;   //邮编
    private String homeAddress;   //家庭住址
    private String address;   //通信地址
    private String hobby;   //爱好
    private String occupation;   //职业
    private String education;   //学历
    private String incomeLevel;   //收入水平
    private String userState;   //激活状态
    // 错误代码
    private String erron;
    // 错误信息
    private String msg;
    // 用户唯一标识
    private String key;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(String subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdCardNO() {
        return idCardNO;
    }

    public void setIdCardNO(String idCardNO) {
        this.idCardNO = idCardNO;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getHandPhone() {
        return handPhone;
    }

    public void setHandPhone(String handPhone) {
        this.handPhone = handPhone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIncomeLevel() {
        return incomeLevel;
    }

    public void setIncomeLevel(String incomeLevel) {
        this.incomeLevel = incomeLevel;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
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
		user.setUserName(jsonObj.optString("userName"));
		return user;
	}

}
