package Client.Socket;

import Client.Func.FileTransfer;
import Client.ui.ChatRoomUI;
import Client.ui.UsersUI;
import Com.CommandTranser;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.*;


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
                        String message = sdf.format(date)+"  "+ msg.getResult() + " :" + "\n" + (String) msg.getData() + "\n\n";
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
                    String message = sdf.format(date)+" "+msg.getReceiver() + ":\n"
                            + (String) msg.getData() + "\n\n";
                    // 在聊天框添加收到的信息
                    chat_txt.append(message);
                    chat_txt.setCaretPosition(chat_txt.getDocument().getLength());
                }
                else if("enterChatRoom".equals(msg.getCmd())){
                    String message =">>>" +msg.getReceiver() + "进入了聊天室"
                            +">>>\n";
                    //在聊天室显示
                    chat_txt.append(message);
                    //
                    ChatRoomUI.print((HashMap<String, String>) msg.getData());
                    chat_txt.setCaretPosition(chat_txt.getDocument().getLength());
                }
                else if("outChatRoom".equals(msg.getCmd())){
                    String message ="<<<" +msg.getReceiver() + "退出了聊天室" +"<<<\n";
                    //在聊天室显示
                    chat_txt.append(message);
                    //
                    HashMap<String, String> map= (HashMap<String, String>)msg.getData();
                    map.remove(msg.getSender());
                    ChatRoomUI.print(map);
                    chat_txt.setCaretPosition(chat_txt.getDocument().getLength());
                }
                else if("fileTransfer".equals(msg.getCmd())){
                    int result=JOptionPane.showConfirmDialog(null,"是否接收？");
                    if(result==JOptionPane.YES_OPTION){
                        //获取下载路径
                        JFileChooser f = new JFileChooser();
                        //设置默认显示的文件夹（不用设置，测试时图方便）
                        f.setCurrentDirectory(new File("F:\\test2"));
                        //设置只可以选择文件夹
                        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        int result1=f.showOpenDialog(null);
                        if(result1==JFileChooser.APPROVE_OPTION){
                            File file = f.getSelectedFile();
                            new FileTransfer((File)(msg.getData()),file);
                            JOptionPane.showMessageDialog(chat_txt, "下载完成！");
                        }
                    }
                }
                /*else if("reprintusers".equals(msg.getCmd())){
                    String status = (String) msg.getData();
                    if("deletfriend".equals(status)){
                        UsersUI.friends.remove(msg.getSender());
                        UsersUI.friends.remove(msg.getReceiver());
                        UsersUI.reprint();
                    }

                }*/
            }
        }
    }
}