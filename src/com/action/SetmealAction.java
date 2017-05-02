package com.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.service.SetmealService;
import com.util.JSONUtil;
import com.vo.Setmeal;
import org.apache.struts2.ServletActionContext;

import java.util.List;

/**
 * 套餐管理
 * Created by forvoid on 4/28/2017.
 */
public class SetmealAction extends ActionSupport {
    private SetmealService setmealService;
    /**新增套餐*/
    public void addSetmeal(){
        String name = ServletActionContext.getRequest().getParameter("name");
        String price = ServletActionContext.getRequest().getParameter("price");
        String total = ServletActionContext.getRequest().getParameter("total");
        String desc = ServletActionContext.getRequest().getParameter("desc");
        Setmeal setmeal = new Setmeal(name,price,total,desc);
        setmealService.add(setmeal);
        UserinfoAction.write(JSONUtil.getJSONObject(0,"新增成功"));
    }
    /**查看全部套餐*/
    public void findALl(){
        List<Setmeal> setmealList = setmealService.findAll();
        if (setmealList!=null||setmealList.size()!=0){
            JSONArray jsonArray = new JSONArray();
            for (Setmeal setmeal:setmealList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",setmeal.getId());
                jsonObject.put("price",setmeal.getPrice());
                jsonObject.put("name",setmeal.getName());
                jsonObject.put("total",setmeal.getTotal());
                jsonObject.put("desc",setmeal.getDesc());
                jsonObject.put("inserttime",setmeal.getInserttime());
                jsonArray.add(jsonObject);
            }
            UserinfoAction.write(JSONUtil.getJSONObject(0,"数据获取成功",jsonArray));
        }else{
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"数据为空"));
        }
    }
    /**查看单个套餐*/
    public void findById(){
        String id = ServletActionContext.getRequest().getParameter("id");
        Setmeal setmeal = setmealService.findById(Integer.parseInt(id));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",setmeal.getId());
        jsonObject.put("price",setmeal.getPrice());
        jsonObject.put("name",setmeal.getName());
        jsonObject.put("total",setmeal.getTotal());
        jsonObject.put("desc",setmeal.getDesc());
        jsonObject.put("inserttime",setmeal.getInserttime());
        UserinfoAction.write(JSONUtil.getJSONObject(0,"数据获取成功",jsonObject));
    }
    /**修改套餐套餐*/
    public void updateSetmeal(){
        String id = ServletActionContext.getRequest().getParameter("id");
        String name = ServletActionContext.getRequest().getParameter("name");
        String price = ServletActionContext.getRequest().getParameter("price");
        String total = ServletActionContext.getRequest().getParameter("total");
        String desc = ServletActionContext.getRequest().getParameter("desc");
        Setmeal setmeal = new Setmeal(Integer.parseInt(id),name,price,total,desc);
        setmealService.update(setmeal);
        UserinfoAction.write(JSONUtil.getJSONObject(0,"修改成功"));
    }
    /**删除套餐*/
    public void delete(){
        String id = ServletActionContext.getRequest().getParameter("id");
    }

    public void setSetmealService(SetmealService setmealService) {
        this.setmealService = setmealService;
    }
}
