package com.vo;

import java.util.Date;

/**
 * Created by forvoid on 4/19/2017.
 */
public class Recharge {
    private int id;
    private int uid;
    private int createid;
    private Date inserttime;
    private Double money;
    private int status;
    private String serveraddr;

    public Recharge() {
    }

    public Recharge(int uid, int createid, Date inserttime, Double money, int status, String serveraddr) {
        this.uid = uid;
        this.createid = createid;
        this.inserttime = inserttime;
        this.money = money;
        this.status = status;
        this.serveraddr = serveraddr;
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

    public int getCreateid() {
        return createid;
    }

    public void setCreateid(int createid) {
        this.createid = createid;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getServeraddr() {
        return serveraddr;
    }

    public void setServeraddr(String serveraddr) {
        this.serveraddr = serveraddr;
    }
}
