package controller.handler;

import controller.Controller;
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
 * Date: 2015-02-27 19:53.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class RoundEndHandler implements Handler {
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
        // send user round-end message
        EventMsg clientMsg = new EventMsg(EventType.ROUND_END, controller.getIdentifier(), new HashMap<String, Object>());
        Map<BigInteger, Pair<InetAddress, Integer>> clientList = controller.getClientList();
        for (Pair<InetAddress, Integer> pair : clientList.values()) {
            Utilities.send(controller.getSocket(), Utilities.serialize(clientMsg), pair.getKey(), pair.getValue());
        }
    }
}
