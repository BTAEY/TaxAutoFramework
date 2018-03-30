package com.ey.tax.stream.poi.model;

/**
 * Created by zhuji on 2/6/2018.
 */
public class ColumnHeaderModel {
    private String mark;

    private String header;

    public ColumnHeaderModel(String mark) {
        this.mark = mark;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
