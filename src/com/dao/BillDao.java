package com.dao;

import com.vo.Recharge;
import com.vo.Records;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

/**
 * 话费管理
 *
 */
public class BillDao extends HibernateDaoSupport {
    private Recharge recharge;
    private Records records;

    /**用户充值记录查询**/

    /**
     * 通过uid查询充值记录
     * */
    public List findRecordByUid(int uid){
        try {
            String queryString = "from Records as model where model.uid= ? ";
            return getHibernateTemplate().find(queryString, uid);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return  null;
    }
    public List findRecordByUidPage(int uid,int page,int row){
        try {
            String queryString = "from Records as model where model.uid= ?";
             Query query = getSession().createQuery(queryString);
            query.setParameter(0, uid);
            query.setMaxResults(row);
            query.setFirstResult((page-1)*row);
            return query.list();
//                    getHibernateTemplate().find(queryString, uid);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return  null;
    }

    public List findAllRecord(){
        try {
            String queryString = "from Records ";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return  null;
    }
    /**用户通话记录查询**/
    public List findRechargeByUidPage(int uid,int page,int row){
        try {
            String queryString = "from Recharge as model where model.uid= ?";
            Query query = getSession().createQuery(queryString);
            query.setParameter(0, uid);
            query.setMaxResults(row);
            query.setFirstResult((page-1)*row);
            return query.list();
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return  null;
    }
    /**用户通话记录查询**/
    public List findRechargeByUid(int uid){
        try {
            String queryString = "from Recharge as model where model.uid= ?";
            return getHibernateTemplate().find(queryString, uid);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return  null;
    }

    public List findAllRecharge(){
        try {
            String queryString = "from Recharge ";
            return getHibernateTemplate().find(queryString);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return  null;
    }
    /**用户话费费用查询**/

    public double billSumById(int uid){
        double sum = 0;
        List<Recharge> list = findRechargeByUid(uid);
        if (list.size()!=0){
            for (Recharge recharge :list){
                sum += recharge.getMoney();
            }
        }
        List<Records> recordsList = findRecordByUid(uid);
        if (recordsList.size()!=0){
            for (Records records:recordsList){
                sum-=records.getLength();
            }
        }
        return sum;
    }
    /**用户充值操作**/
    public void addRecharge(Recharge recharge){
        try {
            getHibernateTemplate().save(recharge);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }
    /**用户通话添加**/
    public void addRecordes(Records records){
        try {
            getHibernateTemplate().save(records);
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }

    public Recharge getRecharge() {
        return recharge;
    }

    public void setRecharge(Recharge recharge) {
        this.recharge = recharge;
    }

    public Records getRecords() {
        return records;
    }

    public void setRecords(Records records) {
        this.records = records;
    }
}
