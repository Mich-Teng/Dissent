package controller.handler;

import controller.Controller;
import controller.ControllerStatus;
import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import template.Handler;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-25 16:16.
 * Package: client.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * finish announcement and send start message signal to the clients
 */
public class AnnouncementHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        // This event is triggered when server finishe announcement
        Controller controller = (Controller) server;
        // distribute final reputation map to servers
        List<Pair<BigInteger, BigInteger>> repList = (List<Pair<BigInteger, BigInteger>>) eventMsg.getField("rep_list");
        Map<BigInteger, BigInteger> repMap = new HashMap<BigInteger, BigInteger>();
        for (Pair<BigInteger, BigInteger> pair : repList) {
            repMap.put(pair.getKey(), pair.getValue());
        }

        Map<String, Object> serverMap = new HashMap<String, Object>();
        serverMap.put("rep_map", repMap);
        EventMsg serverMsg = new EventMsg(EventType.SYNC_REPMAP, controller.getIdentifier(), serverMap);
        List<Pair<InetAddress, Integer>> serverList = controller.getServerList();
        for (Pair<InetAddress, Integer> pair : serverList) {
            Utilities.send(controller.getSocket(), Utilities.serialize(serverMsg), pair.getKey(), pair.getValue());
        }
        BigInteger g = (BigInteger) eventMsg.getField("g");
        // distribute g and hash table of ids to user
        Map<String, Object> clientMap = new HashMap<String, Object>();
        //     clientMap.put("client_list",repMap.keySet());
        clientMap.put("g", eventMsg.getField("g"));
        clientMap.put("p", controller.getPrime());
        EventMsg clientMsg = new EventMsg(EventType.ANNOUNCEMENT, controller.getIdentifier(), clientMap);
        Map<BigInteger, Pair<InetAddress, Integer>> clientList = controller.getClientList();
        for (Pair<InetAddress, Integer> pair : clientList.values()) {
            Utilities.send(controller.getSocket(), Utilities.serialize(clientMsg), pair.getKey(), pair.getValue());
        }
        // set controller's new g
        controller.setGenerator(g);
        controller.setStatus(ControllerStatus.MESSAGE);

    }
}
