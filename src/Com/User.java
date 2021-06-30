package Com;

import java.io.Serializable;

/**
 * 用户信息的实体类
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userid;// 账号
    private String username;// 用户名
    private String password;// 密码
    public User(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }
    public User(String userid, String username, String password) {
        this.userid = userid;
        this.password = password;
        this.username = username;
    }
    public String getUserid() {
        return userid;
    }
    public void setUserid(String Userid) {
        this.userid = Userid;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}