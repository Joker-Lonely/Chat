package Server.Service;

import Server.Socket.SocketThread;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OnlineList {
    private static List<String> list=new LinkedList<>();//id
    public static List<String> getList(){
        return list;
    }
    //将SocketThread入集合
    public static void addUser(String id){
        list.add(id);
    }
    //将用户从列表中剔除
    public static void deleteUser(String id){
        list.remove(id);
    }

    //返回集合中是否存在用户
    public static Boolean notEmpty(){
        if(list.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    public static HashMap<String, String> getid(){
        HashMap<String, String> map = new HashMap<String,String>();
        if(list.isEmpty()==false){
            for (String s: list) {
                map.put(s,"");
            }
        }
        UserService userservice = new UserService();
        map = userservice.getname(map);
        return map;
    }
}
