package Server.Socket;

import java.net.Socket;

/**
 * 所有成功连接的socket实体类 包括一个socket，一个用户名(即账号)
 */
public class SocketThread {
    private Socket socket;
    private String id;

    public SocketThread() {
        super();
        // TODO Auto-generated constructor stub
    }

    public SocketThread(Socket socket, String id) {
        super();
        this.socket = socket;
        this.id = id;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getid() {
        return id;
    }

    public void setName(String id) {
        this.id = id;
    }
}