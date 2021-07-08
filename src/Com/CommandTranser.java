package Com;

import java.io.Serializable;

/**
 *	客户端与服务器交互的数据
 */
public class CommandTranser implements Serializable{
    private static final long serialVersionUID = 1L;
    private String sender=null;//发送者
    private String receiver=null;//接受者
    private Object data=null;//传递的数据
    private Object data2=null;//传递的数据2
    private boolean flag=false;//指令的处理结果
    private String cmd=null;//服务端要做的指令
    private String result=null;//处理结果

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }
    public String getReceiver() {
        return receiver;
    }
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public Object getData2() {
        return data2;
    }
    public void setData2(Object data) {
        this.data2 = data;
    }
    public boolean isFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public String getCmd() {
        return cmd;
    }
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

}