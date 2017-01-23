package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeMBeanException;

import entity.Cost;
import util.DBUtil;

public class CostDao {

	public List<Cost> findAll(){
		Connection con = null;
		try{
			con = DBUtil.getConnection();
			String sql = "select * from cost_xy order by cost_id";
			Statement smt = con.createStatement();
			ResultSet rs = smt.executeQuery(sql);
			List<Cost> list = new ArrayList<Cost>();
			while(rs.next()){
				Cost c = new Cost();
				createCost(rs, c);
				list.add(c);
			}
			return list;
		}catch(SQLException e){
			e.printStackTrace();
			throw new RuntimeException("查询资费失败",e);
		}finally{
			DBUtil.close(con);
		}		
	}	
	
	public void modify(Cost cost){
		Connection con = null;		
		try {
			con = DBUtil.getConnection();
			String sql = "update cost_xy set name=?, base_duration=?, base_cost=?, unit_cost=?, descr=?, cost_type=? where cost_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, cost.getName());
			/*
			 * setInt,setDouble不允许传入null
			 * 但当前业务中，这些值的确可能为null
			 * 则当成setObject()
			 */
			//name,costType,baseDuration,baseCost,unitCost,descr
			ps.setObject(2, cost.getBaseDuration());
			ps.setObject(3, cost.getBaseCost());
			ps.setObject(4, cost.getUnitCost());
			ps.setString(5, cost.getDescr());
			ps.setString(6, cost.getCostType());	
			ps.setObject(7, cost.getCostId());
			ps.executeUpdate();
			System.out.println(cost.getCostId());
			System.out.println("修改资费成功");
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("修改资费失败",e);
		}finally{
			DBUtil.close(con);
		}
	}
	
	public void delete(String costId){
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String sql ="delete from cost_xy where cost_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, Integer.parseInt(costId));			
			ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("删除失败");
		}finally{
			DBUtil.close(con);
		}
	}
	
	public void save(Cost cost){
		Connection con = null;		
		try {
			con = DBUtil.getConnection();
			String sql = "insert into cost_xy values(cost_seq_xy.nextval,?,?,?,?,'1',?,sysdate,null,?)";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, cost.getName());
			/*
			 * setInt,setDouble不允许传入null
			 * 但当前业务中，这些值的确可能为null
			 * 则当成setObject()
			 */
			ps.setObject(2, cost.getBaseDuration());
			ps.setObject(3, cost.getBaseCost());
			ps.setObject(4, cost.getUnitCost());
			ps.setString(5, cost.getDescr());
			ps.setString(6, cost.getCostType());			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("保存资费失败",e);
		}finally{
			DBUtil.close(con);
		}
	}
	
	public void modifyById(Cost cost){
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String sql = "update cost_xy set name=?, cost_type=?, base_duration=?, base_cost=?,"
					+ "unit_cost=?, descr=? where cost_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			//ps.setString(1, x);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Cost findById(int id){
		Connection con = null;
		try {
			con = DBUtil.getConnection();
			String sql = "select * from cost_xy where cost_id=?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			Cost c = new Cost();
			while(rs.next()){				
				createCost(rs, c);
				//System.out.println("查询成功");
			}			
			return c;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("查询此条记录失败");
		}finally{
			DBUtil.close(con);
		}
		//return null;			
	}
	
	private void createCost(ResultSet rs, Cost c) throws SQLException {
		c.setCostId(rs.getInt("cost_id"));
		c.setName(rs.getString("name"));
		c.setBaseDuration(rs.getInt("base_duration"));
		c.setBaseCost(rs.getDouble("base_cost"));
		c.setCostType(rs.getString("cost_type"));
		c.setCreatime(rs.getTimestamp("creatime"));
		c.setDescr(rs.getString("descr"));
		c.setStartime(rs.getTimestamp("startime"));
		c.setStatus(rs.getString("status"));
		c.setUnitCost(rs.getDouble("unit_cost"));
	}
	
	public static void main(String[] args) {
		CostDao dao = new CostDao();
		Cost c = dao.findById(1);
		System.out.println(c.getCostType()+","+c.getName());
	}
}
