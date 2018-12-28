package top.ibase4j.model;

public class JsonResult {
	/**
	 * 请求结果，0为失败，1为成功
	 */
	private int result; 
	
	
	/**
	 * 请求结果信息
	 */
	private String message;
	
	
	/**
	 * 请求的返回数据对象，也将被转为json格式
	 */
	private Object data;
	
	
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
}
