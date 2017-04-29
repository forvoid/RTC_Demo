package com.vo;

import java.util.Date;

/**
 * Created by forvoid on 4/18/2017.
 */
public class UserandGroup {
    private int id;
    private int gid;
    private int uid;
    private int status;
    private Date inserttime;

    public UserandGroup(int gid, int uid, int status) {
        this.gid = gid;
        this.uid = uid;
        this.status = status;
        this.inserttime = new Date();
    }

    public UserandGroup() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }
}
