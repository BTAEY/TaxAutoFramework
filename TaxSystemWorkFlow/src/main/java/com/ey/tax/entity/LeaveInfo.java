package com.ey.tax.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by zhuji on 3/21/2018.
 */
@Entity
@Table(name = "t_leave_info")
public class LeaveInfo extends BaseEntity{

    @Column(name="status")
    private String status;

    @Column(name="leaveMsg")
    private String leaveMsg;

    @Column(name="taskId")
    private String taskId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLeaveMsg() {
        return leaveMsg;
    }

    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
