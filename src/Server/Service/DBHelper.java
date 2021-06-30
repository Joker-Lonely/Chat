package Server.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
*客户端的操作处理
*/

public class DBHelper {
	private static final String driver="com.mysql.cj.jdbc.Driver";
	private static final String url="jdbc:mysql://118.190.142.138:3306/Chat_program?useUnicode=true&characterEncoding=utf-8";
	private static final String username="root";
	private static final String password="123456";
	private static  Connection con=null;
	//静态块代码负责加载驱动
	static
	{
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Connection getConnection(){

		if(con==null){
			try {
				con=DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return con;
	}
}