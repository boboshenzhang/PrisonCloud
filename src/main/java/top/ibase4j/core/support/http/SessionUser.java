package top.ibase4j.core.support.http;

import java.io.Serializable;

/**
 * 用户会话信息
 * @author ShenHuaJie
 * @since 2018年7月22日 上午9:34:50
 */
@SuppressWarnings("serial")
public class SessionUser implements Serializable {
    private Long id;
    private String userName;
    private String userPhone;
    private Long orgId;
    private String orgName;
    private Long parentDeptId;
    private Long deptId;
    private String deptName;
    private Boolean isArea;
    

    public SessionUser(Long id, String userName, String userPhone) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
    }
    public SessionUser(Long id, String userName, String userPhone,Long orgId,String orgName,Long parentDeptId,Long deptId,String deptName,Boolean isArea) {
        this.id = id;
        this.userName = userName;
        this.userPhone = userPhone;
        this.deptId=deptId;
        this.orgId=orgId;
        this.orgName=orgName;
        this.deptName=deptName;
        this.parentDeptId=parentDeptId;
        this.isArea=isArea;
    }
    public boolean isManager(){
    	
    	return this.orgId == 1;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public Long getParentDeptId() {
		return parentDeptId;
	}
	public void setParentDeptId(Long parentDeptId) {
		this.parentDeptId = parentDeptId;
	}
	public Boolean getIsArea() {
		return isArea;
	}
	public void setIsArea(Boolean isArea) {
		this.isArea = isArea;
	}
    
}
