$(function(){
	init("");
});
function init(val){
	$.get("relationServlet",{method:"searchUser",account:val},function(data){
		var userList=$.parseJSON(data).userList;
		var str="";
		$.each(userList,function(i,o){
			str+="<li><img class='headimg' src='"+o.headImg+"'/>" +
					"<span class='account'>"+o.account+
					"&nbsp;<span class='nicheng'>"+o.userName+"</span></span>";
			var year=new Date().getFullYear();
			str+="<span class='age'>"+(year-parseInt(o.birthday.substring(0,4)))+"岁</span>";
			if(o.sex=="男"){
				str+="<img class='sex' src='image/nan.png'/>";
			}else{
				str+="<img class='sex' src='image/nv.png'/>";
			}
			str+="<div class='add' onclick='add("+o.userId+")'>好友</div></li>";
		});
		$(".search_panel").empty().html(str);
	});
}

function add(id){
	$.get("relationServlet",{method:"add",bid:id},function(data){
		var result=$.parseJSON(data);
		$(".toolbar>span:eq(0)").html(result.username+" -添加好友");
		$(".userimg").attr("src",result.friend.headImg);

		$(".p1>span:eq(0)").html(result.friend.userName);
		$(".p1>span:eq(1)").html(result.friend.account);
		$(".p2>span:eq(0)").html("性别:"+result.friend.sex);
		var year=new Date().getFullYear();
		var age=year-parseInt(result.friend.birthday.substring(0,4));
		$(".p2>span:eq(1)").html("年龄:"+age);
		$(".groupsel").empty();
		$.each(result.groupList,function(i,o){
			$(".groupsel").append("<option value='"+o.groupId+"'>"+o.groupName+"</option>")
		});
		//显示窗口
		$(".request_panel").show();
		//把查询的用户的id放在隐藏域中
		$("#recid").val(result.friend.userId);
	});
}
//关闭窗口
function closeRequest(){
	$(".request_panel").hide();
}
//发送请求
function sendRequest(){
	$.post("relationServlet",{
		method:"sendRequest",
		recid:$("#recid").val(),
		message:$(".fjinfo").val(),
		groupId:$(".groupsel").val()
	},
	function(data){
		if(data=="true"){
			alert("发送请求成功!");
		}else{
			alert("发送请求失败!");
		}
		closeRequest();
	});
}
function closeBig(){
	$(".big").hide();
}







