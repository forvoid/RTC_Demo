package com.vo;

import java.util.Date;

/**
 * Created by forvoid on 4/18/2017.
 */
public class Userinfo {
    private int id;
    private String username;
    private String realname;
    private String password;
    private String addr;
    private Date insertTime;

    public int getSetmealid() {
        return setmealid;
    }

    public void setSetmealid(int setmealid) {
        this.setmealid = setmealid;
    }

    //    private int pkgid;
    private int setmealid;

    public Userinfo() {
    }

    public Userinfo(int id, String username, String realname, String password, String addr) {
        this.id = id;
        this.username = username;
        this.realname = realname;
        this.password = password;
        this.addr = addr;
        this.insertTime = insertTime;

    }

    public Userinfo(String username, String realname, String password, String addr, int setmealid) {
        this.username = username;
        this.realname = realname;
        this.password = password;
        this.addr = addr;
        this.insertTime = new Date();
        this.setmealid = setmealid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }
//
    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}
