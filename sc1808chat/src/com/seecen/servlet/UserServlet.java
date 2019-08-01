package com.seecen.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
@WebServlet("/userServlet")
public class UserServlet extends HttpServlet{
	HttpServletRequest request;
	HttpServletResponse response;
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1) throws ServletException, IOException {
		this.request=arg0;
		this.response=arg1;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String method=request.getParameter("method");
		if("loadimg".equals(method)){
			loadimg();
		}
	}
	private void loadimg() {
		String account=request.getParameter("account");
		Map<Integer,String> map=(Map)request.getServletContext().getAttribute("headMap");
		String accounts="",img="";
		if(map!=null){
			for(Integer key:map.keySet()){
				String k=key.toString();
				if(k.startsWith(account)){
					accounts=k;
					img=map.get(key);
					break;
				}
			}
		}
		JSONObject json=new JSONObject();
		json.put("account",accounts);
		json.put("img", img);
		try {
			response.getWriter().print(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
