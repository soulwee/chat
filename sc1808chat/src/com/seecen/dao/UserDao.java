package com.seecen.dao;

import com.seecen.entity.User;
/**
 * 操作的bean的类名开头,DAO data access object
 * @author gudong
 *
 */
public interface UserDao {
	//登录
	public User login(int account,String pass);
	//更改状态
	public boolean updateFlag(int flag,int userId);
	//根据用户的id查询用户的详情
	public User findUser(int userId);
}
