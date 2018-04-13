package com.ey.tax.web.core;

import java.io.Serializable;
import java.util.List;

public class ResponseData implements Serializable{
    public enum Status{
        FAILED,SUCCESS
    }

    private Status status;

    private String code;

    private String message;

    private List<Object> contents;

    public ResponseData(Status status){
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getContents() {
        return contents;
    }

    public void setContents(List<Object> contents) {
        this.contents = contents;
    }
}
