package com.dao;

import com.vo.Setmeal;
import com.vo.Userinfo;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * Created by forvoid on 4/28/2017.
 */
public class SetmealDao extends HibernateDaoSupport {
    public void addUser(Setmeal transientInstance) {
        try {
            System.out.println(transientInstance.toString());
            getHibernateTemplate().save(transientInstance);
//            getSession().save(transientInstance);

        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }

    public void update(Setmeal transientInstance) {
        try {
            getHibernateTemplate().update(transientInstance);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }
    public List findByAll() {

        String queryString = "from Setmeal as model ";
        return getHibernateTemplate().find(queryString);
    }

}
