package com.dao;

import com.vo.UserGroup;
import com.vo.UserandGroup;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * 用户组
 *
 */
public class UserGroupDao extends HibernateDaoSupport{
    /**
     * 创建用户组
     * */
    public void createGroup(UserGroup userGroup){
        try {
            userGroup.toString();
            getHibernateTemplate().save(userGroup);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }
    /**
     * 查找用户组
     * */
    public List findByName(String name) {
        String queryString = "from UserGroup as model where model.name= ?";
        return getHibernateTemplate().find(queryString,name);
    }
    /**
     * 查找用户组
     * */
    public List findMohuByName(String name) {
        String queryString = "from UserGroup as model where model.name like ?";
        return getHibernateTemplate().find(queryString,"%"+name+"%");
    }
    /**
     * 判端用户是否已经加入群组
     * */
    public boolean isAddGroup(int uid, int gid) {
        String queryString = "from UserandGroup as model where model.uid= ? and model.gid = ?";
        Query query = getSession().createQuery(queryString);
        query.setParameter(0, uid);
        query.setParameter(1, gid);
        return  query.list().size()==0?true:false;
    }
    /**
     * 加入电话组
     * */
    public void addGroup(UserandGroup userandGroup){
        getHibernateTemplate().save(userandGroup);
    }
    /**
     * 查看电话组
     * */
    public List findUserByName(String name) {
        String queryString = "from UserGroup as model where model.name= ?";
        List<UserGroup> list= getHibernateTemplate().find(queryString,name);
        String queryString2 = "from UserandGroup as model where model.gid= ? and model.status = 1";//
        return getHibernateTemplate().find(queryString,list.get(0).getId());
    }
    public UserGroup findGroupByGid(int id){
        try {
            UserGroup instance = (UserGroup) getHibernateTemplate().get(
                    "com.vo.UserGroup", id);
            return instance;
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return null;
    }
    /**
     * 通过gid找出所有的用户
     * */
    public List findUserBygid(int gid) {
        String queryString = "from UserandGroup as model where model.gid= ?";
        List<UserandGroup> list= getHibernateTemplate().find(queryString,gid);
//        String queryString2 = "from UserandGroup as model where model.gid= ? and model.status = 1";//
        return list;
    }
    /**
     * 通过uid找出所有的用户组
     * */
    public List findGroupByuid(int uid) {
        String queryString = "from UserandGroup as model where model.uid= ?";
        List<UserandGroup> list= getHibernateTemplate().find(queryString,uid);
//        String queryString2 = "from UserandGroup as model where model.gid= ? and model.status = 1";//
        return list;
    }
}
