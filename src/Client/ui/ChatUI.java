package Client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

import Client.Func.Chatlist;
import Client.Socket.Client;
import Client.Socket.ClientThread;
import Com.CommandTranser;

/**
 * 聊天对话框
 */
public class ChatUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    
    private String myid;
    private String myname;
    private String friendid;
    private String friendname;
    private Client client;
    private ClientThread thread;// 接收信息线程

    //窗口宽度
    final int WIDTH = 500;
    //窗口高度
    final int HEIGHT = 550;

    //创建发送按钮
    JButton btnSend = new JButton("发送");
    //创建发送文件按钮
    JButton btnFile = new JButton("发送文件");
    //创建文本输入框, 参数分别为行数和列数
    JTextArea message_txt = new JTextArea();
    //创建聊天消息框
    JTextArea chat_txt = new JTextArea();
    //创建聊天消息框的滚动窗
    JScrollPane Chat_txt = new JScrollPane(chat_txt);
    //创建聊天输入框的滚动窗
    JScrollPane Message_txt = new JScrollPane(message_txt);

    public ChatUI(String myid, String myname, String friendid, String friendname) {
        this.myid = myid;
        this.myname = myname;
        this.friendid = friendid;
        this.friendname = friendname;
        this.client = new Client();
        Chatlist.setstatus(friendid,true);
        CommandTranser msg = new CommandTranser();
        msg.setCmd("chat");
        msg.setData(myid+friendid.hashCode());//将线程用户名置为myid+friendid.hashCode()进行区分
        msg.setSender(myid);
        client.sendData(msg);

        init();
        setTitle("与" + friendname + "聊天中");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        // 开启客户端接收信息线程
        thread = new ClientThread(client, chat_txt);
        thread.start();
        
    }

    private void init() {
        // TODO Auto-generated method stub
        //大小
        setSize(WIDTH, HEIGHT);
        //不可缩放
        setResizable(false);
        //设置布局:不适用默认布局，完全自定义
        setLayout(null);

        //设置按钮大小和位置
        btnSend.setBounds(390, 475, 80, 30);
        btnFile.setBounds(270, 475, 100, 30);

        //设置按钮文本的字体
        btnSend.setFont(new Font("宋体", Font.BOLD, 13));
        btnFile.setFont(new Font("宋体", Font.BOLD, 13));

        //添加按钮
        this.add(btnSend);
        this.add(btnFile);

        btnSend.addActionListener(this);
        btnFile.addActionListener(this);


        //设置文本输入框字体
        message_txt.setFont(new Font("楷体", Font.BOLD, 16));
        //输入文本框自动换行
        message_txt.setLineWrap(true);
        //设置滚动窗的水平滚动条属性:不出现
        Message_txt.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //设置滚动窗的垂直滚动条属性:需要时自动出现
        Message_txt.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //设置文本输入框大小和位置
        Message_txt.setBounds(5, 310, 478, 160);

        //添加文本输入框
        this.add(Message_txt);

        //聊天消息框自动换行
        chat_txt.setLineWrap(true);
        //聊天框不可编辑，只用来显示
        chat_txt.setEditable(false);
        //设置聊天框字体
        chat_txt.setFont(new Font("楷体", Font.BOLD, 16));

        //设置滚动窗的水平滚动条属性:不出现
        Chat_txt.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //设置滚动窗的垂直滚动条属性:需要时自动出现
        Chat_txt.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //设置滚动窗大小和位置
        Chat_txt.setBounds(5, 5, 478, 300);
        //添加聊天窗口的滚动窗
        this.add(Chat_txt);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                closechat();
                thread.setOnline(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub
                Chatlist.setstatus(friendid,false);
                thread.setOnline(false);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // 如果点击了发送按钮
        if (e.getSource() == btnSend) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            String message = sdf.format(date)+"  "+ myname + " :" + "\n" + message_txt.getText() + "\n\n";
            // 在本地文本区追加发送的信息
            chat_txt.append(message);
            // msg为客户端向服务器发送的数据
            CommandTranser msg = new CommandTranser();
            msg.setCmd("message");
            msg.setSender(myid);
            msg.setReceiver(friendid);
            msg.setData(message_txt.getText());
            msg.setResult(myname);//用于传输发送者昵称
            client.sendData(msg);
            // 发送信息完毕 写信息的文本框设空
            message_txt.setText(null);
        }
        if(e.getSource() == btnFile){
            System.out.println("file");
        }
    }
    //窗口结束工作
    public void closechat()
    {
        Chatlist.setstatus(friendid,false);
        CommandTranser msg = new CommandTranser();
        msg.setCmd("closechat");
        msg.setSender(myid+friendid.hashCode());
        client.sendData(msg);
    }

}