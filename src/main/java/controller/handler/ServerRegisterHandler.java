package controller.handler;

import controller.Controller;
import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import template.Handler;
import util.Utilities;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 11:42.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ServerRegisterHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer baseServer, InetAddress srcAddr, int srcPort) {
        Controller controller = (Controller) baseServer;
        controller.addServer(srcAddr);
        // assume the server is added in the beginning. Otherwise you should transmit
        // some data back
        Map<String, Object> reply = new HashMap<String, Object>();
        EventMsg replyMsg = new EventMsg(EventType.SERVER_REGISTER_REPLY, controller.getIdentifier(), reply);
        Utilities.send(controller.getSocket(), Utilities.serialize(replyMsg), srcAddr, srcPort);
        // if we want to support dynamically adding server, we should check
        // the controller's status and return data to the new coming server
        // todo
    }
}
