package com.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dao.BillDao;
import com.dao.UserGroupDao;
import com.dao.UserinfoDao;
import com.vo.Records;
import com.vo.UserGroup;
import com.vo.UserandGroup;
import com.vo.Userinfo;
import com.websocket.Chat;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by forvoid on 4/19/2017.
 */
public class CallService {

    private  BillDao billDao;
    private UserGroupDao userGroupDao;
    private UserinfoDao userinfoDao;

    /**
     * 通过用户id查找所以用户组的数据
     * */
    public JSONArray findAllGroupByUid(int uid){
        JSONArray jsonArray = new JSONArray();
        List<UserandGroup> userGroupList = userGroupDao.findGroupByuid(uid);
        if (userGroupList!=null&&userGroupList.size()!=0){
            for (UserandGroup userandGroup:userGroupList){
                UserGroup userGroup =  userGroupDao.findGroupByGid(userandGroup.getGid());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("gid",userandGroup.getGid());
                jsonObject.put("name",userGroup.getName());
                //根据gid查询所有的用户
                List<UserandGroup> userandGroups = userGroupDao.findUserBygid(userandGroup.getGid());
                JSONArray userArray = new JSONArray();
                if (userandGroups!=null&&userandGroups.size()!=0) {
                    for (UserandGroup userandGroup1 :userandGroups){
                        if (userandGroup1.getUid() == uid)continue;//如果是自己就不用写入
                        Userinfo userinfo = userinfoDao.findById(userandGroup1.getUid());
                        JSONObject userObject  = new JSONObject();
                        userObject.put("uid",userinfo.getId());
                        userObject.put("username",userinfo.getUsername());
                        userObject.put("realname",userinfo.getRealname());
                        userObject.put("status", Chat.connections.containsKey(userinfo.getId())?"online":"offline");
                        userArray.add(userObject);
                    }
                    jsonObject.put("user",userArray);
                }
                jsonArray.add(jsonObject);
            }
        }
        return jsonArray;
    }

    /**用户通话添加**/
    public void addRecordes(Records records){
        billDao.addRecordes(records);
    }
    /**
     * 电话呼叫验证
     * */
    public  boolean callYanZheng(int uid){
        double sum = billDao.billSumById(uid);
        JSONObject jsonObject = new JSONObject();
        if (sum>0){
            return true;
        }else {
            return false;
        }
    }
    /**
     * findGroupByuid
     * */
    public JSONArray findGroupByuid(int uid){
        List<UserandGroup>  userandGroups = userGroupDao.findGroupByuid(uid);
        if (userandGroups!=null&&userandGroups.size()!=0){
            JSONArray jsonArray =new JSONArray();
            for (UserandGroup userandGroup:userandGroups){
                jsonArray.add(userandGroup.getGid());
            }
            return jsonArray;
        }
        return null;
    }
    /**
     * 挂电话
     * */
    /**
     *电话计费
     * */
    public  BillDao getBillDao() {
        return billDao;
    }

    public void setBillDao(BillDao billDao) {
        this.billDao = billDao;
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }

    public UserinfoDao getUserinfoDao() {
        return userinfoDao;
    }

    public void setUserinfoDao(UserinfoDao userinfoDao) {
        this.userinfoDao = userinfoDao;
    }
}
