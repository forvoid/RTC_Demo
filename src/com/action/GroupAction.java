package com.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.service.GroupService;
import com.util.JSONUtil;
import com.vo.UserGroup;
import com.vo.UserandGroup;
import com.vo.Userinfo;
import org.apache.struts2.ServletActionContext;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * 用户群组管理
 */
public class GroupAction extends ActionSupport{
    private HttpSession session;
    private GroupService groupService;

    /*用户新建一个用户组*/
    public void createGroup(){
        try {
            session = ServletActionContext.getRequest().getSession();
            Userinfo userinfo = (Userinfo) session.getAttribute("user");
            if (userinfo == null) {
                UserinfoAction.write(JSONUtil.getJSONObject(-2,"用户未登录"));
                return;
            }
            int uid = userinfo.getId();
            String name = ServletActionContext.getRequest().getParameter("name");
//        UserGroup userGroup = new UserGroup(name,userinfo.getId(),0);
            UserGroup userGroup = new UserGroup(name, uid, 0);
            groupService.create(userGroup);//创建一个用户组
            //添加创建用户进入该用户组
            groupService.findByName(name);
            groupService.addGroupByCreate(groupService.findByName(name).getId(), uid);
            UserinfoAction.write(JSONUtil.getJSONObject(0,"添加成功"));
        }catch (Exception e){
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"添加失败"));
        }
    }
    /**查找用户组*/
    public void findByName(){
        String name = ServletActionContext.getRequest().getParameter("name");
        List<UserGroup> list = groupService.findGroupMohuByName(name);
        if (list.size()==0){
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"没有查到"));
        }else{
            JSONArray jsonArray = new JSONArray();
            for (UserGroup userGroup : list){
                JSONObject jsonObject =new JSONObject();
                jsonObject.put("id",userGroup.getId());
                jsonObject.put("name",userGroup.getName());
                Userinfo userinfo = groupService.findByIdForUserInfo(userGroup.getCreateid());
                if (userinfo!=null) {
                    jsonObject.put("createid", userinfo.getId());
                    jsonObject.put("createname", userinfo.getUsername());
                }
                jsonObject.put("time",userGroup.getInserttime());
                jsonArray.add(jsonObject);
            }
            UserinfoAction.write(JSONUtil.getJSONObject(0,"查到",jsonArray));
        }
    }
    /**
     * 加入用户组
     * */
    public void addGroup(){
        int gid = Integer.parseInt( ServletActionContext.getRequest().getParameter("gid"));
        session = ServletActionContext.getRequest().getSession();
        Userinfo userinfo = (Userinfo) session.getAttribute("user");
        if (userinfo == null) {
            UserinfoAction.write(JSONUtil.getJSONObject(-2,"用户未登录"));
            return;
        }
        int uid = userinfo.getId();
        //判断用户是否已经加入过群组
        if (groupService.isAddGroup(uid,gid)){
            groupService.addGroup(gid,uid);
            UserinfoAction.write(JSONUtil.getJSONObject(0,"添加成功等待管理员审核"));
        }else{
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"用户已经添加过了"));
        }

    }
    /**
     *查看群组中的用户信息
     * */
    public void findUserinfoByGid(){
        Integer gid = Integer.parseInt(ServletActionContext.getRequest().getParameter("gid"));
        List<UserandGroup> list = groupService.findUserListByGid(gid);
        if (list.size()==0){
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"没有查到"));
        }else{
            JSONArray jsonArray = new JSONArray();
            for (UserandGroup userandGroup : list){
                JSONObject jsonObject =new JSONObject();
                jsonObject.put("id",userandGroup.getId());
                jsonObject.put("gid",userandGroup.getGid());
                jsonObject.put("status",userandGroup.getStatus());
                Userinfo userinfo = groupService.findByIdForUserInfo(userandGroup.getUid());
                if (userinfo!=null) {
                    jsonObject.put("createid", userinfo.getId());
                    jsonObject.put("createname", userinfo.getUsername());
                    jsonObject.put("realname", userinfo.getRealname());
                }
                jsonObject.put("time",userandGroup.getInserttime());
                jsonArray.add(jsonObject);
            }
            UserinfoAction.write(JSONUtil.getJSONObject(0,"查到",jsonArray));
        }
    }
    /**
     *查看群组中的用户信息
     * */
    public void findGroupByUid(){
        Integer uid = Integer.parseInt(ServletActionContext.getRequest().getParameter("uid"));
        List<UserandGroup> list = groupService.findGroupListByuid(uid);
        if (list.size()==0){
            UserinfoAction.write(JSONUtil.getJSONObject(-1,"没有查到"));
        }else{
            JSONArray jsonArray = new JSONArray();
            for (UserandGroup userandGroup : list){
                JSONObject jsonObject =new JSONObject();
//                jsonObject.put("id",userandGroup.getId());
                jsonObject.put("status",userandGroup.getStatus());
                UserGroup userGroup = getGroupService().findGroupById(userandGroup.getGid());
                if (userGroup!=null){
                    jsonObject.put("gid",userandGroup.getId());
                    jsonObject.put("name",userGroup.getName());
                    jsonObject.put("createid",userGroup.getCreateid());
                    jsonObject.put("time",userandGroup.getInserttime());
                }
//                jsonObject.put("guanxishijian",userandGroup.getInserttime());

                jsonArray.add(jsonObject);
            }
            UserinfoAction.write(JSONUtil.getJSONObject(0,"查到",jsonArray));
        }
    }

    public GroupService getGroupService() {
        return groupService;
    }

    public void setGroupService(GroupService groupService) {
        this.groupService = groupService;
    }
}
