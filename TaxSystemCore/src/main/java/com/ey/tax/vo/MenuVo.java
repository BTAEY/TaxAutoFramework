package com.ey.tax.vo;

import java.util.ArrayList;
import java.util.List;

public class MenuVo {
    private String name;

    private String url;

    private Integer level;

    private Integer order;

    private boolean hasChild;

    private List<MenuVo> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHasChild() {
        return hasChild;
    }

    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<MenuVo> getChildren() {
        if(children == null){
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
