package Com;

import java.util.HashMap;
import java.util.Map;

public class NameList {
    private static HashMap<String, String> map=new HashMap<String, String>();//id和name
    public static HashMap<String, String> getHashmap(){
        return map;
    }
    public static HashMap<String, String> setHashmap(HashMap<String, String> M){
        map = M;
        return map;
    }
    //将用户名入集合
    public static void addid(String id){
        map.put(id, "");
    }
    public static void addname(String id,String name){
        map.put(id, name);
    }
    //通过id返回name
    public static String getname(String id){
        return map.get(id);
    }
    //通过name返回id
    public static String getid(String name){
        if(map.isEmpty()==false){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if(entry.getValue().equals(name)){
                    return entry.getKey();
                }
            }
        }
        return null;
    }
    //返回集合中是否存在用户
    public static Boolean notEmpty(){
        if(map.isEmpty()){
            return false;
        }else{
            return true;
        }
    }
    //返回用户个数
    public static int size(){
        return map.size();
    }
}
