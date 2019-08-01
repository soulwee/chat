<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="UploadServlet" method="post" enctype="multipart/form-data">
	<input name="username" ><br/>
	<input name="password"><br/>
	<input type="file" name="headImg"><br/>
	<input type="submit">
	<a href="UploadServlet?fileName=0.jpg">0.jpg 下载</a>
</form>
</body>
</html>