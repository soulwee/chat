$(function(){
	init();
});
function init(){
	$.get("relationServlet",{
		method:"queryAllRequest"	
	},function(data){
		var r=$.parseJSON(data).requestList;
		
		var str="";
		$.each(r,function(i,o){
			str+="<li><img src='"+o.sendUser.headImg+"' class='touxiang'/>	" +
				"<div class='info'><span class='username'>"+
				o.sendUser.userName+"</span><br/>";
			var year=new Date().getFullYear();
			var age=year-parseInt(o.sendUser.birthday.substring(0,4));
				
		str+="<span>"+o.sendUser.sex+"&nbsp;"+age+"岁</span><br/>" +
			"<span><font color='gray'>附加消息 ：</font>"+o.message+"</span>";
		if(o.flag==0||o.flag==1){
			str+="<div class='messagebox'><input type='button' " +
					"onclick='agree("+o.sendUser.userId+","+
					o.requestId+")' class='rbtn' value='同意'/>" +
			"<input type='button'" +
			"onclick='reject("+o.sendUser.userId+")' class='rbtn' value='拒绝'/></div></div></li>";
		}else if(o.flag==2){
			str+="<div class='messagebox' >已同意</div></div></li>";
		}else if(o.flag==3){
			str+="<div class='messagebox'>拒绝请求</div></div></li> ";
		}
		$(".h2>ul").empty().html(str);
		});
	});	
}
var sid,rid;//用来接收每次处理的这条请求人id和请求id

//点击同意按钮
function agree(sendId,requestId){
	sid=sendId;
	rid=requestId;
	//通过ajax获得自己所有的分组
	$.get("groupServlet",{method:"loadGroup"},function(data){
		var groupList=$.parseJSON(data).groupList;
		var str="";
		$.each(groupList,function(i,o){
			str+="<option value='"+o.groupId+"'>"+o.groupName+"</option>";
		});
		$("#mygroup").empty().html(str);
		$(".fenzu").css({
			top:"50%",
			left:"50%"
		}).show();
	});
}
//点击确定同意时
function agree1(){
	$.post("relationServlet",{
		method:"agree1",
		sendId:sid,
		requestId:rid,
		groupId:$("#mygroup").val()},
		function(data){
			$(".fenzu").hide();
	});
}
function reject(sendId){
	$.post("relationServlet",{
		method:"reject",
		sendId:sendId},
		function(data){
	});
}








