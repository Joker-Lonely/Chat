package Client.ui;

import Client.Socket.Client;
import Com.CommandTranser;
import Com.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.*;


public class LoginFrame extends JFrame {

    private static final long serialVersionUID = -4065664344081690485L;

    /**
     * 登录窗体宽度
     */
    private static final Integer FRAME_WIDTH = 400;

    /**
     * 登录窗体高度
     */
    private static final Integer FRAME_HEIGHT = 300;

    //创建一个文字按钮
    JButton enter = new JButton("登 录");
    //创建一个取消按钮
    JButton cancel=new JButton("取 消");
    //创建一个注册按钮
    JButton regist=new JButton("注册账号");
    //账号文本框
    JTextField textUid = new JTextField();
    //创建一个密码框，用于输入用户密码
    JPasswordField textPsw = new JPasswordField();




    public LoginFrame() {
        this.setTitle("登录");
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        //获取屏幕
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width;
        int height = screenSize.height;
        setLocation((width-FRAME_WIDTH)/2, (height-FRAME_HEIGHT)/2);

        JLabel lblBackground=new JLabel();
        //设置图片的位置和大小
        lblBackground.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        //设置布局为空布局
        lblBackground.setLayout(null);
        //添加到当前窗体中
        this.add(lblBackground);

        //创建一个标签
        JLabel lblUid=new JLabel("账 号: ");
        //设置位置、大小
        lblUid.setBounds(80, 60, 120, 30);
        lblUid.setFont(new Font("宋体" , 0 , 16));
        //设置标签文本的颜色为黑色
        lblUid.setForeground(Color.BLACK);
        //将标签添加到背景图片上
        lblBackground.add(lblUid);
        //设置文本框的位置、大小
        textUid.setBounds(150, 60, 160, 30);
        this.add(textUid);

        //创建一个的标签
        JLabel lblPsw=new JLabel("密 码: ");
        //设置标签的位置、大小
        lblPsw.setBounds(80, 100, 120, 30);
        lblPsw.setFont(new Font("宋体" , 0 , 16));
        //设置字体颜色为黑色
        lblPsw.setForeground(Color.BLACK);
        //添加到背景图片上
        lblBackground.add(lblPsw);
        //设置密码框的位置、大小
        textPsw.setBounds(150, 100, 160, 30);
        this.add(textPsw);

        //设置位置、大小
        enter.setBounds(95, 170, 85, 30);
        lblBackground.add(enter);
        //监听登录按钮
        enter.addActionListener(this::actionPerformed);
        //设置按钮的位置、大小
        cancel.setBounds(225, 170, 85, 30);
        //监听取消按钮
        cancel.addActionListener(this::actionPerformed);
        //添加到背景上
        lblBackground.add(cancel);
        //设置按钮的位置、大小
        regist.setBounds(152, 210, 95, 20);
        //监听注册按钮
        regist.addActionListener(this::actionPerformed);
        //添加到背景上
        lblBackground.add(regist);
        //设置布局为空布局
        setLayout(null);
        setVisible(true);

        textUid.setText("test01");
        textPsw.setText("123456");
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == enter){
            String userid = textUid.getText().trim();
            String password = new String(textPsw.getPassword()).trim();
            if ("".equals(userid) || userid == null) {
                JOptionPane.showMessageDialog(null, "请输入帐号！！");
                return;
            }
            if ("".equals(password) || password == null) {
                JOptionPane.showMessageDialog(null, "请输入密码！！");
                return;
            }
            User user = new User(userid, password);
            CommandTranser msg = new CommandTranser();
            msg.setCmd("login");
            msg.setData(user);
            msg.setReceiver(userid);
            msg.setSender(userid);
            // 实例化客户端 并且发送数据 这个client客户端 直到进程死亡 否则一直存在
            Client client = new Client();
            client.sendData(msg);
            msg = client.getData();
            if (msg != null) {
                if (msg.isFlag()) {
                    this.dispose();
                    List<String> friends = (List<String>) msg.getData();
                    JOptionPane.showMessageDialog(null, "登陆成功！");
                    //显示好友界面
                    new UsersUI(friends, user.getUserid(), msg.getReceiver(), client);
                } else {
                    JOptionPane.showMessageDialog(this, msg.getResult());
                }
            }
        }
        if(e.getSource() == regist){
            this.dispose();
            new RegisterUI();
        }
        if(e.getSource() == cancel){
            System.exit(0);
        }
    }
    public static void main(String[] args) {
        new LoginFrame();
    }
}