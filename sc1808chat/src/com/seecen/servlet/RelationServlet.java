package com.seecen.servlet;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seecen.dao.GroupDao;
import com.seecen.dao.GroupDaoImpl;
import com.seecen.dao.RelationDao;
import com.seecen.dao.RelationDaoImpl;
import com.seecen.dao.UserDao;
import com.seecen.dao.UserDaoImpl;
import com.seecen.entity.Chat;
import com.seecen.entity.Group;
import com.seecen.entity.Request;
import com.seecen.entity.User;

import net.sf.json.JSONObject;
@WebServlet("/relationServlet")
public class RelationServlet extends HttpServlet{
	HttpServletRequest request;
	HttpServletResponse response;
	RelationDao relationDao=new RelationDaoImpl();
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		this.request=arg0;
		this.response=arg1;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String method=request.getParameter("method");
		if("searchUser".equals(method)){
			searchUser();
		}else if("add".equals(method)){
			add();
		}else if("sendRequest".equals(method)){
			sendRequest();
		}else if("requestTip".equals(method)){
			requestTip();
		}else if("queryAllRequest".equals(method)){
			queryAllRequest();
		}else if("agree1".equals(method)){
			agree1();
		}else if("reject".equals(method)){
			reject();
		}else if("deleteFriend".equals(method)){
			deleteFriend();
		}else if("moveFriend".equals(method)){
			moveFriend();
		}else if("history".equals(method)){
			history();
		}
	}
	
	private void history() {
		User user=(User)request.getSession().getAttribute("user");
		int recId=Integer.parseInt(request.getParameter("friendId"));
		String inDate=request.getParameter("indate");
		
		Chat c=new Chat();
		c.setRecId(recId);
		c.setSendId(user.getUserId());
		c.setInDate(inDate);
		int count=relationDao.queryHistoryCount(c);
		int size=10;
		int total=count%size==0?count/size:count/size+1;
		int index = Integer.parseInt(request.getParameter("pageIndex"));

		int pageIndex = index >= 1 && index <= total ? index : total;
		int start = (pageIndex - 1) * size + 1;
		int end = pageIndex * size;
		List<Chat> l=relationDao.queryHistory(c, start, end);
		User u=new UserDaoImpl().findUser(recId);
		JSONObject json=new JSONObject();
		json.put("chat", l);
		json.put("friend", u);
		json.put("user", user);
		json.put("nextDate", relationDao.nextDate(c));
		json.put("prevDate", relationDao.prevDate(c));
		json.put("pageIndex", pageIndex);
		json.put("total", total);

		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void moveFriend() {
		User user=(User)request.getSession().getAttribute("user");
		int groupId=Integer.parseInt(request.getParameter("groupId"));
		int userId=Integer.parseInt(request.getParameter("userId"));
		relationDao.moveFriend(userId, user.getUserId(), groupId);
	}

	
	private void deleteFriend() {
		User user=(User)request.getSession().getAttribute("user");
		int userId=Integer.parseInt(request.getParameter("userId"));
		relationDao.deleteFriend(userId, user.getUserId());
	}

	private void reject() {
		User user=(User)request.getSession().getAttribute("user");
		int sendId=Integer.parseInt(request.getParameter("sendId"));
		relationDao.rejectRequest(sendId, user.getUserId());
		
	}

	private void agree1() {
		User user=(User)request.getSession().getAttribute("user");
		int sendId=Integer.parseInt(request.getParameter("sendId"));
		int requestId=Integer.parseInt(request.getParameter("requestId"));
		int groupId=Integer.parseInt(request.getParameter("groupId"));
		relationDao.agreeRequest(sendId, user.getUserId(), requestId, groupId);
	}

	private void queryAllRequest() {
		User user=(User)request.getSession().getAttribute("user");
		List<Request> requestList=relationDao.getAllRequest(user.getUserId());
		//更改状态
		relationDao.updateRequestFlag(user.getUserId());
		JSONObject json=new JSONObject();
		json.put("requestList", requestList);
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void requestTip() throws IOException {
		User user=(User)request.getSession().getAttribute("user");
		List<Request> requestList=relationDao.getAllRequest(user.getUserId());
		for(Request request:requestList){
			if(request.getFlag()==0){
				response.getWriter().print("true");
				return;
			}
		}
		response.getWriter().print("false");
	}
	private void sendRequest() {
		User user=(User)request.getSession().getAttribute("user");
		int userId=user.getUserId();
		int recId=Integer.parseInt(request.getParameter("recid"));
		String message=request.getParameter("message");
		int groupId=Integer.parseInt(request.getParameter("groupId"));
		boolean flag=relationDao.sendRequest(userId,recId,groupId,message);
		try {
			response.getWriter().print(flag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void add() {
		int bid=Integer.parseInt(request.getParameter("bid"));
		//登录的user的信息
		User user=(User)request.getSession().getAttribute("user");
		GroupDao groupDao=new GroupDaoImpl();
		//得到自己的所有分组
		List<Group> groupList=groupDao.queryAllGroup(user.getUserId());
		//得到查询的用户的信息
		UserDao userDao =new UserDaoImpl();
		User friend =userDao.findUser(bid);
		JSONObject json=new JSONObject();
		json.put("username", user.getUserName());
		json.put("groupList", groupList);
		json.put("friend", friend);
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void searchUser() {
		String account=request.getParameter("account");
		User user=(User)request.getSession().getAttribute("user");
		JSONObject json=new JSONObject();
		json.put("userList", relationDao.queryNotFriend(user.getUserId(), account));
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
