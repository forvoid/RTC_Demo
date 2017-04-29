package com.vo;

import java.util.Date;

/**
 * Created by forvoid on 4/18/2017.
 */
public class UserGroup {
    private int id;
    private String name;
    private int createid;
    private Date inserttime;
    private int status;

    public UserGroup() {
    }

    @Override
    public String toString() {
        return "UserGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createid=" + createid +
                ", inserttime=" + inserttime +
                ", status=" + status +
                '}';
    }

    public UserGroup(String name, int createid, int status) {
        this.name = name;
        this.createid = createid;
        this.inserttime = new Date();
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
