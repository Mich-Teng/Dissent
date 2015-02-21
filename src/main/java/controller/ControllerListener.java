package controller;

import controller.handler.ControllerHandler;
import proto.EventMsg;
import proto.EventType;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 10:29.
 * Package: controller
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ControllerListener implements Runnable {
    private Controller controller = null;
    private byte[] buffer = new byte[4096];

    public ControllerListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        DatagramSocket socket = controller.getSocket();
        ControllerHandler[] handlers = new ControllerHandler[EventType.EVENT_NUM];
        assignHandlers(handlers);
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                EventMsg eventMsg = new EventMsg(packet.getData());
                InetAddress srcAddr = packet.getAddress();
                handlers[eventMsg.getEventType()].execute(eventMsg, controller, srcAddr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void assignHandlers(ControllerHandler[] handlers) {

    }
}
