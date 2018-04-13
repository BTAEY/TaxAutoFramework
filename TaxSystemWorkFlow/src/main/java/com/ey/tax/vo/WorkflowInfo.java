package com.ey.tax.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhuji on 4/10/2018.
 */
public class WorkflowInfo implements Serializable{

    private String defineId;

    private String deploymentId;

    private String name;

    private String key;

    private Date deployDate;

    private String description;

    private String resourceName;

    private String resourcePng;

    private Long duration;

    private Date startTime;

    private Date endTime;

    public WorkflowInfo(){}

    public WorkflowInfo(Builder builder){
        this.defineId = builder.defineId;
        this.deploymentId = builder.deploymentId;
        this.name = builder.name;
        this.key = builder.key;
        this.deployDate = builder.deployDate;
        this.description = builder.description;
        this.resourceName = builder.resourceName;
        this.resourcePng = builder.resourcePng;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.duration = builder.duration;
    }

    public static Builder createBuilder(){
        return new Builder();
    }

    public String getDefineId() {
        return defineId;
    }

    public void setDefineId(String defineId) {
        this.defineId = defineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Date getDeployDate() {
        return deployDate;
    }

    public void setDeployDate(Date deployDate) {
        this.deployDate = deployDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourcePng() {
        return resourcePng;
    }

    public void setResourcePng(String resourcePng) {
        this.resourcePng = resourcePng;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public static class Builder{
        private String defineId;

        private String deploymentId;

        private String name;

        private String key;

        private Date deployDate;

        private String description;

        private String resourceName;

        private String resourcePng;

        private Long duration;

        private Date startTime;

        private Date endTime;

        public Builder defineId(String defineId){
            this.defineId = defineId;
            return this;
        }

        public Builder deploymentId(String deploymentId){
            this.deploymentId = deploymentId;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder key(String key){
            this.key = key;
            return this;
        }

        public Builder deployDate(Date deployDate){
            this.deployDate = deployDate;
            return this;
        }

        public Builder resourceName(String resourceName){
            this.resourceName = resourceName;
            return this;
        }

        public Builder resourcePng(String resourcePng){
            this.resourcePng = resourcePng;
            return this;
        }

        public Builder startTime(Date startTime){
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(Date endTime){
            this.endTime = endTime;
            return this;
        }

        public Builder duration(Long duration){
            this.duration = duration;
            return this;
        }


        public WorkflowInfo build(){
            return new WorkflowInfo(this);
        }
    }
}
