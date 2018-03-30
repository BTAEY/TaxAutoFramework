package com.ey.tax.stream.poi.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhuji on 2/6/2018.
 */
public class RowModel {
    private int rowNum;

    private Map<String,Object> headerValueMapping;

    private boolean empty = true;

    public RowModel(int rowNum) {
        super();
        this.rowNum = rowNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public Map<String, Object> getHeaderValueMapping() {
        if(headerValueMapping == null){
            headerValueMapping = new HashMap<>();
        }
        return headerValueMapping;
    }

    public void setHeaderValueMapping(Map<String, Object> headerValueMapping) {
        this.headerValueMapping = headerValueMapping;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
