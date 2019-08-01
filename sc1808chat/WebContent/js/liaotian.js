

/* 时间插件 */
$(function() {
	var mySchedule = new Schedule({
		el : '#schedule-box',
		// date: '2018-9-20',
		clickCb : function(y, m, d) {
			$(".shijian").html('' + y + '-' + format(m) + '-' + format(d));
		},
		nextMonthCb : function(y, m, d) {
		},
		nextYeayCb : function(y, m, d) {
		},
		prevMonthCb : function(y, m, d) {
		},
		prevYearCb : function(y, m, d) {
		}
	});
});
// 监听回车事件
$(function() {
	$(window).keydown(function(e) {
		// 回车
		if (e.keyCode == 13) {
			sendMessage();//调用发送消息
			return false
		}
		// 回车和shift同时按下
		if (13 == e.keyCode && e.shiftKey) {
			return false;// 禁止回车换行
		}
	});
});
$(function(){
	var date = new Date();
	$(".shijian").html(
			date.getFullYear()+"-"
			+format((date.getMonth()+1))+"-"
			+format(date.getDate())
			);
});
function format(month){
	return  parseInt(month)<10?"0"+month:month;
}

//点击历史
function showHistory() {
	if ($(".history").is(':visible')) {
		//隐藏
		$("#window").css("width", "555px");
		$(".history").hide();
		$(".history_btn").css("backgroundColor", "white");
	} else {
		//调用
		//触发一个ajax函数
		historyAjax(-1);
		$("#window").css("width", "920px");
		$(".history").show();
		$(".history_btn").css("backgroundColor", "rgb(241,241,241)");
	}
}

var nextDate,prevDate;

function prev(pageIndex,total){
	if(pageIndex==0){//判断是否没有上一页数据了
		if(prevDate!=undefined){//判断是否有上一个日期的记录
			//回到前一天的聊天记录
			var date = new Date(prevDate);
			
			$(".shijian").html(date.getFullYear()+"-"
					+format((date.getMonth()+1))+"-"
					+format(date.getDate()));
			pageIndex=-1;
		}else{
			pageIndex=1;
		}
	}
	historyAjax(pageIndex);
}
function next(pageIndex,total){//点击下一页
	if(pageIndex>total){//判断是否没有下一页数据了
		if(nextDate!=undefined){//判断是否有下一个日期的记录
			//回到前一天的聊天记录
			var date = new Date(nextDate);
			
			$(".shijian").html(date.getFullYear()+"-"
					+format((date.getMonth()+1))+"-"
					+format(date.getDate()));
			pageIndex=1;
		}else{
			pageIndex=-1;
		}
	}
	historyAjax(pageIndex);
}


//ajax查看历史
function historyAjax(pageIndex){
//	alert(pageIndex);
	$.post("relationServlet",
			{
			method:"history",
			indate:$(".shijian").html(),
			pageIndex:pageIndex,
			friendId:friendId
			}
		,function(data){
			//json
			var result = $.parseJSON(data);
			//上一个时间和下一个时间
			nextDate = result.nextDate;
			prevDate = result.prevDate;
			
			var user = result.user;
			var chatList = result.chat;
			//当前日的数据不存在
			if(chatList.length==0){
				prev(result.pageIndex,result.total);
				return;
			}
			var friend = result.friend;
			var str ="";
			$.each(chatList,function(i,o){
				if(o.sendId==friendId){
					str+="<li class='blue'>";
					str+="<div>"+friend.userName+" &nbsp;";
				}else{
					str+="<li class='green'>";
					str+="<div>"+user.userName+"&nbsp;";
					
				}
				str+="<span>"+o.inDate+"</span></div>";
				str+="<div>"+o.message+"</div></li>";
			});
			$(".history_list>ul").empty().html(str);
			
			str="";
			str+="<div class='shouye' onclick='historyAjax(1)'></div>";
			str+="<div class='shangyiye' onclick='prev("+(result.pageIndex-1)+","+result.total+")'></div>";
			str+="<div class='xiayiye' onclick='next("+(result.pageIndex+1)+","+result.total+")'></div>";
			str+="<div class='weiye' onclick='historyAjax("+(result.total)+")'></div>";
			$(".shijian").html(result.indate);
			$(".page").html(str);
		});
	}


// 点击日历
function boxshow() {
	if ($("#schedule-box").is(':hidden')) {
		$("#schedule-box").show();
	} else {
		$("#schedule-box").hide();
	}
}