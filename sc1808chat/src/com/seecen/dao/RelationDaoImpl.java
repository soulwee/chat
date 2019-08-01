package com.seecen.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.seecen.entity.Chat;
import com.seecen.entity.Request;
import com.seecen.entity.User;
import com.seecen.util.DBUtil;

public class RelationDaoImpl implements RelationDao {

	Connection con;
	PreparedStatement pst;
	ResultSet rs;
	@Override
	public List<User> queryNotFriend(int userId, String account) {
		String sql="select * from c_user where userid not in"
				+ " (select bid from c_relation "
				+ " where aid=? and flag=1 )"
				+ " and ( account like ? or username like ?)";
		List<User> list=new ArrayList<User>();
		try {
			con=DBUtil.getConnection();
			pst=con.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setString(2, "%"+account+"%");
			pst.setString(3, "%"+account+"%");
			rs=pst.executeQuery();
			while(rs.next()){
				User user=new User();
				user.setAccount(rs.getInt("account"));
				user.setBirthday(rs.getString("birthday"));
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
	public boolean sendRequest(int userId, int recId, int groupId, String message) {
		try {
			con=DBUtil.getConnection();
			con.setAutoCommit(false);//关闭事务自动提交
			//判断对方是否存在当前用户发出的请求,并且未处理
			String sql="select count(*) from c_request where sendid=? and recid=?"
					+ "and flag in(0,1)";
			pst=con.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, recId);
			rs=pst.executeQuery();
			rs.next();
			if(rs.getInt(1)==0){
				sql="insert into c_request values(c_request_seq.nextval,sysdate,?,?,?,0)";
				pst=con.prepareStatement(sql);
				pst.setInt(1, userId);
				pst.setInt(2, recId);
				pst.setString(3, message);
				pst.executeUpdate();
			}else{
				sql="update c_request set indate=sysdate,"
						+ "message=?,flag=0 where sendid=? and recid=?";
				pst=con.prepareStatement(sql);
				pst.setString(1, message);
				pst.setInt(2, userId);
				pst.setInt(3, recId);
				pst.executeUpdate();
			}
			//查看是否存在单向的关系
			sql="select count(*) from c_relation where aid=? and bid=?";
			pst=con.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, recId);
			rs=pst.executeQuery();
			rs.next();
			if(rs.getInt(1)==0){
				sql="insert into c_relation values(?,?,0,?)";
				pst=con.prepareStatement(sql);
				pst.setInt(1, userId);
				pst.setInt(2, recId);
				pst.setInt(3, groupId);
				pst.executeUpdate();
			}else{
				sql="update c_relation set groupid=? where aid=? and bid=?";
				pst=con.prepareStatement(sql);
				pst.setInt(1, groupId);
				pst.setInt(2, userId);
				pst.setInt(3, recId);
				pst.executeUpdate();
			}
			con.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return false;
	}
	@Override
	public List<Request> getAllRequest(int userId) {
		String sql="select * from c_request where recid=? order by indate desc";
		List<Request> list=new ArrayList<Request>();
		UserDao userDao=new UserDaoImpl();
		try {
			con=DBUtil.getConnection();
			pst=con.prepareStatement(sql);
			pst.setInt(1, userId);
			rs=pst.executeQuery();
			while(rs.next()){
				Request r=new Request();
				r.setFlag(rs.getInt("flag"));
				r.setInDate(rs.getString("indate"));
				r.setMessage(rs.getString("message"));
				r.setRecId(rs.getInt("recid"));
				r.setRequestId(rs.getInt("requestid"));
				r.setSendId(rs.getInt("sendid"));
				r.setSendUser(userDao.findUser(rs.getInt("sendid")));
				list.add(r);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public boolean updateRequestFlag(int userId) {
		String sql="update c_request set flag=1 where recid=? and flag=0";
		return DBUtil.executeUpdate(sql, new Object[]{userId});
	}
	@Override
	public boolean agreeRequest(int sendId, int recId, int requestId, int groupId) {
		try {
				con=DBUtil.getConnection();
				con.setAutoCommit(false);
				String sql="update c_request set flag=2 where requestid=?";
				pst=con.prepareStatement(sql);
				pst.setInt(1, requestId);
				pst.executeUpdate();
				//插入
				sql="insert into c_relation values(?,?,1,?)";
				pst=con.prepareStatement(sql);
				pst.setInt(1, recId);
				pst.setInt(2, sendId);
				pst.setInt(3, groupId);
				pst.executeUpdate();
				//更新
				sql="update c_relation set flag=1 where bid=? and aid=?";
				pst=con.prepareStatement(sql);
				pst.setInt(1, recId);
				pst.setInt(2, sendId);
				pst.executeUpdate();
				con.commit();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return false;
	}
	@Override
	public boolean rejectRequest(int sendId, int recId) {
		try {
			con=DBUtil.getConnection();
			con.setAutoCommit(false);
			String sql="delete from c_relation  where aid=? and bid=?";
			pst=con.prepareStatement(sql);
			pst.setInt(1, sendId);
			pst.setInt(2, recId);
			pst.executeUpdate();
			sql="update c_request set flag=3 where sendid=? and recid=?";
			pst=con.prepareStatement(sql);
			pst.setInt(1, sendId);
			pst.setInt(2, recId);
			pst.executeUpdate();
			con.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean deleteFriend(int userId, int recId) {
		String sql="delete from c_relation where aid=? and bid=?";
		try {
			con=DBUtil.getConnection();
			pst=con.prepareStatement(sql);
			pst.setInt(1, userId);
			pst.setInt(2, recId);
			pst.executeQuery();
			sql="delete from c_relation where aid=? and bid=?";
			pst=con.prepareStatement(sql);
			pst.setInt(2, userId);
			pst.setInt(1, recId);
			pst.executeQuery();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean moveFriend(int userId, int recId, int groupId) {
		String sql="update c_relation  set groupid=? where aid=? and bid=?";
		return DBUtil.executeUpdate(sql, new Object[]{groupId,recId,userId});
	}
	@Override
	public boolean insertChat(Chat c) {
		String sql="insert into c_chat values(c_chat_seq.nextval,?,?,sysdate,?,?)";
		return DBUtil.executeUpdate(sql, new Object[]{c.getSendId(),c.getRecId(),c.getFlag(),c.getMessage()});
	}
	@Override
	public List<Chat> queryHistory(Chat c,int start,int end) {
		

	    List<Chat> l=new ArrayList<Chat>();
		String sql=" select * from (select s.*,rownum rn from"
				+ " (select * from c_chat where"
				+ "   sendid in(?,?) and recid in(?,?) "
				+ "  and to_char(indate,'yyyy-MM-dd')=?"
				+ " order by indate ) s)"
				+ " where rn between ? and ? ";
			con=DBUtil.getConnection();
		try {
			pst=con.prepareStatement(sql);
			
			pst.setInt(1, c.getSendId());
			pst.setInt(2, c.getRecId());
			pst.setInt(3, c.getSendId());
			pst.setInt(4, c.getRecId());
			pst.setString(5, c.getInDate());
			pst.setInt(6,start );
			pst.setInt(7,end);
			ResultSet rs=pst.executeQuery();
		    while(rs.next()){
		    	Chat chat=new Chat();
		    	chat.setInDate(rs.getString("indate"));
		    	chat.setMessage(rs.getString("message"));
		    	chat.setRecId(rs.getInt("recid"));
		    	chat.setSendId(rs.getInt("sendid"));
		    	chat.setFlag(rs.getInt("flag"));
		    	chat.setChatId(rs.getInt("chatid"));
		    	l.add(chat);
		    }
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return l;
	}
	@Override
	public int queryHistoryCount(Chat c) {
		String sql="select count(*) from c_chat where"
				+ "    sendid in(?,?) and recid in(?,?)  "
				+ "  and to_char(indate,'yyyy-MM-dd')=?"
				+ " order by indate ";
		con=DBUtil.getConnection();
		try {
			pst=con.prepareStatement(sql);
			
			pst.setInt(1, c.getSendId());
			pst.setInt(2, c.getRecId());
			pst.setInt(3, c.getSendId());
			pst.setInt(4, c.getRecId());
			pst.setString(5, c.getInDate());
			ResultSet rs=pst.executeQuery();
			rs.next();
			return rs.getInt(1);
			}catch (SQLException e) {
				e.printStackTrace();
			}
		return 0;
		}
	@Override
	public String nextDate(Chat c) {
		String sql="select to_Char(min(indate),'yyyy-MM-dd') from c_Chat where "
				+ "((sendid=? and recid =?) or "
				+ "(sendid=? and recid =?)) "
				+" and to_char(indate,'yyyy-MM-dd')>?";
		try {
			con=  DBUtil.getConnection();
			pst=con.prepareStatement(sql);
			pst.setInt(1, c.getSendId());
			pst.setInt(2, c.getRecId());
			pst.setInt(4, c.getSendId());
			pst.setInt(3, c.getRecId());
			pst.setString(5, c.getInDate());
			rs= pst.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String prevDate(Chat c) {
		String sql="select to_Char(max(indate),'yyyy-MM-dd') from c_Chat where "
				+ "((sendid=? and recid =?) or "
				+ "(sendid=? and recid =?)) "
				+" and to_char(indate,'yyyy-MM-dd')<?";
		try {
			con=  DBUtil.getConnection();
			pst=con.prepareStatement(sql);
			pst.setInt(1, c.getSendId());
			pst.setInt(2, c.getRecId());
			pst.setInt(4, c.getSendId());
			pst.setInt(3, c.getRecId());
			pst.setString(5, c.getInDate());
			rs= pst.executeQuery();
			rs.next();
			return rs.getString(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
