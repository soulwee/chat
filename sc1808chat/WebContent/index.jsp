<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/index.css" />
<script src="js/jquery-3.2.1.min.js"></script>
<script src="js/index.js"></script>

<style>
</style>

</head>
<body>
	<div class="left">
		<div class="person_info">
			<img src="${user.headImg }" class="headimg" />
			<div>
				<h4>${user.userName }</h4>
				<span class="qianming">${user.sign }</span>
				<div style="margin-top: 5px; position: relative;">
					<img src="image/addfa.png" onclick="add()" class="addfd" /> <img
						src="image/tip1.png" onclick="requestlist()" id="tishi"
						style="margin-left: 20px;" class="addfd" /> <img
						src="image/message1.png" id="xiaoxi" style="margin-left: 20px;"
						class="addfd" />
				</div>
			</div>
		</div>
		<!-- 个人信息结束 -->
		<div class="user_search" style="position: relative;">
			<input class="search_text" 
			onkeyup="searchFriend(this.value)" value="搜索" />
			<div class="close_search">✖</div>
		</div>
		<script>
            //好像少了什么
			function searchFriend(val){
				if(val==""){
					init();
				}else{
				$.post("UserServlet",{
					method:"queryFriend",
					keyword:val
				},
				function(data){
					var userList = $.parseJSON(data).userList;
					var str = "<li><ul>";
					$.each(userList, function(index, u) {
						str += "<li>";
						if (u.flag == 1) {
							str += "<img src='"+u.img+"' class='friendimg'/>";
						} else {
							str += "<img src='"+u.img+"' class='imggray'/>";
						}
						str += "<div>";
						str += "<span style='color:red;'>" + u.userName
								+ "</span><br/>";
						str += "<span style='color:rgb(127,127,127)'>" + u.sign
								+ "</span>";
						str += "</div></li>";
					});
					str += "</ul></li>";
					$(".friend_group").empty().append(str);
					$(".friend_group ul").show();
				});
				}
			}
		
		</script>
		<!-- 搜索结束 -->
		<div class="head3">
			<div></div>
			<div></div>
		</div>
		<!-- tab结束 -->
		<div class="friendlist">
			<ul class="friend_group">
				<!--  <li>
					<span>我的好友&nbsp;&nbsp;5/102</span>
					<ul style="display:block">
						<li>
							<img src="upload/headimg/1.jpg" class="friendimg"/>
							<div>
								<span style="color:red;">李狗蛋</span><br/>
								<span style="color:rgb(127,127,127)">冬天最开心的就是我在被子里asdasdsada哈哈哈的事发生的佛挡杀佛斯蒂芬森的第三方</span>
							</div>
						</li>
                        <li>
							<img src="upload/headimg/1.jpg" class="imggray"/>
							<div>
								<span style="color:red;">李狗蛋</span><br/>
								<span style="color:rgb(127,127,127)">冬天最开心的就是我在被子里asdasdsada哈哈哈的事发生的佛挡杀佛斯蒂芬森的第三方</span>
							</div>
						</li>
					</ul>
				</li> --> 

			</ul>
		</div>
	</div>
	<div class="right" style="width:60%;">
		<iframe name="myframe" scrolling="no" id="myframe" frameborder="0"
			src="" style="width: 100%; height: 100%;"> </iframe>
	</div>
<!-- 菜单 -->
	<ul class="right_menu">
		<li onclick="rename()">重命名</li>
		<li onclick="deleteGroup()">删除分组</li>
		<li onclick="addGroup()">添加分组</li>
	</ul>
<!-- 菜单2 -->
	<ul class="right_menu2">
		<li class="move" onmouseenter="moveGroup()" onmouseleave="hideGroup()">移动联系人至 >
		<ul class="group" ></ul>
		</li>
		<li onclick="deleteFriend()">删除好友</li>
	</ul>
</body>
</html>