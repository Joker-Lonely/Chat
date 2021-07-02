package Client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Random;


import javax.swing.*;

import Client.Socket.Client;
import Com.CommandTranser;

/**
 * 列表界面
 */
public class UsersUI extends JFrame {
    static final long serialVersionUID = 1L;
    JPanel friend_pal;
    JScrollPane jsp;
    static String id;
    String name;
    JTabbedPane jtp;
    static Client client;
    List<String> friends;
    JLabel[] friendname;
    //创建一个文字按钮
    JButton ChatRoom = new JButton("进入聊天室");

    public UsersUI(List<String> friends,String id, String name,Client client) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.friends = friends;
        init();
        setTitle("Hi," + name);
        setSize(350, 600);
        setLocation(1100, 100);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        //设置位置、大小
    }

    private void init() {
        // TODO Auto-generated method stub
        jtp = new JTabbedPane();
        jtp.setBounds(0,19,350,550);
        friend_pal = new JPanel();
        friend_pal.setLayout(new GridLayout(50, 1, 4, 4));
        friendname = new JLabel[50];
        for (int i = 0; i < friends.size(); i++) {
            friendname[i] = new JLabel(friends.get(i), JLabel.CENTER);
            friendname[i].addMouseListener(new MyMouseListener());
            friend_pal.add(friendname[i]);
        }
        jsp = new JScrollPane(friend_pal);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jtp.add("我的好友", jsp);
        add(jtp);
        ChatRoom.setBounds(0, 0, 95, 20);
        this.add(ChatRoom);
        //监听登录按钮
        ChatRoom.addActionListener(this::actionPerformed);
    }

    //点击聊天室按钮后，生成聊天室界面并将用户存到聊天室在线用户列表里
    private void actionPerformed(ActionEvent e) {
        if(e.getSource() == ChatRoom){
            CommandTranser msg = new CommandTranser();
            msg.setCmd("enterChatRoom");
            msg.setSender(id);
            client.sendData(msg);
            new ChatRoomUI(id, name, client);
        }
    }

    static class MyMouseListener extends MouseAdapter {


        @Override
        public void mouseClicked(MouseEvent e) {
            // TODO Auto-generated method stub
            // 如果双击了两次 我的好友 弹出与这个好友的聊天框
            if (e.getClickCount() == 2) {
                JLabel label = (JLabel) e.getSource();
                new ChatUI(id, label.getText(), client);
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