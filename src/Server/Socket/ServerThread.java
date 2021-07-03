package Server.Socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Com.CommandTranser;
import Com.User;
import Server.Service.OnlineList;
import Server.Service.UserService;

/**
 *  服务器线程
 */
public class ServerThread extends Thread {
    private Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        // 时刻监听 客户端发送来的数据
        while (socket != null) {
            try {
                ois = new ObjectInputStream(socket.getInputStream());
                CommandTranser msg = (CommandTranser) ois.readObject();
                // 处理客户端发送来的信息
                msg = execute(msg);


                //主程序退出
                if("close".equals(msg.getCmd())){
                    SocketThread socketThread=new SocketThread();
                    socketThread.setid(msg.getSender());
                    socketThread.setSocket(socket);
                    SocketList.deletesocket(socketThread);
                }

                //关闭私聊连接
                if("closechat".equals(msg.getCmd())){
                    SocketThread socketThread=new SocketThread();
                    socketThread.setid(msg.getSender());
                    socketThread.setSocket(socket);
                    SocketList.deletesocket(socketThread);
                }

                //打开私聊窗口建立线程
                if("chat".equals(msg.getCmd())){
                    SocketThread socketThread = new SocketThread();
                    String s = (String) msg.getData();
                    socketThread.setid(s);
                    socketThread.setSocket(socket);
                    SocketList.addSocket(socketThread);
                    msg.setResult("OK");
                }
                //退出聊天室，踢出聊天室在线列表
                if("outChatRoom".equals(msg.getCmd())){
                    HashMap<String,String> map = OnlineList.getid();
                    msg.setData(map);
                    if(!OnlineList.getList().isEmpty())
                    {
                        for(String s: OnlineList.getList()){
                            oos=new ObjectOutputStream(SocketList.getSocket(s).getOutputStream());
                            oos.writeObject(msg);
                            oos.flush();
                        }
                    }
                    OnlineList.deleteUser(msg.getSender());
                }
                //如果进入聊天室，进入聊天室在线列表
                if("enterChatRoom".equals(msg.getCmd())){
                    OnlineList.addUser(msg.getSender());
                    HashMap<String,String> map = OnlineList.getid();
                    msg.setData(map);

                    for(String s: OnlineList.getList()){
                        System.out.println("OK");
                        oos=new ObjectOutputStream(SocketList.getSocket(s).getOutputStream());
                        oos.writeObject(msg);
                        oos.flush();
                    }
                }
                //如果是群发消息的指令 查询当前在聊天室列表里的用户，
                if ("allmessage".equals(msg.getCmd())){
                    if(OnlineList.notEmpty()==true){
                        for(String s: OnlineList.getList()){
                            oos=new ObjectOutputStream(SocketList.getSocket(s).getOutputStream());
                            oos.writeObject(msg);
                            oos.flush();
                        }
                    }
                }

                if ("message".equals(msg.getCmd())) {
                    /*
                     * 如果 msg.ifFlag即 服务器处理成功,可以向该好友发送信息;如果服务器处理信息失败,信息发送给发送者本人
                     */
                    if (msg.isFlag()) {
                        String s = msg.getReceiver() + msg.getSender().hashCode();
                        if(SocketList.getSocket(s)!=null){
                            oos = new ObjectOutputStream(SocketList.getSocket(s).getOutputStream());
                        } else {
                            oos = new ObjectOutputStream(SocketList.getSocket(msg.getReceiver()).getOutputStream());
                        }
                    } else {
                        oos = new ObjectOutputStream(socket.getOutputStream());
                    }
                    oos.writeObject(msg);
                    oos.flush();
                }
                // 如果是登录请求 发送给发送者本人
                if ("login".equals(msg.getCmd())) {
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(msg);
                    oos.flush();
                }
                if ("checkregist".equals(msg.getCmd())) {
                    System.out.println("验证成功");
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(msg);
                    oos.flush();
                }
                if ("regist".equals(msg.getCmd())) {
                    System.out.println("注册成功");
                    oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(msg);
                    oos.flush();
                }
            } catch (IOException e) {
                socket = null;
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }

    // 处理客户端发送的信息
    private CommandTranser execute(CommandTranser msg) {
        // 如果是注册验证请求
        if ("checkregist".equals(msg.getCmd())) {
            UserService userService = new UserService();
            User user = (User) msg.getData();
            userService.checkregistUser(user);
            msg.setFlag(userService.checkregistUser(user));
            if(msg.isFlag()) {
                msg.setResult("用户已存在！");
            } else {
                System.out.println("允许注册！");
            }
        }
        // 如果是完成注册请求
        if ("regist".equals(msg.getCmd())) {
            UserService userService = new UserService();
            User user = (User) msg.getData();
            userService.registUser(user);
            msg.setFlag(true);
            msg.setResult("注册成功");
        }
        // 如果是登录请求
        if ("login".equals(msg.getCmd())) {
            UserService userService = new UserService();
            User user = (User) msg.getData();
            msg.setFlag(userService.checkUser(user));
            /*
             * 如果登陆成功，将该客户端加入已经连接成功的map集合里面,并且开启此用户的接受线程
             */
            if (msg.isFlag()) {
                // 将该线程加入连接成功的map集合
                if(SocketList.getSocket(msg.getSender())!=null){
                    msg.setResult("already");
                }
                else{
                    SocketThread socketThread = new SocketThread();
                    socketThread.setid(msg.getSender());
                    socketThread.setSocket(socket);
                    SocketList.addSocket(socketThread);
                    msg.setReceiver(userService.Findusername(user.getUserid()));
                    msg.setData(userService.friendsmap(user));
                    msg.setResult("登陆成功");
                }
            } else {
                msg.setResult("账号密码输入错误！");
            }
        }

        // 如果是发送消息的指令 判断当前用户是否在线
        if ("message".equals(msg.getCmd())) {
            // 如果要发送的用户在线 发送信息
            if (SocketList.getSocket(msg.getReceiver()) != null) {
                msg.setFlag(true);
            } else {
                msg.setFlag(false);
                msg.setResult("当前用户不在线");
            }
        }


        return msg;
    }
}