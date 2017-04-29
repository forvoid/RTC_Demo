package com.vo;

import java.util.Date;

/**
 * Created by forvoid on 4/19/2017.
 */
public class Records {
    private int id;
    private int uid;
    private Date starttime;
    private int length;

    public Records() {
    }

    public Records(int uid, Date starttime, int length) {
        this.uid = uid;
        this.starttime = starttime;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
