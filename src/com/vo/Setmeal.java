package com.vo;

import java.util.Date;

/**
 * Created by forvoid on 4/28/2017.
 */
public class Setmeal {
    private int id;
    private String name ;
    private String price;
    private String total;
    private String desc;
    private Date inserttime;

    @Override
    public String toString() {
        return "Setmeal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", total='" + total + '\'' +
                ", desc='" + desc + '\'' +
                ", inserttime=" + inserttime +
                '}';
    }

    public Setmeal() {
    }

    public Setmeal(int id, String name, String price, String total, String desc) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.total = total;
        this.desc = desc;
    }

    public Setmeal(String name, String price, String total, String desc) {
        this.name = name;
        this.price = price;
        this.total = total;
        this.desc = desc;
        this.inserttime = new Date();
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getInserttime() {
        return inserttime;
    }

    public void setInserttime(Date inserttime) {
        this.inserttime = inserttime;
    }
}
