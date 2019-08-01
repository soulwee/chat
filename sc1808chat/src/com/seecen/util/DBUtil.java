package com.seecen.util;

import java.sql.*;


public class DBUtil {

	private static final String USERNAME = "tt";
	private static final String PASSWORD = "1234";
	private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";
	private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";

	public static Connection getConnection(){
		Connection con =null;
		try {
			Class.forName(DRIVER);
			con=DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}
	
	//关闭方法
	public static void close(Connection con,Statement stmt,ResultSet rs){
		try {
			if(rs!=null){
				rs.close();
			}
			if(stmt!=null){
				stmt.close();
			}
			if(con!=null){
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 增删改
	 */
	public static boolean executeUpdate(String sql,
			Object[] param){
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = getConnection();
			pst = con.prepareStatement(sql);
			if(param!=null && param.length>0){
				for (int i = 0; i < param.length; i++) {
					pst.setObject(i+1, param[i]);
				}
			}
			return pst.executeUpdate()>0?true:false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			DBUtil.close(con,pst,null);
		}
		return false;
	}
	
	
}
