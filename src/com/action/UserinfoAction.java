package com.action;

import com.alibaba.fastjson.JSONObject;
import com.service.UserinfoService;
import com.util.JSONUtil;
import com.vo.Userinfo;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by forvoid on 4/18/2017.
 */
public class UserinfoAction extends com.opensymphony.xwork2.ActionSupport {
    private HttpSession session;
    private UserinfoService userinfoService;
//
    /**
     * 用户注册
     * */
    public void regist()  {
        JSONObject jsonObject = new JSONObject();
        try {
            String username = ServletActionContext.getRequest().getParameter("username");
            String realname = ServletActionContext.getRequest().getParameter("realname");
            String password = ServletActionContext.getRequest().getParameter("password");
            String addr = ServletActionContext.getRequest().getParameter("addr");
            Userinfo userinfo = new Userinfo(username,realname,password,addr);
            if (username == null || realname == null || password == null || addr == null){
                jsonObject.put("code", -1);
                jsonObject.put("message", "用户未输入完整");
                write(jsonObject);
            }else{
                if ((userinfoService.findByUsername(username)).size()!=0){
                    jsonObject.put("code", -1);
                    jsonObject.put("code", "已经被注册了");
                    write(jsonObject);
                }else {
                    userinfoService.addUserinfo(userinfo);
                    jsonObject.put("code", 0);
                    jsonObject.put("code", "注册成功");
                    write(jsonObject);
                }
            }
        }catch (Exception e){
                jsonObject.put("code", -1);
                jsonObject.put("message", "程序错误");
                write(jsonObject);

        }
    }
    /**
     * 登录
     * */
    public void login(){
        HttpSession session = ServletActionContext.getRequest().getSession();
        JSONObject jsonObject = new JSONObject();
        String username = ServletActionContext.getRequest().getParameter("username");
        String password = ServletActionContext.getRequest().getParameter("password");
        List<Userinfo> list = userinfoService.findByUsername(username);
        if (list!=null&&list.size()!=0){
            if(list.get(0).getPassword().equals(password)){//密码相等
                jsonObject.put("code",0);
                jsonObject.put("message",",登录成功!");
                session.setAttribute("user",list.get(0));
            }else{
                jsonObject.put("code",-1);
                jsonObject.put("message",",密码错误!");
            }
        }else{
            jsonObject.put("code",-1);
            jsonObject.put("message","没有该用户");
        }
        write(jsonObject);
    }
    /**
     * 用户个人信息修改
     * */
    public void updateUserInfo(){

        String realname = ServletActionContext.getRequest().getParameter("realname");
        String addr = ServletActionContext.getRequest().getParameter("addr");
        if ( realname == null || addr == null) {
            write(JSONUtil.getJSONObject(-1, "输入数据不完全"));
            return;
        }
        session = ServletActionContext.getRequest().getSession();
        Userinfo userinfo = (Userinfo) session.getAttribute("user");
        if (userinfo == null) {

        }
        userinfo.setRealname(realname);
        userinfo.setAddr(addr);
        userinfoService.updateUser(userinfo);
        session.setAttribute("user",userinfo);
        write(JSONUtil.getJSONObject(0,"修改成功"));
    }
    /**
     * 用户个人密码修改
     * */
    public void updateUserPassword(){

        String password = ServletActionContext.getRequest().getParameter("password");
        if ( password == null ) {
            write(JSONUtil.getJSONObject(-1, "输入数据不完全"));
            return;
        }
        session = ServletActionContext.getRequest().getSession();
        Userinfo userinfo = (Userinfo) session.getAttribute("user");
        if (userinfo == null) {

        }
        userinfo.setPassword(password);
        userinfoService.updateUser(userinfo);
        session.setAttribute("user",userinfo);
        write(JSONUtil.getJSONObject(0,"修改成功"));
    }
    /**查看个人信息*/
    public void seeUserInfo(){
        String id = ServletActionContext.getRequest().getParameter("id");

        session = ServletActionContext.getRequest().getSession();
        Userinfo userinfo = (Userinfo) session.getAttribute("user");
        if (userinfo == null) {

            UserinfoAction.write(JSONUtil.getJSONObject(-2,"用户未登录"));
            return;
        }

        int uid ;
        if (id==null){
            uid = userinfo.getId();
        }else{
            uid = Integer.parseInt(id);
        }
            userinfo =userinfoService.findById(uid);
            if (userinfo == null){
                write(JSONUtil.getJSONObject(-1,"没有该用户id"));
                return;
            }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",userinfo.getId());
        jsonObject.put("username",userinfo.getUsername());
        jsonObject.put("realname",userinfo.getRealname());
        jsonObject.put("addr",userinfo.getAddr());
        jsonObject.put("inserttime",userinfo.getInsertTime());
        write(JSONUtil.getJSONObject(0,"获取成功",jsonObject));
    }
    public static void write(JSONObject jsonObject){
        HttpServletResponse response= ServletActionContext.getResponse();
        response.setContentType("text/html;charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(jsonObject);
        out.println(jsonObject);
        out.flush();
        out.close();
    }
    public UserinfoService getUserinfoService() {
        return userinfoService;
    }

    public void setUserinfoService(UserinfoService userinfoService) {
        this.userinfoService = userinfoService;
    }
}
