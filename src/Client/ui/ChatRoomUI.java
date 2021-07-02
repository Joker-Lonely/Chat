package Client.ui;

import Client.Socket.Client;
import Client.Socket.ClientThread;
import Com.CommandTranser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

public class ChatRoomUI extends JFrame implements ActionListener {
    private String id;
    private String name;
    private Client client;
    private ClientThread thread;// 接收信息线程
    //时间显示格式
    SimpleDateFormat sdf =new SimpleDateFormat("HH:mm:ss");
    //窗口宽度
    final int WIDTH = 700;
    //窗口高度
    final int HEIGHT = 700;

    //创建发送按钮
    JButton btnSend = new JButton("发送");
    //创建清除按钮
    JButton btnClear = new JButton("清屏");
    //创建发送文件按钮
    JButton btnExit = new JButton("发送文件");

    //创建消息接收者标签
    //JLabel lblReceiver = new JLabel("谁来接收：");

    //创建文本输入框, 参数分别为行数和列数
    JTextArea jtaSay = new JTextArea();

    //创建聊天消息框
    JTextArea jtaChat = new JTextArea();
    //当前在线列表的列标题
    String[] colTitles = {"昵称"};
    //当前在线列表的数据
    String[][] rowData = null;
    //创建当前在线列表
    JTable jtbOnline = new JTable
            (
                    new DefaultTableModel(rowData, colTitles) {
                        //表格不可编辑，只可显示
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return false;
                        }
                    }
            );

    //创建聊天消息框的滚动窗
    JScrollPane jspChat = new JScrollPane(jtaChat);

    //创建当前在线列表的滚动窗
    JScrollPane jspOnline = new JScrollPane(jtbOnline);

    //设置默认窗口属性，连接窗口组件
    public ChatRoomUI(String id, String name, Client client){
        this.id=id;
        this.name=name;
        this.client=client;
        // 开启客户端接收信息线程
        thread = new ClientThread(client, jtaChat);
        thread.start();
        init();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void init() {
        //标题
        setTitle("聊天室");
        //大小
        setSize(WIDTH, HEIGHT);
        //不可缩放
        setResizable(false);
        //设置布局:不适用默认布局，完全自定义
        setLayout(null);

        //设置按钮大小和位置
        btnSend.setBounds(350, 620, 60, 30);
        btnClear.setBounds(425, 620, 60, 30);
        btnExit.setBounds(10, 415, 100, 30);

        //设置标签大小和位置
        //lblReceiver.setBounds(20, 420, 300, 30);

        //设置按钮文本的字体
        btnSend.setFont(new Font("宋体", Font.BOLD, 13));
        btnClear.setFont(new Font("宋体", Font.BOLD, 13));
        btnExit.setFont(new Font("宋体", Font.BOLD, 13));

        //添加按钮
        this.add(btnSend);
        this.add(btnClear);
        this.add(btnExit);

        btnSend.addActionListener(this);
        btnClear.addActionListener(this);
        btnExit.addActionListener(this);
        //添加标签
        //this.add(lblReceiver);

        //设置文本输入框大小和位置
        jtaSay.setBounds(10, 450, 475, 160);
        //设置文本输入框字体
        jtaSay.setFont(new Font("楷体", Font.BOLD, 16));
        //输入文本框自动换行
        jtaSay.setLineWrap(true);
        //添加文本输入框
        this.add(jtaSay);

        //聊天消息框自动换行
        jtaChat.setLineWrap(true);
        //聊天框不可编辑，只用来显示
        jtaChat.setEditable(false);
        //设置聊天框字体
        jtaChat.setFont(new Font("楷体", Font.BOLD, 16));

        //设置滚动窗的水平滚动条属性:不出现
        jspChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //设置滚动窗的垂直滚动条属性:需要时自动出现
        jspChat.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //设置滚动窗大小和位置
        jspChat.setBounds(10, 10, 475, 400);
        //添加聊天窗口的滚动窗
        this.add(jspChat);

        //设置滚动窗的水平滚动条属性:不出现
        jspOnline.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //设置滚动窗的垂直滚动条属性:需要时自动出现
        jspOnline.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //设置当前在线列表滚动窗大小和位置
        jspOnline.setBounds(500, 10, 170, 640);
        //添加当前在线列表
        this.add(jspOnline);

        // 添加窗口关闭事件
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub
                thread.setOnline(false);
                CommandTranser msg = new CommandTranser();
                msg.setCmd("outChatRoom");
                client.sendData(msg);
                UsersUI.changestatus();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub
                thread.setOnline(false);
                CommandTranser msg = new CommandTranser();
                msg.setCmd("outChatRoom");
                client.sendData(msg);
                UsersUI.changestatus();
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //如果点击了发送按钮
        if(e.getSource()==btnSend) {
            //滚动条拉到最底部，显示最新消息
            jtaChat.setCaretPosition(jtaChat.getDocument().getLength());
            //在聊天室打印发送动作的信息
            String s = jtaSay.getText();
            if (s != null && !"".equals(s.trim())) {

                //向服务器发送聊天信息
                CommandTranser msg = new CommandTranser();
                msg.setCmd("allmessage");
                msg.setSender(id);
                msg.setData(jtaSay.getText());
                client.sendData(msg);
                // 发送信息完毕 写信息的文本框设空
                jtaSay.setText(null);
            }
            else{
                JOptionPane.showMessageDialog(null, "发送消息不能为空");
            }
        }
        //如果点击清屏按钮
        if(e.getSource()==btnClear){
            jtaChat.setText("");
        }
    }
}
