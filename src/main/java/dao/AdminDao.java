package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import entity.Admin;
import util.DBUtil;

public class AdminDao {
	/*
	 * 根据账号查询管理员
	 */
	public Admin findByCode(String adminCode){
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String sql = "select * from admin_info_xy where admin_code=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, adminCode);
			ResultSet rs = ps.executeQuery();			
			//System.out.println(rs.next());
			//System.out.println("-------");
			if(rs.next()){
				//System.out.println("-------");
				Admin a = new Admin();
				a.setAdminCode(rs.getString("admin_code"));
				a.setAdminId(rs.getInt("admin_id"));
				a.setEmail(rs.getString("email"));
				a.setEnrolldate(rs.getTimestamp("enrolldate"));
				a.setName(rs.getString("name"));
				a.setPassword(rs.getString("password"));
				a.setTelephone(rs.getString("telephone"));	
				//System.out.println(a.getName());
				return a;
			}			
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			throw new RuntimeException("查询管理员失败");
			
		}finally{
			DBUtil.close(con);			
		}	
		
	}
	
	public static void main(String[] args) {
		AdminDao dao = new AdminDao();
		Admin a = dao.findByCode("caocao");
		System.out.println(a.getName());		
	}
		
}
