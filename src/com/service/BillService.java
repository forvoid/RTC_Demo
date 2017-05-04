package com.service;

import com.dao.BillDao;
import com.vo.Recharge;
import com.vo.Records;

import java.util.List;

/**
 * Created by forvoid on 4/19/2017.
 */
public class BillService {
    private BillDao billDao;
    public List findRecordByUid(int uid){

        return  billDao.findRecordByUid(uid);
    }
    public List findRecordByUidPage(int uid,int page, int row){

        return  billDao.findRecordByUidPage(uid,page,row);
    }

    public List findAllRecord(){

        return  billDao.findAllRecord();
    }

    /**用户通话记录查询**/
    public List findRechargeByUid(int uid){

        return  billDao.findRechargeByUid(uid);
    }
    /**用户通话记录查询**/
    public List findRechargeByUidPage(int uid,int page,int row){

        return  billDao.findRechargeByUidPage(uid,page,row);
    }

    public List findAllRecharge(){

        return  billDao.findAllRecharge();
    }
    /**用户话费费用查询**/

    public double billSumById(int uid){
        double sum = 0;
        List<Recharge> list = billDao.findRechargeByUid(uid);
        if (list!=null&&list.size()!=0){
            for (Recharge recharge :list){
                sum += recharge.getMoney();
            }
        }
        List<Records> recordsList = billDao.findRecordByUid(uid);
        if (recordsList!=null&&recordsList.size()!=0){
            for (Records records:recordsList){
                sum-=records.getLength();
            }
        }
        return sum;
//        return billDao.billSumById(uid);
    }
    /**用户充值操作**/
    public void addRecharge(Recharge recharge){
        billDao.addRecharge(recharge);
    }
    /**用户通话添加**/
    public void addRecordes(Records records){
       billDao.addRecordes(records);
    }

    public BillDao getBillDao() {
        return billDao;
    }

    public void setBillDao(BillDao billDao) {
        this.billDao = billDao;
    }
}
