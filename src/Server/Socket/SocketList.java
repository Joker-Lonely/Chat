package Server.Socket;

import java.net.Socket;
import java.util.HashMap;


/**
 *	所有已经成功登录服务器的socket和昵称
 */

public class SocketList {
    private static HashMap<String, Socket> map=new HashMap<String, Socket>();
    //将SocketThread入集合
    public static void addSocket(SocketThread socketThread){
        map.put(socketThread.getid(), socketThread.getSocket());
    }
    //通过昵称返回socket
    public static Socket getSocket(String name){
        return map.get(name);
    }
}