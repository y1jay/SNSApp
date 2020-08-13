package com.yijun.myapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostRes {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("rows")
    @Expose
    private List<Row> rows = null;
    @SerializedName("cnt")
    @Expose
    private Integer cnt;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

}
