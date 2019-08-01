package com.seecen.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.seecen.entity.Group;
import com.seecen.entity.User;
import com.seecen.util.DBUtil;

public class GroupDaoImpl implements GroupDao{

	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	@Override
	public List<Group> queryAllGroup(int userId) {
		String sql="select groupid,groupname,"
				+ " (select count(*) from c_relation "
				+ " where groupid=g.groupid and flag=1) count,"
				+ " (select count(*) from c_relation r,c_user u "
				+ " where groupid=g.groupid and u.userid = r.bid "
				+ " and u.flag=1 and r.flag=1) zxcount"
				+ " from c_group g where userid = ? order by groupid";
		List<Group> list=new ArrayList<Group>();
		try {
			con=DBUtil.getConnection();
			pst=con.prepareStatement(sql);
			pst.setInt(1, userId);
			rs=pst.executeQuery();
			while(rs.next()){
				Group g=new Group();
				g.setCount(rs.getInt("count"));
				g.setZxcount(rs.getInt("zxcount"));
				g.setGroupId(rs.getInt("groupId"));
				g.setGroupName(rs.getString("groupName"));
				g.setUserId(userId);
				list.add(g);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<User> queryGroupFriend(int groupId) {
		String sql="select * from c_user where userid in("
				+ " select bid"
				+ " from c_relation where groupid=? and flag=1) order by flag desc";
		List<User> list=new ArrayList<User>();
		try {
			con=DBUtil.getConnection();
			pst=con.prepareStatement(sql);
			pst.setInt(1, groupId);
			rs=pst.executeQuery();
			while(rs.next()){
				User user=new User();
				user.setAccount(rs.getInt("account"));
			//	user.setBirthday(rs.getDate("birthday"));
				user.setHeadImg(rs.getString("headimg"));
				user.setSex(rs.getString("sex"));
				user.setSign(rs.getString("sign"));
				user.setUserId(rs.getInt("userid"));
				user.setFlag(rs.getInt("flag"));
				user.setUserName(rs.getString("username"));
				list.add(user);
				
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public boolean updateGroupName(int groupId, String groupName) {
		String sql="update c_group set groupname=? where groupid=?";
		return DBUtil.executeUpdate(sql, new Object[]{groupName,groupId});
	}
	@Override
	public int addGroup(String groupName, int userId) {
		Connection con=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		String sql="insert into c_group values(c_group_seq.nextval,?,?)";
		try {
			con=DBUtil.getConnection();
			pst=con.prepareStatement(sql);
			pst.setString(1, groupName);
			pst.setInt(2, userId);
			if(pst.executeUpdate()>0){
				//分组插入后我们需要查询序列的自增值
				sql="select c_group_seq.currval from dual";
				pst=con.prepareStatement(sql);
				rs=pst.executeQuery();
				rs.next();
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	@Override
	public boolean deleteGroup(int groupId) {
		String sql="delete from c_group where groupId=?";
		
		return DBUtil.executeUpdate(sql, new Object[]{groupId});
	}

}
