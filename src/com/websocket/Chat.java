package com.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.opensymphony.xwork2.ActionSupport;
import com.service.CallService;
import com.util.JSONUtil;
import com.vo.Records;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by forvoid on 4/19/2017.
 */
@ServerEndpoint(value = "/ws/chat/{id}")
public class Chat {
    public static CallService callService;
    static{
        ApplicationContext act = new ClassPathXmlApplicationContext("applicationContext-bean.xml");
        callService = (CallService)act.getBean("callService");

    }
    public static  Map<Integer,Chat> connections =new ConcurrentHashMap<>();
//    private static CopyOnWriteArraySet<Chat> sessions = new CopyOnWriteArraySet<TransmissionLocationWebSocket>();

    private Session session;
    private int id ;
    private int status;
    private int whoCall;
    private Date starttime;

    @OnOpen
    public void start(Session session,@PathParam(value="id") int id) {
        try {
            this.id = id;
            this.session = session;
            this.status = 0;
            this.whoCall = 0;
            connections.put(id, this);
            System.out.format("* %s %s", this.id, "has joined.");
//        上线提示;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", id);
            jsonObject.put("gid",callService.findGroupByuid(id));
            pushAllElseSelf(JSONUtil.getSocketJSONObject(0, "用户" + id + "上线了", jsonObject, "online").toString(), id);
//            System.out.println(jsonObject.toJSONString());
//            JSONUtil.getSocketJSONObject(0,"用户"+id+"下线了",jsonObject,"offline").toString()
            //发送群组数据信息
            allGroup(this.id);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @OnClose
    public void end() throws IOException {
        if (this.whoCall!=0) {
            hangup();
        }
        Chat chat = connections.get(this.id);
        connections.remove(this.id);
        chat.session.close();

        chat = null;
        System.out.println("* %s %s"+
                this.id+ "has disconnected.");
        //        下线提示;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",id);
        jsonObject.put("gid",callService.findGroupByuid(id));
        pushAllElseSelf(JSONUtil.getSocketJSONObject(0,"用户"+id+"下线了",jsonObject,"offline").toString(),this.id);

    }
    @OnError
    public void onError(Throwable t) throws Throwable {
        this.end();
        System.out.println("Chat Error: " + t.toString());
    }
    @OnMessage
    public void incoming(String message) {
        try {
            System.out.println(message);
            JSONObject jsonObject = JSON.parseObject(message);
            String option = jsonObject.getString("option");
            if (option.equals("call")) {//打电话
                call(jsonObject);
                return;
            }
            if (option.equals("answer")) {//接电话
                answerPhone(jsonObject);
                return;
            }
            if (option.equals("hangup")) {//挂电话
                hangup();
                return;
            }
            if (option.equals("allGroup")) {//获取所有用户群组
                allGroup(this.id);
                return;
            }
            if (option.equals("message")) {//发送私有消息
                message(jsonObject);
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 返回当前用户所以用户组的情况
     * **/
    public void allGroup(int uid){
        JSONArray jsonArray = callService.findAllGroupByUid(uid);
        if (jsonArray!=null&&jsonArray.size()!=0) {
            pushOneSelf(JSONUtil.getSocketJSONObject(0,
                    "返回用户组在线情况",jsonArray , "allGroup").toJSONString());
        }else{
            pushOneSelf(JSONUtil.getSocketJSONObject(-1,
                    "用户没有群组",null, "allGroup").toJSONString());
        }
    }
    /**
     * 挂电话
     * */
    public void hangup(){
        int callId = whoCall; //打电话的id
        int length = (int)(new Date().getTime()-this.starttime.getTime())/60000;
        Records records = new Records(callId,starttime,length,this.status==this.whoCall?this.id:this.status,callId==whoCall?1:2);
        callService.addRecordes(records);//计入话费
//        if (length!=0){
//             records = new Records(this.status,starttime,length,callId==whoCall?2:1,whoCall);
//            callService.addRecordes(records);//计入话费
//        }

        pushOne(JSONUtil.
                getSocketJSONObject(0,
                        "用户"+this.id+"挂断了电话",null,"be_hangup").toJSONString(),status);
        pushOneSelf(JSONUtil.
                getSocketJSONObject(0,
                        "用户"+this.id+"挂断了电话",null,"hangup_ok").toJSONString());
        //扫尾工作
        //被关联用户
        Chat chat = connections.get(this.status);
        chat.whoCall=0;
        chat.starttime = null;
        chat.status = 0;
        //自己
        this.starttime = null;
        this.whoCall = 0;
        this.status = 0;

    }
    /**
     *
     * 通信过程中进行数据传输
     * **/
    public void message(JSONObject jsonObject){
//        System.out.println("进入messsage");
//        System.out.println("from"+id);
//        System.out.println("to"+this.status);
//        System.out.println(jsonObject.toString());
//        JSONObject dataJSON = (JSONObject)jsonObject.get("data");
//        resultJson.put("from",id);
        pushOne(JSONUtil.getSocketJSONObject(
                0,
                "用户"+this.id+"向你发送信息",this.status+"",id+"",jsonObject.get("data"),"message").toJSONString(),this.status);
        System.out.println("-----"+JSONUtil.getSocketJSONObject(
                0,
                "用户"+this.id+"向你发送信息",this.status+"",id+"",jsonObject.get("data"),"message").toJSONString());
    }
    /**
     * 收到电话请求后反映
     * 1、接听
     * 2、不接听
     *
     * */
    public void answerPhone(JSONObject jsonObject){
        try {
            JSONObject resultjson = new JSONObject();
            String request = jsonObject.getString("request");
            if (request.equals("ok")) {//接通谢谢
                //开始计费
                this.starttime = new Date();
                System.out.println("我的对象"+this.status);
                System.out.println(connections.get(this.status));
                connections.get(this.status).starttime = new Date();//拨号者时间
                //返回给拨号者信息
//                resultjson.put("code", 0);
//                resultjson.put("option", "link_ok");
//                resultjson.put("message", "对方同意接听电话");
                pushOne(JSONUtil.getSocketJSONObject(0,"对方同意接听电话",jsonObject.getJSONObject("data"),"agree").toJSONString(), this.status);
                //-----
                //返回给接听者
                resultjson.put("code", 0);
                resultjson.put("option", "link_ok");
                resultjson.put("message", "已经接通了电话。可以发送msg信息了");
                pushOneSelf(resultjson.toJSONString());
            } else if (request.equals("sorry")) {//我不接
                connections.get(this.status).status = 0;//恢复拨号者的状态
                connections.get(this.status).whoCall = 0;//恢复拨号者的状态
                //返回给拨号者信息
                resultjson.put("code", 0);
                resultjson.put("option", "sorry");
                resultjson.put("message", "对方不接电话");
                pushOne(resultjson.toJSONString(), this.status);
                this.status = 0;//恢复拨号者的状态
                this.whoCall = 0;
                //返回给拒绝者
                resultjson.put("code", 0);
                resultjson.put("option", "refuse_ok");
                resultjson.put("message", "已经拒绝了");
                pushOneSelf(resultjson.toJSONString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 呼叫电话
     * 打给谁
     * 打电话需要预热  被呼叫用户是否在线、被呼叫用户是否在通话中  呼叫用户是否欠费
     * */
    public void call(JSONObject jsonObject){
        JSONObject resultJson = new JSONObject();
        System.out.println(jsonObject.toString());
        int toid = jsonObject.getInteger("to");
        JSONObject dataJSON = jsonObject.getJSONObject("data");
        System.out.println("toid"+toid);
        System.out.println("是否存在"+connections.containsKey(toid));
        if (connections.containsKey(toid)&&connections.get(toid).status==0){//被呼叫用户是否在线、被呼叫用户是否在通话中
            System.out.println("进入了");
            if (callService.callYanZheng(this.id)){//用户不欠费
//                resultJson.put("from",this.id);
                //改变呼叫者状态
                this.status = toid;
                this.whoCall = this.id;
                //改变被呼叫者状态；
                connections.get(toid).status=this.id;
                connections.get(toid).whoCall=this.id;
                //消息发送给被呼叫者
                System.out.println("呼叫者"+id+" 被呼叫者"+toid);
                System.out.println("from name"+callService.findNameById(this.id));
                pushOne(JSONUtil.getSocketJSONObject(0,this.id+"请求与您通话",toid+"",callService.findNameById(this.id),dataJSON,"requestLink").toJSONString(),toid);
                return;
            }else{//欠费
                resultJson.put("code",-1);
                resultJson.put("message","用户欠费");
                resultJson.put("option","option2");
                pushOneSelf(resultJson.toJSONString());
                return;
            }
        }else{//不能通话
            System.out.println("不进入");
            resultJson.put("code",-1);
            if (connections.containsKey(toid)) resultJson.put("message","用户正在通话");
            else  resultJson.put("message","用户不在线");
            resultJson.put("option","option1");
            pushOneSelf(resultJson.toJSONString());
            return;
        }
    }

    /**发送给除开自己的所以用户*/
    public static void pushAllElseSelf(String msg,int id){
        for (Chat client : connections.values()) {
            if (client.id == id){
                continue;
            }
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                System.out.println("Chat Error: Failed to send message to client");
                connections.remove(client.id);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    System.out.println("关闭-----错误!");
                }
            }
        }
    }

    /**发送给所以用户*/
    public static void pushAll(String msg){
        for (Chat client : connections.values()) {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                System.out.println("Chat Error: Failed to send message to client");
                connections.remove(client.id);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    System.out.println("关闭-----错误!");
                }
            }
        }
    }

    /**发送给某个用户*/
    public static void pushOne(String msg, int id){
        Chat  client = connections.get(id);//推送专人
            try {
            client.session.getBasicRemote().sendText(msg);
            } catch (IOException e) {
                System.out.println("Chat Error: Failed to send message to client");
                connections.remove(client.id);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    System.out.println("关闭-----错误!");
                }
            }


    }

    /**发送给用户自己*/
    public  void pushOneSelf(String msg){
        try{
            System.out.println("发给自己"+id+msg);
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            try {
                this.session.close();
            } catch (IOException e1) {
                System.out.println("关闭-----错误!");
            }
        }


    }


}
