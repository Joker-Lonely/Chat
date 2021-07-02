package Client.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.*;

import Client.Socket.Client;
import Client.Socket.ClientThread;
import Com.CommandTranser;

/**
 * 聊天对话框
 */
public class ChatUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JTextArea chat_txt;
    private JTextField message_txt;
    private JButton send_btn;
    private JPanel panel;
    private String myid;
    private String myname;
    private String friendid;
    private String friendname;
    private Client client;
    private ClientThread thread;// 接收信息线程

    public ChatUI(String myid, String myname, String friendid, String friendname, Client client) {
        this.myid = myid;
        this.myname = myname;
        this.friendid = friendid;
        this.friendname = friendname;
        this.client = client;
        init();
        setTitle("与" + friendname + "聊天中");
        setSize(350, 325);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        // 开启客户端接收信息线程
        thread = new ClientThread(client, chat_txt);
        thread.start();
    }

    private void init() {
        // TODO Auto-generated method stub
        setLayout(new BorderLayout());
        panel = new JPanel();
        message_txt = new JTextField(20);
        send_btn = new JButton("发送");
        panel.add(message_txt);
        panel.add(send_btn);
        chat_txt = new JTextArea();
        chat_txt.setEditable(false);
        //创建聊天消息框的滚动窗
        JScrollPane chat = new JScrollPane(chat_txt);
        //聊天框不可编辑，只用来显示
        chat_txt.setEditable(false);
        //设置滚动窗的水平滚动条属性:不出现
        chat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //设置滚动窗的垂直滚动条属性:需要时自动出现
        chat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        chat_txt.add(new JScrollBar(JScrollBar.VERTICAL));
        this.add(chat, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);
        send_btn.addActionListener(this);
        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                thread.setOnline(false);
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub
                thread.setOnline(false);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        // 如果点击了发送按钮
        if (e.getSource() == send_btn) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            String message = sdf.format(date)+"  "+ myname + " :" + "\n" + message_txt.getText() + "\n";
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
    }

}