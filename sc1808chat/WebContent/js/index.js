$(function(){
	//让右边iframediv的高度和当前文档高度同等
	$(".left").css("height",$(window).height()-10);
	$(".right").css("height",$(window).height()-10);
	
	//好友悬停时改变背景
	$(".friend_group>li").mouseenter(function(){
		$(this).css("backgroundColor","rgb(240,240,240)");
	});
	//搜索好友获得焦点
	$(".search_text").focus(function(){
		$(this).css({
			backgroundColor:"white",
			backgroundImage:"url(image/search_icon2.png)",
			color:"black"
		}).val("");
		$(".close_search").show();
	});
	//搜索框失去焦点时
	$(".search_text").blur(function(){
		if($(this).val()==""){
			$(this).css({
				backgroundColor:"rgb(94,167,229)",
				backgroundImage:"url(image/search_icon.png)"
			});
			$(".close_search").hide();
		}
	});
	//X按钮点击时
	$(".close_search").click(function(){
		$(".search_text").val("");
		$(".search_text").trigger("blur");
	});
});

//改变颜色
function changeBgColor(obj){
	$(obj).css("backgroundColor","rgb(240,240,240)");
}
function changeBgColor1(obj){
	$(obj).css("backgroundColor","white");
}
//点击添加小按钮的时候
function add(){
	$("#myframe").attr("src","search.html");
}

$(function(){
	loadGroup();
});
function loadGroup(){
	$.get("groupServlet",{method:"loadGroup"},
			function(data){
		var groupList=$.parseJSON(data).groupList;
	//	alert(groupList);
		var str="";
		$.each(groupList,function(i,o){
			str+="<li><span oncontextmenu='rightMenu(this,"+o.groupId+")'" +
					" onclick='loadFriend(this,"+o.groupId+")' >"+o.groupName+
			"</span>&nbsp;&nbsp;<span>"+o.zxcount+"/"+o.count+"</span></li>";
		});
		$(".friend_group").html(str);
	});
}
//根据分组id查询这个组下的好友信息
function loadFriend(spanObject,groupId){
	
	//判断span相邻的下一个兄弟标签ul是否可见
	if($(spanObject).siblings("ul").is(":visible")){
		$(spanObject).siblings("ul").hide();
		$(spanObject).parent().css("backgroundImage","url(image/ysj.png)");
	}else{
	$.post("groupServlet",{method:"loadGroupFriend",groupId:groupId}
	,function(data){
		
		var userList=$.parseJSON(data).userList;
		/*//把原本的清除
		$(spanObject).siblings("ul").remove();*/
		var str='<ul>';
		$.each(userList,function(i,o){
			str+='<li ondblclick="openChat('+o.userId+')" oncontextmenu="rightMenu2(this,'+o.userId+','+groupId+')">';
			//alert(o.flag);
			if(o.flag==1){
				str+='<img src="'+o.headImg+'" class="friendimg"/>'
				+'<div><span style="color:red;">'+o.userName+'</span><br/>';
			}else{
				str+='<img src="'+o.headImg+'" class="imggray"/>'
				+'<div><span class="uName" style="color:gray;">'+o.userName+'</span><br/>';
			}
			str+='<span style="color:rgb(127,127,127)">'+o.sign+'</span>'
			+'</div></li>';
		});
		str+='</ul>';
		//把原本的清除
		$(spanObject).siblings("ul").remove();
		$(spanObject).parent().append(str);
		
		$(spanObject).siblings("ul").show();
		$(spanObject).parent().css("backgroundImage","url(image/xsj.png)");
	});
	}
}

//去掉dom自带的右键
window.document.oncontextmenu=function(){
	return false;
}
$(function(){
	//在任意一个地方点击左键把显示的右键菜单隐藏
	$(document).click(function(){
		$(".right_menu").hide();
		$(".right_menu2").hide();
	});
});

