package top.ibase4j.core.support.login;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordLoginTypeToken extends UsernamePasswordToken{
	
	private static final long serialVersionUID = 8746902097331546419L;
	private int loginType = 0;// 0为用户密码登录，1为手机验证码登录
    private String username;
    private String pwd; 
    private String clientIp;
    
	 
	public UsernamePasswordLoginTypeToken(int loginType,  String username, String pwd,String clientIp) {
	
		this.loginType = loginType;
		this.username = username;
		this.pwd = pwd;
		this.clientIp=clientIp;
	}
	
	public int getLoginType() {
		return loginType;
	}
	
	public void setLoginType(int loginType) {
		this.loginType = loginType;
	} 
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}
 
    
}
