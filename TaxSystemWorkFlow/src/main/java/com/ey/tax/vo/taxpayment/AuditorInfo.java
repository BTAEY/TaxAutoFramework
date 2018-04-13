package com.ey.tax.vo.taxpayment;

import java.io.Serializable;

/**
 * Created by zhuji on 4/13/2018.
 */
public class AuditorInfo implements Serializable{
    private static final long serialVersionUID = 7321986913993269430L;
    private String station;

    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
