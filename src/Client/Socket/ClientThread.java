package Client.Socket;

import Com.CommandTranser;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;



/**
 * 客户端线程类。一旦聊天启动，为其开启一个线程
 * I/O阻塞接收服务端发送的数据
 */
public class ClientThread extends Thread {
    private Client client;//客户端对象
    private boolean isOnline = true;//当前聊天框是否存在
    private JTextArea chat_txt;//聊天框

    public ClientThread(Client client, JTextArea chat_txt) {
        this.client = client;
        this.chat_txt = chat_txt;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public void run() {
        while (isOnline()) {
            //I/O阻塞  接收服务端发送的数据
            CommandTranser msg = client.getData();
            if (msg != null) {
                if ("message".equals(msg.getCmd())) {
                    /*
                     * 如果服务端处理数据成功，接收信息
                     * 否则弹出对方不在线的对话框
                     */
                    if (msg.isFlag()) {
                        Date date = new Date();
                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                        String message = sdf.format(date)+"  "+ msg.getResult() + " :" + "\n" + (String) msg.getData() + "\n";
                        // 在聊天框添加收到的信息
                        chat_txt.append(message);
                        //滚动条拉到最底部，显示最新消息
                        chat_txt.setCaretPosition(chat_txt.getDocument().getLength());
                    } else {
                        JOptionPane.showMessageDialog(chat_txt, msg.getResult());
                    }
                }
                else if("allmessage".equals(msg.getCmd())){
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "HH:mm:ss");
                    String message = sdf.format(date)+" "+msg.getSender() + ":\n"
                            + (String) msg.getData() + "\n\n";
                    // 在聊天框添加收到的信息
                    chat_txt.append(message);
                    chat_txt.setCaretPosition(chat_txt.getDocument().getLength());
                }
                else if("enterChatRoom".equals(msg.getCmd())){
                    String message ="--------------------------------------------\n"
                            +msg.getReceiver() + "进入了聊天室"
                            +"\n--------------------------------------------\n";
                    //在聊天室显示
                    chat_txt.append(message);
                }
                else if("outChatRoom".equals(msg.getCmd())){
                    String message ="\n--------------------------------------------\n"
                            +msg.getReceiver() + "退出了聊天室"
                            +"\n--------------------------------------------\n";
                    //在聊天室显示
                    chat_txt.append(message);
                }


            }
        }
    }
}