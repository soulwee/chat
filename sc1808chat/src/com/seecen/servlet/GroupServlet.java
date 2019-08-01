package com.seecen.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seecen.dao.GroupDao;
import com.seecen.dao.GroupDaoImpl;
import com.seecen.entity.User;

import net.sf.json.JSONObject;
@WebServlet("/groupServlet")
public class GroupServlet extends HttpServlet{
	HttpServletRequest request;
	HttpServletResponse response;
	GroupDao groupDao=new GroupDaoImpl();
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		this.request=arg0;
		this.response=arg1;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String method=request.getParameter("method");
		if("loadGroup".equals(method)){
			loadGroup();
		}else if("loadGroupFriend".equals(method)){
			loadGroupFriend();
		}else if("updateGroupName".equals(method)){
			updateGroupName();
		}else if("addGroup".equals(method)){
			addGroup();
		}else if("deleteGroup".equals(method)){
			deleteGroup();
		}
	}
	private void deleteGroup() {
		int groupId=Integer.parseInt(request.getParameter("groupId"));
		boolean flag=groupDao.deleteGroup(groupId);
		try {
			response.getWriter().print(flag);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	private void addGroup() {
		User user=(User)request.getSession().getAttribute("user");
		String groupName=request.getParameter("groupName");
		int id=groupDao.addGroup(groupName, user.getUserId());
		try {
			response.getWriter().print(id);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	private void updateGroupName() {
		int groupId=Integer.parseInt(request.getParameter("groupId"));
		String groupName=request.getParameter("groupName");
		boolean flag=groupDao.updateGroupName(groupId, groupName);
		try {
			response.getWriter().print(flag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//根据分组id得到好友的分组信息
	private void loadGroupFriend() {
		int groupId=Integer.parseInt(request.getParameter("groupId"));
		JSONObject json=new JSONObject();
		json.put("userList", groupDao.queryGroupFriend(groupId));
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void loadGroup() {
		User user=(User)request.getSession().getAttribute("user");
		JSONObject json=new JSONObject();
		json.put("groupList", groupDao.queryAllGroup(user.getUserId()));
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
