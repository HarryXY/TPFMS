package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBTool {
	//成员变量
	private static String driver;
	private static String url;
	private static String user;
	private static String pwd;
	
	//这些参数只读一次即可
	static{
		Properties p = new Properties();
		try {
			//任何class都可以读取流，一般用本类
			p.load(DBTool.class.getClassLoader().getResourceAsStream("db.properties"));
			driver = p.getProperty("driver");
			url = p.getProperty("url");
			user = p.getProperty("user");
			pwd = p.getProperty("pwd");
			//加载驱动：只需要加载一次驱动即可
			try {
				Class.forName(driver);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException("加载驱动失败",e);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("加载资源文件失败",e);
		}
	}
	
	//***static静态方法可以直接“类名.方法名”调用，不需new对象，方便调用！！！
	public static Connection getConnection() throws SQLException{	
		
		return DriverManager.getConnection(url, user, pwd);		
	}
	
	public static void close(Connection con){
		if(con != null){
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭连接失败",e);
			}
		}
	}
	
}











