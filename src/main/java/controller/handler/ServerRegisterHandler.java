package controller.handler;

import controller.Controller;
import javafx.util.Pair;
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
        System.out.println("[debug] Receive the registration info from server " + srcAddr + ":" + srcPort);
        Controller controller = (Controller) baseServer;
        // assume the server is added in the beginning. Otherwise you should transmit
        // some data back
        Map<String, Object> reply = new HashMap<String, Object>();
        Pair<InetAddress, Integer> pair = controller.getLastServer();
        reply.put("reply", true);
        reply.put("prev_server", pair);
        EventMsg replyMsg = new EventMsg(EventType.SERVER_REGISTER_REPLY, controller.getIdentifier(), reply);
        Utilities.send(controller.getSocket(), Utilities.serialize(replyMsg), srcAddr, srcPort);
        // update next hop for previous server

        if (pair != null) {
            reply.put("next_hop", new Pair<InetAddress, Integer>(srcAddr, srcPort));
            EventMsg updateNextHopMsg = new EventMsg(EventType.UPDATE_NEXT_HOP, controller.getIdentifier(), reply);
            Utilities.send(controller.getSocket(), Utilities.serialize(updateNextHopMsg), pair.getKey(), pair.getValue());
        }
        // add server int topo
        controller.addServer(srcAddr, srcPort);

    }
}
