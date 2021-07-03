package Client.Func;

import java.util.HashMap;
import java.util.Map;

public class Chatlist {
    private static HashMap<String, Boolean> map=new HashMap<String, Boolean>();//id和status
    public static HashMap<String, Boolean> getHashmap(){
        return map;
    }
    //将SocketThread入集合
    public static void addfriend(String id, Boolean status){
        map.put(id, status);
    }
    //将用户从列表中剔除
    public static void deletefriend(String id, Boolean status){
        map.remove(id, status);
    }
    //通过id返回聊天窗口状态
    public static Boolean getstatus(String id){
        return map.get(id);
    }
    public static void setstatus(String id, Boolean status ){
        map.put(id, status);
    }
    //返回集合中是否存在用户
    public static Boolean notEmpty(){
        if(map.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    public static void init(HashMap<String,String> friends){
        if(friends.isEmpty()==false){
            for (Map.Entry<String, String> entry : friends.entrySet()) {
                map.put(entry.getKey(),false);
            }
        }
    }
   }