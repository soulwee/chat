package com.seecen.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.seecen.dao.UserDao;
import com.seecen.dao.UserDaoImpl;
import com.seecen.entity.User;
@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		int account=Integer.parseInt(req.getParameter("account"));
		String pass=req.getParameter("pass");
		UserDao userDao=new UserDaoImpl();
		User user=userDao.login(account, pass);
		if(user!=null){
			//登录成功,调用更改状态的方法
			userDao.updateFlag(1, user.getUserId());
			user.setFlag(1);
			req.getSession().setAttribute("user", user);
			//从application域中判断头像是否存在
			ServletContext context=req.getServletContext();
			Object object =context.getAttribute("headMap");
			Map<Integer,String> map=null;
			if(object!=null){
				map=(Map)object;	
			}else{
				map=new HashMap<Integer,String>();
			}
			map.put(user.getAccount(),user.getHeadImg());
			context.setAttribute("headMap", map);
			resp.sendRedirect("index.jsp");
		}else{
			req.setAttribute("error", "账户或密码错误");
			req.getRequestDispatcher("login.jsp").forward(req, resp);	
		}
	}
}
