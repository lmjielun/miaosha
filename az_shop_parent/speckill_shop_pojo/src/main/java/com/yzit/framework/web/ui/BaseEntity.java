package com.yzit.framework.web.ui;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class BaseEntity implements Serializable {
    //分页参数
    @JsonIgnore  //返回json数据时，忽略该属性
    private  int  pageNo=1 ;//当前页
    @JsonIgnore  //返回json数据时，忽略该属性
    private int  pageSize = 10;//页面显示记录数

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
