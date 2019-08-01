package com.seecen.dao;

import java.util.List;

import com.seecen.entity.Chat;
import com.seecen.entity.Group;
import com.seecen.entity.Request;
import com.seecen.entity.User;

public interface RelationDao {
	//查询所有非好友的用户信息
	public List<User> queryNotFriend(int userId,String account);
	//发出请求并且建立单向关系
	public boolean sendRequest(int userId, int recId, int groupId, String message);
	//查看当前用户的被请求的消息
	public List<Request> getAllRequest(int userId);
	//更改请求状态为查看
	public boolean updateRequestFlag(int userId);
	//同意请求
	public boolean agreeRequest(int sendId,int recId,int requestId,int groupId);
	//拒绝
	public boolean rejectRequest(int sendId,int recId);
	//删除好友
	public boolean deleteFriend(int userId,int recId);
	//移动好友所在分组
	public boolean moveFriend(int userId,int recId,int groupId);
	//插入聊天记录
	public boolean insertChat(Chat c);
	//根据时间查询历史聊天记录,倒序排序
	public List<Chat> queryHistory(Chat c,int start,int end);
	//得到总记录数
	public int queryHistoryCount(Chat c);
	public String nextDate(Chat c);
	public String prevDate(Chat c);
}
