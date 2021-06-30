package Server.ui;

import Server.Socket.Server;

class startServerThread extends Thread {

    @Override
    public void run() {
        // TODO Auto-generated method stub
        Server s = new Server();
        s.startService();
    }

}