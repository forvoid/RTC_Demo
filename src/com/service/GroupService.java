package com.service;

import com.dao.UserGroupDao;
import com.dao.UserinfoDao;
import com.vo.UserGroup;
import com.vo.UserandGroup;
import com.vo.Userinfo;

import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by forvoid on 4/18/2017.
 */
public class GroupService {
    private UserGroupDao userGroupDao;
    private UserinfoDao userinfoDao;

    public UserinfoDao getUserinfoDao() {
        return userinfoDao;
    }

    public void setUserinfoDao(UserinfoDao userinfoDao) {
        this.userinfoDao = userinfoDao;
    }

    public void create(UserGroup userGroup){
        userGroupDao.createGroup(userGroup);

    }

    public Userinfo findByIdForUserInfo(int id){
        return userinfoDao.findById(id);
    }

    public void addGroupByCreate(int gid,int uid){
        UserandGroup userandGroup = new UserandGroup(gid, uid, 1);
        userGroupDao.addGroup(userandGroup);
    }
    public void addGroup(int gid,int uid){
        UserandGroup userandGroup = new UserandGroup(gid, uid, 1);
        userGroupDao.addGroup(userandGroup);
    }
    public List findGroupMohuByName(String name){
        return userGroupDao.findMohuByName(name);
    }
    public List findUserListByGid(int gid){
        return userGroupDao.findUserBygid(gid);
    }
    public List findGroupListByuid(int uid){
        return userGroupDao.findGroupByuid(uid);
    }
    public UserGroup findGroupById(int gid){
       return userGroupDao.findGroupByGid(gid);
    }
    public UserGroup findByName(String name){
        return (UserGroup)(userGroupDao.findByName(name).get(0));
    }

    public UserGroupDao getUserGroupDao() {
        return userGroupDao;
    }

    public void setUserGroupDao(UserGroupDao userGroupDao) {
        this.userGroupDao = userGroupDao;
    }
}
