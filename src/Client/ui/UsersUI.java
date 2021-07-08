package Client.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;


import javax.swing.*;

import Client.Func.Chatlist;
import Client.Socket.Client;
import Com.CommandTranser;


/**
 * 列表界面
 */
public class UsersUI extends JFrame {
    static final long serialVersionUID = 1L;
    public static boolean status=false;
    static JPanel friend_pal;
    static JScrollPane jsp;
    static String id;
    static String name;
    static JTabbedPane jtp;
    static Client client;
    public static HashMap<String, String>  friends;
    static JLabel[] friendname;
    //创建一个文字按钮
    static JButton ChatRoom = new JButton("进入聊天室");

    public static void changestatus(){
        status=false;
    }
    public UsersUI(HashMap<String,String> friends,String id, String name,Client client) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.friends = friends;
        init();

        ChatRoom.setBounds(0, 0, 95, 20);
        this.add(ChatRoom);
        //监听按钮
        ChatRoom.addActionListener(this::actionPerformed);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                CommandTranser msg = new CommandTranser();
                msg.setCmd("close");
                msg.setSender(id);
                client.sendData(msg);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub

            }
        });

        setTitle("Hi," + name);
        setSize(350, 600);
        setLocation(1100, 100);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    public void init() {
        // TODO Auto-generated method stub
        jtp = new JTabbedPane();
        jtp.setBounds(0,19,350,550);
        friend_pal = new JPanel();
        friend_pal.setLayout(new GridLayout(friends.size(), 1, 4, 4));
        friendname = new JLabel[friends.size()];
        int i = 0;
        if(friends.isEmpty()==false){
            for (Map.Entry<String, String> entry : friends.entrySet()) {
                friendname[i] = new JLabel(entry.getValue(), JLabel.CENTER);
                friendname[i].addMouseListener(new MyMouseListener());
                friend_pal.add(friendname[i]);
                String labelid = entry.getKey();
                //添加右键菜单
                JPopupMenu popupMenu = new JPopupMenu(labelid);
                addPopup(friendname[i], popupMenu);
                JMenuItem mntmNewMenuItem = new JMenuItem("删除好友");
                popupMenu.add(mntmNewMenuItem);
                //给菜单中的项目添加监听
                mntmNewMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        CommandTranser msg = new CommandTranser();
                        msg.setCmd("deletefriend");
                        msg.setSender(id);
                        msg.setReceiver(labelid);
                        client.sendData(msg);
                    }
                });
            }
        }
        jsp = new JScrollPane(friend_pal);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jtp.add("我的好友", jsp);
        add(jtp);
    }
    /*public static void reprint(){
        friend_pal.removeAll();
        jtp = new JTabbedPane();
        jtp.setBounds(0,19,350,550);
        friend_pal = new JPanel();
        friend_pal.setLayout(new GridLayout(friends.size(), 1, 4, 4));
        friendname = new JLabel[friends.size()];
        int i = 0;
        if(friends.isEmpty()==false){
            for (Map.Entry<String, String> entry : friends.entrySet()) {
                friendname[i] = new JLabel(entry.getValue(), JLabel.CENTER);
                friendname[i].addMouseListener(new MyMouseListener());
                friend_pal.add(friendname[i]);
                String labelid = entry.getKey();
                //添加右键菜单
                JPopupMenu popupMenu = new JPopupMenu(labelid);
                addPopup(friendname[i], popupMenu);
                JMenuItem mntmNewMenuItem = new JMenuItem("删除好友");
                popupMenu.add(mntmNewMenuItem);
                //给菜单中的项目添加监听
                mntmNewMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        CommandTranser msg = new CommandTranser();
                        msg.setCmd("deletefriend");
                        msg.setSender(id);
                        msg.setReceiver(labelid);
                        client.sendData(msg);
                    }
                });
            }
        }
        jsp = new JScrollPane(friend_pal);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jtp.add("我的好友", jsp);
        jsp.add(jtp);
        jsp.updateUI();
    }*/
    private static void addPopup(Component component, final JPopupMenu popup) {
        component.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showMenu(e);
                }
            }
            private void showMenu(MouseEvent e) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }


    //点击聊天室按钮后，生成聊天室界面并将用户存到聊天室在线用户列表里
    private void actionPerformed(ActionEvent e) {
        if(e.getSource() == ChatRoom){
            if(status){
                JOptionPane.showMessageDialog(null, "你已进入聊天室！");
            }
            else{
                CommandTranser msg = new CommandTranser();
                msg.setCmd("enterChatRoom");
                msg.setSender(id);
                msg.setReceiver(name);  //用来显示发送方的用户名
                client.sendData(msg);
                msg = client.getData();
                HashMap<String,String> online = (HashMap<String,String>) msg.getData();
                new ChatRoomUI(id, name, client, online);
                status=true;
            }
        }
    }

    public static void openchat(String friendid, String friendname){
        if(Chatlist.getstatus(friendid)!=null){
            if(!Chatlist.getstatus(friendid)){
                new ChatUI(id, name, friendid, friendname);
            }
        }
    }

    static class MyMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            // 如果双击了两次 我的好友 弹出与这个好友的聊天框
            if (e.getClickCount() == 2) {
                String friendid = null;
                JLabel label = (JLabel) e.getSource();
                if(UsersUI.friends.isEmpty()==false){
                    for (Map.Entry<String, String> entry : friends.entrySet()) {
                        if(entry.getValue().equals(label.getText())){
                            friendid = entry.getKey();
                        }
                    }
                }
                UsersUI.openchat(friendid, label.getText());
            }
        }

        // 如果鼠标进入我的好友列表 背景色变色
        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
            JLabel label = (JLabel) e.getSource();
            label.setOpaque(true);
            label.setBackground(new Color(255, 240, 230));
        }

        // 如果鼠标退出我的好友列表 背景色变色
        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub
            JLabel label = (JLabel) e.getSource();
            label.setOpaque(false);
            label.setBackground(Color.WHITE);
        }
    }
}