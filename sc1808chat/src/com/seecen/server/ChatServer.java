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

/**
 * @ServerEndpoint 注解是一个类层次的注解，
 * 它的功能主要是将目前的类定义成一个websocket服务器端,
  * 注解的值将被用于监听用户连接的终端访问URL地址,
  * 客户端可以通过这个URL来连接到WebSocket服务器端
  */
//ws://ip:端口/工程名/call
//localhost:8080/工程/服务器地址/{id}/{name}/{age}
@ServerEndpoint("/chat/{name}")
public class ChatServer {
	private String name;//表示登录进来的人的名字
	private Session session;//存储当前的会话
	/**
     * 连接建立成功调用的方法
     * @param session  可选的参数。
     * session为与某个客户端的连接会话，
     * 需要通过它来给客户端发送数据
     */
	//存储所有正在会话的用户的服务器实例对象
	static Set<ChatServer> chatSet = new HashSet<ChatServer>();
    @OnOpen
	public void onOpen(Session session,
			@PathParam("name")String username){
    		this.name = username;
    		this.session = session;

    		chatSet.add(this);
    		for(ChatServer c:chatSet){
    			try {
    				//后台发送数据回前台
					c.session.getBasicRemote().
					sendText(username+"加入了群聊<br/>");
				} catch (IOException e) {
					e.printStackTrace();
				}
    		}
	}
    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
    	
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     * @throws IOException 
     */
    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
       System.out.println(name+"发送了"+message);
       for(ChatServer c:chatSet){
			try {
				//后台发送数据回前台
				c.session.getBasicRemote()
				.sendText(name+"说:"+message+"<br/>");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
