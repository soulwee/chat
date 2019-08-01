package com.seecen.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class UploadServlet
 */
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		// 获取图片名
		String filename = request.getParameter("fileName");
		System.out.println(filename);
		filename = new String(filename.getBytes("iso-8859-1"), "utf-8");
		System.out.println(filename);
		// 得到向客户端输出的输出流
		OutputStream outputStream = response.getOutputStream();
		// 输出文件用的字节数组，每次向输出流发送1024个字节
		byte[] b = new byte[1024];
		// 要下载的文件
		//g.//TOMCAT/sc1803chat/upload/jiaoyou.sql
		File fileload = new File(request
				.getRealPath(File.separator + "/upload"), filename);
		// 客服端使用保存文件的对话框
		response.setHeader("Content-disposition", "attachment;filename="
				+ URLEncoder.encode(filename, "utf-8"));
		// 通知客服文件的MIME类型
		response.setContentType("application/x-msdownload");
		// 通知客服文件的长度
		long fileLength = fileload.length();
		String length = String.valueOf(fileLength);
		response.setHeader("Content_length", length);
		// 读取文件，并发送给客服端下载
		FileInputStream inputStream = new FileInputStream(fileload);

		int n = 0;
		while ((n = inputStream.read(b)) != -1) {
			outputStream.write(b, 0, n);
		}
		outputStream.flush();
		outputStream.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map map =upload(request,response);
		
		String username = map.get("username").toString();
		String passsword = map.get("password").toString();
		String headImg = map.get("headImg").toString();
		
		
	}
	// 上传
		private Map upload(HttpServletRequest request, HttpServletResponse response) throws IOException {
			PrintWriter out = response.getWriter();
			// 将每个单独上传的文件封装到磁盘
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 得到要保存上传文件的绝对路径，D:\apache-tomcat-6.0.35\webapps\sc1604Email/uoload
			String path1 = File.separator;
			
			System.out.println(path1);
			
			//指定要把资源上传到服务器的项目中的某个位置
			String path = request.getRealPath(path1 + "/upload");
			System.out.println(path);
			// 设置是否将上传文件以临时文件的形式保存在磁盘的临界值，默认值10KB。这里设置为1M
			// 这里表示每次文件读写都是1M一次，即每次读写1M
			factory.setSizeThreshold(1024 * 1024);
			// 超过1M时，则存放到临时文件夹中
			factory.setRepository(new File(path));
			// 将磁盘文件加载到上传中
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 每一个上传的文件不能超过2M
			upload.setFileSizeMax(1024 * 1024 * 2);
			// 所有上传的文件总共加起来不能超过2M
			// upload.setSizeMax(1024 * 1024 * 2);
			// 从请求中获取所有上传文件，即获取页面表单内所有的元素的value值
			List<FileItem> list = new ArrayList<FileItem>();
			try {
				list = upload.parseRequest(request);
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 对所有请求中的上传文件进行上传
			Map<String, Object> map = new HashMap<String, Object>();
			String fileName = "";
			//
			for (FileItem item : list) {
				String name = item.getFieldName();
				if (item.isFormField()) {
					String value = item.getString();
					value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
					map.put(name, value);
				} else {
					String value = item.getName();
					if (null != value && value.length() > 0) {
						int start = value.lastIndexOf(File.separator);
						fileName = value.substring(start + 1);
						File file = new File(path, fileName);
						try {
							item.write(file);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					map.put(name, fileName);
				}
			}
			return map;
		}

}
