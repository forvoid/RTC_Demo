package com.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.service.BillService;
import com.service.UserinfoService;
import com.util.JSONUtil;
import com.vo.Recharge;
import com.vo.Records;
import com.vo.Userinfo;
import org.apache.struts2.ServletActionContext;

import java.util.Date;
import java.util.List;


public class BillAction extends ActionSupport {
    private BillService billService;
    private UserinfoService userinfoService;

    public void setUserinfoService(UserinfoService userinfoService) {
        this.userinfoService = userinfoService;
    }

    public void findRecordByUid(){
        String uid = ServletActionContext.getRequest().getParameter("uid");
        String page = ServletActionContext.getRequest().getParameter("page");
        String row = ServletActionContext.getRequest().getParameter("row");
        List<Records> recordsList =billService.findRecordByUidPage(Integer.parseInt(uid),Integer.parseInt(page),Integer.parseInt(row));

        if (recordsList!=null&&recordsList.size()!=0){
            JSONArray jsonArray = new JSONArray();
            for (Records records : recordsList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",records.getId());
                jsonObject.put("uid",records.getUid());
                jsonObject.put("starttime",records.getStarttime());
                Userinfo userinfo  = userinfoService.findById(records.getTowho());
                jsonObject.put("towho",userinfo.getRealname());
                jsonObject.put("length",records.getLength());
                jsonArray.add(jsonObject);
            }
            List<Records> rr =billService.findRecordByUid(Integer.parseInt(uid));
            UserinfoAction.write(JSONUtil.getJSONObject(0,"获取成功",jsonArray,rr.size()));
        }else {
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"没有数据"));
        }
    }

    public void findAllRecord(){

        List<Records> recordsList =billService.findAllRecord();
        if (recordsList!=null&&recordsList.size()!=0){
            JSONArray jsonArray = new JSONArray();
            for (Records records : recordsList){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",records.getId());
                jsonObject.put("uid",records.getUid());
                jsonObject.put("starttime",records.getStarttime());
                jsonObject.put("length",records.getLength());
                jsonArray.add(jsonObject);
            }
            UserinfoAction.write(JSONUtil.getJSONObject(0,"获取成功",jsonArray));
        }else {
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"没有数据"));
        }
    }

    /**用户通话记录查询**/
    public void findRechargeByUid(){
        String uid = ServletActionContext.getRequest().getParameter("uid");
        String page = ServletActionContext.getRequest().getParameter("page");
        String  row= ServletActionContext.getRequest().getParameter("row");
        List<Recharge> recharges =billService.findRechargeByUidPage(Integer.parseInt(uid),Integer.parseInt(page),Integer.parseInt(row));
        if (recharges!=null&&recharges.size()!=0){
            JSONArray jsonArray = new JSONArray();
            for (Recharge recharge : recharges){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",recharge.getId());
                jsonObject.put("uid",recharge.getUid());
                jsonObject.put("inserttime",recharge.getInserttime());
                jsonObject.put("money",recharge.getMoney());
                jsonObject.put("status",recharge.getStatus());
                jsonObject.put("createid",recharge.getCreateid());
                jsonObject.put("serveraddr",recharge.getServeraddr());
                jsonArray.add(jsonObject);
            }
            List<Recharge> re =billService.findRechargeByUid(Integer.parseInt(uid));
            UserinfoAction.write(JSONUtil.getJSONObject(0,"获取成功",jsonArray,re.size()));
        }else {
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"没有数据"));
        }
    }

    public void findAllRecharge(){

        List<Recharge> recharges =billService.findAllRecharge();
        if (recharges!=null&&recharges.size()!=0){
            JSONArray jsonArray = new JSONArray();
            for (Recharge recharge : recharges){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",recharge.getId());
                jsonObject.put("uid",recharge.getUid());
                jsonObject.put("inserttime",recharge.getInserttime());
                jsonObject.put("money",recharge.getMoney());
                jsonObject.put("status",recharge.getStatus());
                jsonObject.put("createid",recharge.getCreateid());
                jsonObject.put("serveraddr",recharge.getServeraddr());
                jsonArray.add(jsonObject);
            }
            UserinfoAction.write(JSONUtil.getJSONObject(0,"获取成功",jsonArray));
        }else {
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"没有数据"));
        }
    }
    /**用户话费费用查询**/

    public void billSumById(){
        String uid = ServletActionContext.getRequest().getParameter("uid");
        double sum = billService.billSumById(Integer.parseInt(uid));

        UserinfoAction.write(JSONUtil.getJSONObject(0,"获取数据成功",sum));

    }
    /**用户充值操作**/
    public void addRecharge(){
        String uid = ServletActionContext.getRequest().getParameter("uid");
        String createid = ServletActionContext.getRequest().getParameter("createid");
        String money = ServletActionContext.getRequest().getParameter("money");
        String ip = ServletActionContext.getRequest().getRemoteAddr();
        Recharge recharge = new Recharge(Integer.parseInt(uid),Integer.parseInt(createid),new Date(),Double.parseDouble(money),1,ip);
        billService.addRecharge(recharge);
        UserinfoAction.write(JSONUtil.getJSONObject(0,"获取添加成功"));

    }
//    /**用户通话添加测试**/
//    public void addRecordes(){
//        Records records1 = new Records(1,new Date(),50);
//        billService.addRecordes(records1);
//    }


    public BillService getBillService() {
        return billService;
    }

    public void setBillService(BillService billService) {
        this.billService = billService;
    }
}
