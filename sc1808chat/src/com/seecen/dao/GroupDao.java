package com.seecen.dao;

import java.util.List;

import com.seecen.entity.Group;
import com.seecen.entity.User;

public interface GroupDao {
	//根据用户id查询用户的所有的分组的信息
	public List<Group> queryAllGroup(int userId);
	//根据分组的id查询分组下所有的好友关系的用户
	public List<User> queryGroupFriend(int groupId);
	//更新分组的名字
	public boolean updateGroupName(int groupId,String groupName);
	//添加分组
	public int addGroup(String groupName,int userId);
	//删
	public boolean deleteGroup(int groupId);
	
}
