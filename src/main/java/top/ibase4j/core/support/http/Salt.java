package top.ibase4j.core.support.http;

import java.io.Serializable;

public class Salt implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	private String password;
	private String salt;
	
	
	
	public Salt(String password, String salt) {
		super();
		this.password = password;
		this.salt = salt;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	@Override
	public String toString() {
		return "Salt [password=" + password + ", salt=" + salt + "]";
	}
	
	
}
