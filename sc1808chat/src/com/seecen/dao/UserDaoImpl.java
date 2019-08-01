package com.seecen.dao;

import java.sql.*;

import com.seecen.entity.User;
import com.seecen.util.DBUtil;

public class UserDaoImpl implements UserDao{

	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	@Override
	public User login(int account, String pass) {
		String sql="select * from c_user where account=? and pass=?";
		con=DBUtil.getConnection();
		try {
			pst=con.prepareStatement(sql);
			pst.setInt(1, account);
			pst.setString(2, pass);
			rs=pst.executeQuery();
			while(rs.next()){
				User user=new User();
				user.setAccount(rs.getInt("account"));
				user.setBirthday(rs.getString("birthday"));
				user.setHeadImg(rs.getString("headimg"));
				user.setPass(pass);
				user.setSex(rs.getString("sex"));
				user.setSign(rs.getString("sign"));
				user.setUserId(rs.getInt("userid"));
				user.setUserName(rs.getString("username"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateFlag(int flag, int userId) {
		String sql="update c_user set flag=? where userid=?";
		return DBUtil.executeUpdate(sql, new Object[]{flag,userId});
		
	}

	@Override
	public User findUser(int userId) {
		String sql="select * from c_user where userid=?";
		con=DBUtil.getConnection();
		try {
			pst=con.prepareStatement(sql);
			pst.setInt(1, userId);
			rs=pst.executeQuery();
			while(rs.next()){
				User user=new User();
				user.setAccount(rs.getInt("account"));
				user.setBirthday(rs.getString("birthday"));
				user.setHeadImg(rs.getString("headimg"));
				user.setSex(rs.getString("sex"));
				user.setSign(rs.getString("sign"));
				user.setUserId(rs.getInt("userid"));
				user.setUserName(rs.getString("username"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
