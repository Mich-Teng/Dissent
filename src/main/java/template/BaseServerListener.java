package template;

import proto.EventMsg;
import proto.EventType;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-22 20:57.
 * Package: template
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class BaseServerListener implements Runnable {
    private byte[] buffer = new byte[4096];
    BaseServer baseServer = null;

    public BaseServerListener(BaseServer baseServer) {
        this.baseServer = baseServer;
    }

    @Override
    public void run() {
        DatagramSocket socket = baseServer.getSocket();
        Handler[] handlers = new Handler[EventType.EVENT_NUM];
        assignHandlers(handlers);
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                EventMsg eventMsg = new EventMsg(packet.getData());
                InetAddress srcAddr = packet.getAddress();
                handlers[eventMsg.getEventType()].execute(eventMsg, baseServer, srcAddr, packet.getPort());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void assignHandlers(Handler[] handlers) {

    }
}
