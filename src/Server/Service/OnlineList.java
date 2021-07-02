package Server.Service;

import Server.Socket.SocketThread;
import java.net.Socket;
import java.util.HashMap;

public class OnlineList {
    private static HashMap<String, Socket> map=new HashMap<String, Socket>();//id和socket
    public static HashMap<String, Socket> getHashmap(){
        return map;
    }
    //将SocketThread入集合
    public static void addSocket(SocketThread socketThread){
        map.put(socketThread.getid(), socketThread.getSocket());
    }
    //将用户从列表中剔除
    public static void deleteUser(SocketThread socketThread){
        map.remove(socketThread.getid(),socketThread.getSocket());
    }
    //通过id返回socket
    public static Socket getSocket(String id){
        return map.get(id);
    }
    //返回集合中是否存在用户
    public static Boolean notEmpty(){
        if(map.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
}
