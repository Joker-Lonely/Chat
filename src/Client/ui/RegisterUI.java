package Client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Com.User;
import Client.Socket.Client;
import Com.CommandTranser;

/**
 *用户注册界面
 */

public class RegisterUI extends JFrame implements ActionListener,ItemListener{

    /**
     * 注册窗体宽度
     */
    private static final Integer FRAME_WIDTH = 400;

    /**
     * 注册窗体高度
     */
    private static final Integer FRAME_HEIGHT = 300;

    //创建一个文字按钮
    JButton Register = new JButton("注 册");
    //创建一个取消按钮
    JButton cancel = new JButton("取 消");
    //账号文本框
    JTextField textUid = new JTextField();
    //用户名文本框
    JTextField textUname = new JTextField();
    //创建一个密码框，用于输入用户密码
    JPasswordField textPsw = new JPasswordField();



    public RegisterUI() {
        this.setTitle("用户注册");
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
        lblUid.setBounds(80, 50, 120, 30);
        lblUid.setFont(new Font("宋体" , 0 , 16));
        //设置标签文本的颜色为白色
        lblUid.setForeground(Color.BLACK);
        //将标签添加到背景图片上
        lblBackground.add(lblUid);
        //设置文本框的位置、大小
        textUid.setBounds(150, 50, 160, 30);
        this.add(textUid);

        //创建一个的标签
        JLabel lblName=new JLabel("用户名: ");
        //设置标签的位置、大小
        lblName.setBounds(80, 100, 120, 30);
        lblName.setFont(new Font("宋体" , 0 , 16));
        //设置字体颜色为白色
        lblName.setForeground(Color.BLACK);
        //添加到背景图片上
        lblBackground.add(lblName);
        //设置文本框的位置、大小
        textUname.setBounds(150, 100, 160, 30);
        this.add(textUname);

        //创建一个的标签
        JLabel lblPsw=new JLabel("密 码: ");
        //设置标签的位置、大小
        lblPsw.setBounds(80, 150, 120, 30);
        lblPsw.setFont(new Font("宋体" , 0 , 16));
        //设置字体颜色为白色
        lblPsw.setForeground(Color.BLACK);
        //添加到背景图片上
        lblBackground.add(lblPsw);
        //设置密码框的位置、大小
        textPsw.setBounds(150, 150, 160, 30);
        this.add(textPsw);

        //设置位置、大小
        Register.setBounds(85, 200, 85, 30);
        lblBackground.add(Register);
        //监听登录按钮
        Register.addActionListener(this::actionPerformed);
        //设置按钮的位置、大小
        cancel.setBounds(225, 200, 85, 30);
        //监听取消按钮
        cancel.addActionListener(this::actionPerformed);
        //添加到背景上
        lblBackground.add(cancel);
        //设置布局为空布局
        setLayout(null);
        setVisible(true);
    }


    @Override
    public void itemStateChanged(ItemEvent e) {
        // TODO 自动生成的方法存根
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        if(e.getSource()==Register){
            //获取输入的信息
            String userid = textUid.getText().trim();
            String password = new String(textPsw.getPassword()).trim();
            String username = textUname.getText().trim();
            if ("".equals(username) || username == null) {
                JOptionPane.showMessageDialog(this, "请输入用户名！！","系统提示",JOptionPane.WARNING_MESSAGE);
                return;
            }
            if ("".equals(password) || password == null) {
                JOptionPane.showMessageDialog(this, "请输入密码！！","系统提示",JOptionPane.WARNING_MESSAGE);
                return;
            }
            if ("".equals(username) || username == null) {
                JOptionPane.showMessageDialog(this, "请输入帐号！！","系统提示",JOptionPane.WARNING_MESSAGE);
                return;
            }
            User user = new User(userid, username, password);
            CommandTranser msg = new CommandTranser();
            //注册之前，先验证一下该数据库中是否存在该用户
            msg.setCmd("checkregist");
            msg.setData(user);
            msg.setReceiver(userid);
            msg.setSender(userid);
            //实例化客户端,并且发送数据,这个client客户端直到进程死亡
            Client client = new Client();
            client.sendData(msg);
            msg = client.getData();
            if (msg != null) {
                if (msg.isFlag()==false) { //验证该账号未注册
                    //this.dispose();
                    System.out.println(msg.isFlag());
                    msg.setCmd("regist");
                    msg.setData(user);
                    msg.setReceiver(userid);
                    msg.setSender(userid);
                    //实例化客户端并且发送数据，该client客户端直到进程死亡
                    Client c = new Client();
                    c.sendData(msg);
                    msg = c.getData();
                    if (msg.isFlag() == true) {
                        this.dispose();
                        JOptionPane.showMessageDialog(null, "注册成功！", "系统提示", JOptionPane.WARNING_MESSAGE);
                        // 显示登录界面
                        new LoginFrame();
                        }
                    } else {
                        System.out.println(msg.isFlag());
                        JOptionPane.showMessageDialog(null,"该账号已存在！","系统提示",JOptionPane.WARNING_MESSAGE);
                        new RegisterUI();
                    }
                }
        }
        if(e.getSource() == cancel){ //点击取消按钮
            this.dispose();
            new LoginFrame();
        }
    }
    public static void main(String[] args) {
        new RegisterUI();
    }
}