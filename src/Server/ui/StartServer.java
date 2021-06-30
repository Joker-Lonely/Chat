package Server.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


/**
 * 服务器界面
 */
public class StartServer extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JButton startServer_btn;
    private JButton endServer_btn;
    private JLabel p;

    public StartServer() {
        setLayout(new FlowLayout());
        startServer_btn = new JButton("开启服务器");
        endServer_btn = new JButton("关闭服务器");
        add(startServer_btn);
        add(endServer_btn);
        setTitle("Chat服务端");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        startServer_btn.addActionListener(this);
        endServer_btn.addActionListener(this);
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new StartServer();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //当点击了开启服务器按钮便新开启一个线程 开启动服务器
        if (e.getSource() == startServer_btn) {
            new startServerThread().start();
            JOptionPane.showMessageDialog(null, "Chat服务器开启成功，请连接...");
        }
        if (e.getSource() == endServer_btn) {
            System.exit(0);
        }
    }
}

