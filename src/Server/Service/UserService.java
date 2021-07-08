package Server.Service;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import Com.User;



/**
 * 客户端的操作处理
 */
public class UserService {

    //登录时，查询是否存在该账号
    public boolean checkUser(User user) {
        PreparedStatement stmt = null;
        Connection conn =null;
        ResultSet rs = null;
        conn = DBHelper.getConnection();
        String sql = "select * from User where Userid=? and password =?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUserid());
            stmt.setString(2, user.getPassword());
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }
    //注册时，查询是否已经注册该账号
    public boolean checkregistUser(User user) {
        PreparedStatement stmt = null;
		Connection conn = null;
		ResultSet rs = null;
		conn = DBHelper.getConnection();
		String sql = "select * from User where Userid=?";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, user.getUserid());
			rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
        return false;
    }
    //完成用户的注册
    public boolean registUser(User user) {
        PreparedStatement stmt = null;
        Connection conn = null;
        conn = DBHelper.getConnection();
        String sql = "insert into User(Userid,Username,password) values (?,?,?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUserid());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.executeUpdate();;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }
    public static String Findusername(String id){
        String name = "";
        PreparedStatement stmt = null;
        Connection conn =null;
        ResultSet rs = null;
        conn = DBHelper.getConnection();
        String sql = "select Username from User where Userid=?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                name = rs.getString(1);
                return name;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return name;
    }
    public static HashMap<String,String> getname(HashMap<String, String> users){
        try {
            if(users.isEmpty()==false){
                for (Map.Entry<String, String> entry : users.entrySet()) {
                    entry.setValue(Findusername(entry.getKey()));
                }
            }
            return users;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return users;
    }
    public static HashMap<String,String> friendsmap(String id){
        HashMap<String,String> friends = new HashMap<String,String>();
        PreparedStatement stmt = null;
        Connection conn =null;
        ResultSet rs = null;
        conn = DBHelper.getConnection();
        String sql = "select friend_id from friend_list where id=?";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();
            while(rs.next()){
                friends.put(rs.getString(1),"");
            }
            friends = getname(friends);
            return friends;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return friends;
    }
    public static Boolean addfriend(String id, String friendid){
        PreparedStatement stmt = null;
        Connection conn =null;
        Boolean rs = false;
        conn = DBHelper.getConnection();
        String sql = "insert into friend_list (id, friend_id) values ( ? , ?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, friendid);
            rs = stmt.execute();
            return rs;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Boolean deletefriend(String id, String friendid){
        PreparedStatement stmt = null;
        Connection conn =null;
        Boolean rs = false;
        conn = DBHelper.getConnection();
        String sql = "delete from friend_list where id=? AND friend_id=? ";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            stmt.setString(2, friendid);
            rs = stmt.execute();
            return rs;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }
}