//给所有的分组绑定右键显示菜单事件
function rightMenu(spanObject,groupId){
	var e=window.event;
	$(".right_menu").css({
		top:e.clientY+"px",
		left:e.clientX+"px"
	}).show();
	//把点击的span,以及兄弟span以及传过来的分组id给全局
	span=spanObject;
	gid=groupId;
	nextSpan=$(spanObject).next("span");
}
var uid;
var lif;
var gid2;
function rightMenu2(spanObject,userId,groupId){
	uid=userId;
	lif=spanObject;
	gid2=groupId;
	var e=window.event;
	$(".right_menu2").css({
		top:e.clientY+"px",
		left:e.clientX+"px"
	}).show();
	
}
function hideGroup(){
	$(".group").hide();
}
function moveGroup(){
	$.get("groupServlet",{method:"loadGroup"},
			function(data){
		var groupList=$.parseJSON(data).groupList;
		var str="";
		$.each(groupList,function(i,o){
			if(o.groupId==gid2){
				str+="";
			}else{
			str+="<li onclick=moveFriend("+o.groupId+","+uid+")>"+o.groupName+"</li>";
			}
			});
		$(".group").empty().append(str).show();
	});
}
function moveFriend(groupId,uid){
	$.get("relationServlet",{
		method:"moveFriend",
		groupId:groupId,
		userId:uid
	},function(data){
		loadGroup();
		
	});
}
function deleteFriend(){
	$.get("relationServlet",{
		method:"deleteFriend",
		userId:uid
	},function(data){
		$(lif).remove();
		loadGroup();
	});
}

/**
 * 点击重命名li
 */
var span;//接收右键触发时选中的span对象
var nextSpan;//兄弟span
var gid;//同上

function rename(){
	//得到两个span的值
	var span1Html=$(span).html();
	$(span).parent().html("<input onblur='commitModifier(this)' " +
			"style='margin-left:13px' value='"+span1Html+"'/>")
			.find("input").select();
}
//改完名字后,失去焦点得到时候
function commitModifier(element){
	$.post("groupServlet",{method:"updateGroupName",
		groupId:gid,groupName:element.value},function(data){
			$(span).html(element.value);
			$(element).after(nextSpan).after(span).remove();
		});
}
		
function addGroup(){
	$(".friend_group").append("<li><input style='margin-left:13px'+" +
			" onblur='addGroup1(this.value)'/></li>");
	$(".friend_group li:last").find("input").focus();
}
function addGroup1(val){
	if(val==""){
		$(".friend_group li:last").find("input").val("新建分组");
		val="新建分组";
	}
	$.post("groupServlet",{method:"addGroup",groupName:val},function(data){
		//回调函数
	$(".friend_group li:last").html("<span oncontextmenu='rightMenu(this,"+data+")'" 
			+" onclick='loadFriend(this,"+data+")' >"+val+"</span><span>0/0</span>");
	});
}

function deleteGroup(){
	if($(nextSpan).html().split("/")[1]>0){
		alert("分组中有好友,不能删除!");
	}else{
		//删除分组根据分组id删除分组,ajax方法
		$.post("groupServlet",{method:"deleteGroup",
			groupId:gid},function(data){
				$(span).parent().remove();
				alert(data);
			});
	}
}
/*$(function(){
	//ajax轮询得到消息状态
	setInterval(function(){
		$.get("relationServlet",{method:"requestTip"},
				function(data){
			if(data=="true"){
				$("#tishi").attr("src","image/redtip.gif");
			}else{
				$("#tishi").attr("src","image/tip1.png");
			}
		});
	},1000);
});*/
//打开请求页面
function requestlist(){
	
	$("#tishi").attr("src","image/tip1.png");
	
	$("#myframe").attr("src","request.html");
}
//打开聊天窗口
function openChat(friendId){
	window.location.href="liaotian.jsp?friendId="+friendId;
	//$("#myframe").attr("src","liaotian.jsp?friendId="+friendId);
}
	





