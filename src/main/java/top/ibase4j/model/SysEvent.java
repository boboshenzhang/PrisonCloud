package top.ibase4j.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import top.ibase4j.core.base.BaseModel;

@TableName("sys_event")
@SuppressWarnings("serial")
public class SysEvent extends BaseModel {
    @TableField("title_")
    private String title;

    private String requestUri;

    @TableField("parameters_")
    private String parameters;

    @TableField("method_")
    private String method;

    private String clientHost;
    private String userAgent;

    @TableField("status_")
    private Integer status;

    @TableField("user_name")
    private String userName;

    @TableField("user_phone")
    private String userPhone;

    /**
     * @return the value of sys_event.title_
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     *            the value for sys_event.title_
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * @return the value of sys_event.request_uri
     */
    public String getRequestUri() {
        return requestUri;
    }

    /**
     * @param requestUri
     *            the value for sys_event.request_uri
     */
    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri == null ? null : requestUri.trim();
    }

    /**
     * @return the value of sys_event.parameters_
     */
    public String getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     *            the value for sys_event.parameters_
     */
    public void setParameters(String parameters) {
        this.parameters = parameters == null ? null : parameters.trim();
    }

    /**
     * @return the value of sys_event.method_
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method
     *            the value for sys_event.method_
     */
    public void setMethod(String method) {
        this.method = method == null ? null : method.trim();
    }

    /**
     * @return the value of sys_event.client_host
     */
    public String getClientHost() {
        return clientHost;
    }

    /**
     * @param clientHost
     *            the value for sys_event.client_host
     */
    public void setClientHost(String clientHost) {
        this.clientHost = clientHost == null ? null : clientHost.trim();
    }

    /**
     * @return the value of sys_event.user_agent
     */
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * @param userAgent
     *            the value for sys_event.user_agent
     */
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent == null ? null : userAgent.trim();
    }

    /**
     * @return the value of sys_event.status_
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status
     *            the value for sys_event.status_
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}
