package com.dao;

import com.vo.Userinfo;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by forvoid on 4/18/2017.
 */
public class UserinfoDao extends HibernateDaoSupport {


    public void addUser(Userinfo transientInstance) {
        try {
            getHibernateTemplate().save(transientInstance);
        } catch (RuntimeException re) {
            re.printStackTrace();
            String a ="fafa";
            char[] b= a.toCharArray();
            String c = String.valueOf(b);
        }
    }

    public void update(Userinfo transientInstance) {
        try {
            getHibernateTemplate().update(transientInstance);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }

    public void delete(Userinfo persistentInstance) {
        try {
            getHibernateTemplate().delete(persistentInstance);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }

    public Userinfo findById(Integer id) {
        try {
            Userinfo instance = (Userinfo) getHibernateTemplate().get(
                    "com.vo.Userinfo", id);
            return instance;
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return null;
    }
    public List findByUsername(String username) {

            String queryString = "from Userinfo as model where model.username= ?";
            return getHibernateTemplate().find(queryString,username);
    }

    /**
     * 根据数据查询
     * */
    public List findByProperty(String propertyName, Object value) {
        try {
            String queryString = "from Userinfo as model where model."
                    + propertyName + "= ?";
            return getHibernateTemplate().find(queryString, value);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return  null;
    }
}
