package com.seecen.server;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.seecen.dao.RelationDao;
import com.seecen.dao.RelationDaoImpl;
import com.seecen.dao.UserDao;
import com.seecen.dao.UserDaoImpl;
import com.seecen.entity.Chat;

import net.sf.json.JSONObject;

/**
 * @ServerEndpoint 注解是一个类层次的注解，
 * 它的功能主要是将目前的类定义成一个websocket服务器端,
  * 注解的值将被用于监听用户连接的终端访问URL地址,
  * 客户端可以通过这个URL来连接到WebSocket服务器端
  */
//ws://ip:端口/工程名/call
//localhost:8080/工程/服务器地址/{id}/{name}/{age}
@ServerEndpoint("/qq/{userId}/{friendId}")
public class QQ {
	private int userId;//发出人的id
	private int friendId;//接收人的id
	private Session session;//存储当前的会话
	/**
     * 连接建立成功调用的方法
     * @param session  可选的参数。
     * session为与某个客户端的连接会话，
     * 需要通过它来给客户端发送数据
     */
	public int hashCode(){
		return 1;
	}
	public boolean equals(Object obj){
		if(obj instanceof QQ){
			QQ qq=(QQ)obj;
			if(qq.userId==this.userId && qq.friendId==this.friendId){
				return true;
			}
		}
			return false;
	}
	
	//存储所有正在会话的用户的服务器实例对象
	static Set<QQ> chatSet = new HashSet<QQ>();
   
	@OnOpen
	public void onOpen(Session session,
			@PathParam("userId")int userId,
			@PathParam("friendId")int friendId){
    		this.userId = userId;
    		this.friendId = friendId;
    		this.session = session;
    		//加入会话set中
    		chatSet.add(this);
    		
	}
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
    	chatSet.remove(this);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @throws IOException 
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
       System.out.println("发送的信息为:"+message);
       //声明一个局部变量,用来接收接收方的session
       Session si =null;
       
       Chat c=new Chat();
       c.setSendId(userId);
       c.setRecId(friendId);
       c.setMessage(message);
       //set中每个实例的session都表示一个会话1-3
       for(QQ qq: chatSet){
    	   //只需要找到一个friendId和当前用户id一致,userId和当前friendId一致
    	   if( qq.userId==this.friendId&&qq.friendId==this.userId){
    		   //就把你发送的消息传给对方
    		   c.setFlag(1);//把消息状态改为1,已读
    		   si=qq.session;//把这个人的session得到
    		   break;
    	   }
       }
       //调用dao层的方法插入聊天记录
       RelationDao relationDao=new RelationDaoImpl();
       relationDao.insertChat(c);
       
       UserDao userDao=new UserDaoImpl();
       JSONObject json=new JSONObject();
       //找到发送人的对象信息
       json.put("user", userDao.findUser(userId));
       json.put("message", message);
       si.getBasicRemote().sendText(json.toString());
    }
    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }
}
