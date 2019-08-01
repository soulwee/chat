<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/admin.css"/>
<script src="js/jquery-3.2.1.min.js"></script>
<script>
	//表单提交事件
	function login(){
		//把文本框和文本框div内的内容拼接在一起
		var value=$("#uname_div").text()+$("#uname_div input").val();
		var element=$("#uname_div input");
		//清空原本的div,然后追加一个文本框,把拼接的内容放进去
		$("#uname_div").empty().append(element);
		$("#uname_div input").val(value);
		//目的是为了提交表单是传递给后台是完整的用户账号
		return true;
	}
	function loadimg(element){
		//拼接账号值
		var value=$("#uname_div").text()+$("#uname_div input").val();
		if(event.keyCode==8){//判断按键
			$("#uname_div").empty().append(element);
			$("#uname_div input").val(value).focus();
			$.get("userServlet",{method:"loadimg",account:value},function(data){
				var result = $.parseJSON(data);
				//后台是否返回数据
				if(result.account=="" || result.account!=value){
					$(".headimg").attr("src","image/laofu.jpg");
				}else{
					$("#uname_div input").val(value).focus();
					$(".headimg").attr("src",result.img);
				}	
			});
			return;
		}
		//触发ajax,传递账号值到后台
		$.get("userServlet",{method:"loadimg",account:value},function(data){
			var result=$.parseJSON(data);
			//后台是否返回数据
			if(result.account==""){
				$(".headimg").attr("src","image/laofu.jpg");
				return;
			}
			var after=result.account.substr(value.length);
			//  			清空		追加刚输入的内容			追加input
			$("#uname_div").empty().append(value).append(element);
			$("#uname_div").css({
				textIndent:"12px",
				color:"rgb(127,127,127)",
				fontSize:"12px"
			});//如果没有截取到要被选中的值就把所有的内容都给input
			if(after==""){
				$("#uname_div").empty().append(element);
				$("#uname_div input").val(value).focus();
			}else{
				$("#uname_div input").val(after).css({
					width:"auto",
					height:"31px",
					border:"none",
					outline:"none",
					textIndent:"0px",
					color:"rgb(127,127,127)",
					fontSize:"12px"
				}).select();
			}
			//改头像
			$(".headimg").attr("src",result.img);
		});
	}
</script>
</head>
<body>
	<div class="login_panel">
		<div class="top">
			SC Chat
		</div>
		<div class="login_div">
			<div>
				<img class="headimg" src="image/laofu.jpg"/>
				<form class="loginform" action = "login"  method="post"
				onsubmit="return login()">
					<div>
						<div id="uname_div"><input class="uname" onkeyup="loadimg(this)"  
						 name="account" placeholder="登录名" /></div>	
						<div><input type="password" class="upass" name="pass" placeholder="密码"/></div>
						<div style="font-size:10px; color:red;">${error }</div>
					</div>
					<input type="submit" class="login_btn" value="登录">
				</form>
				<div class="d3">
					<a href="#">注册用户</a>
					<a style="margin-top:12px;" href="#">忘记密码</a>
				</div>
			</div>
		</div>
	</div>
	
	
</body>
</html>