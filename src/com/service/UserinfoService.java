package com.service;

import com.dao.UserinfoDao;
import com.vo.Userinfo;

import java.util.List;

/**
 * Created by forvoid on 4/18/2017.
 */
public class UserinfoService {
    private UserinfoDao userinfoDao;

    public void addUserinfo(Userinfo userinfo){
        userinfoDao.addUser(userinfo);
    }
    public void updateUser(Userinfo userinfo) {
        userinfoDao.update(userinfo);
    }
    public List findByUsername(String username){
        return userinfoDao.findByUsername(username);

    }
    public Userinfo findById(int id){
        return userinfoDao.findById(id);
    }
    public UserinfoDao getUserinfoDao() {
        return userinfoDao;
    }

    public void setUserinfoDao(UserinfoDao userinfoDao) {
        this.userinfoDao = userinfoDao;
    }
}
