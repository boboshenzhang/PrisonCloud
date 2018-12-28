package top.ibase4j.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("task_fire_log")
@SuppressWarnings("serial")
public class TaskFireLog implements Serializable {
    @TableId(value = "id_", type = IdType.AUTO)
    private Long id;
    private String groupName;
    private String taskName;
    private Date startTime;
    private Date endTime;
    @TableField("status_")
    private String status;
    private String serverHost;
    private String serverDuid;
    private String fireInfo;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the value of task_fire_log.group_name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * @param groupName the value for task_fire_log.group_name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    /**
     * @return the value of task_fire_log.task_name
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * @param taskName the value for task_fire_log.task_name
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    /**
     * @return the value of task_fire_log.start_time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the value for task_fire_log.start_time
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the value of task_fire_log.end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the value for task_fire_log.end_time
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the value of task_fire_log.status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the value for task_fire_log.status
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * @return the value of task_fire_log.server_host
     */
    public String getServerHost() {
        return serverHost;
    }

    /**
     * @param serverHost the value for task_fire_log.server_host
     */
    public void setServerHost(String serverHost) {
        this.serverHost = serverHost == null ? null : serverHost.trim();
    }

    /**
     * @return the value of task_fire_log.server_duid
     */
    public String getServerDuid() {
        return serverDuid;
    }

    /**
     * @param serverDuid the value for task_fire_log.server_duid
     */
    public void setServerDuid(String serverDuid) {
        this.serverDuid = serverDuid == null ? null : serverDuid.trim();
    }

    /**
     * @return the value of task_fire_log.fire_info
     */
    public String getFireInfo() {
        return fireInfo;
    }

    /**
     * @param fireInfo the value for task_fire_log.fire_info
     */
    public void setFireInfo(String fireInfo) {
        this.fireInfo = fireInfo == null ? null : fireInfo.trim();
    }
}