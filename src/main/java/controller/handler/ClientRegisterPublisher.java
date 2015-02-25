package controller.handler;

import controller.Controller;
import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import template.Handler;
import util.Utilities;

import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-24 19:47.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ClientRegisterPublisher implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        Controller controller = (Controller) server;
        DatagramSocket socket = controller.getSocket();
        // reply to the client with successful info
        EventMsg clientMsg = new EventMsg(EventType.CLIENT_REGISTER_CONFIRMATION, controller.getIdentifier(), new HashMap<String, Object>());
        BigInteger publicKey = (BigInteger) eventMsg.getField("public_key");
        Pair<InetAddress, Integer> clientAddr = controller.getClientAddr(publicKey);
        Utilities.send(socket, Utilities.serialize(clientMsg), clientAddr.getKey(), clientAddr.getValue());

        // sync data to all the servers
        EventMsg serverMsg = new EventMsg(EventType.ADD_NEWCLIENT, controller.getIdentifier(), eventMsg.getMap());
        List<Pair<InetAddress, Integer>> serverList = controller.getServerList();
        for (Pair<InetAddress, Integer> pair : serverList) {
            Utilities.send(socket, Utilities.serialize(serverMsg), pair.getKey(), pair.getValue());
        }
    }
}
