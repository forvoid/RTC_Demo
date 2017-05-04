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
    private int whocall;
    private int towho;


    public Records() {
    }

    public Records(int uid, Date starttime, int length,int towho,int whocall) {
        this.uid = uid;
        this.starttime = starttime;
        this.length = length;
        this.length = length;
        this.towho= towho;
        this.whocall = whocall;
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
    public int getWhocall() {
        return whocall;
    }

    public void setWhocall(int whocall) {
        this.whocall = whocall;
    }

    public int getTowho() {
        return towho;
    }

    public void setTowho(int towho) {
        this.towho = towho;
    }

}
