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

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-24 19:47.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for CLIENT_REGISTER_SERVERSIDE event 
 * * publish the client data to all the servers
 */
public class ClientRegisterPublisher implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        Controller controller = (Controller) server;
        DatagramSocket socket = controller.getSocket();
        // reply to the client with successful info
        EventMsg clientMsg = new EventMsg(EventType.CLIENT_REGISTER_CONFIRMATION, controller.getIdentifier(), new HashMap<String, Object>());
        BigInteger publicKey = (BigInteger) eventMsg.getField("public_key");
        Pair<InetAddress, Integer> addr = (Pair<InetAddress, Integer>) eventMsg.getField("addr");
        Utilities.send(socket, Utilities.serialize(clientMsg), addr.getKey(), addr.getValue());
        // instead of sending new client to server, we will send it when finishing this round. Currently we just add it into buffer
        controller.addNewClientIntoBuffer(publicKey);
        /*
        eventMsg.add("addr", clientAddr);
        // sync data to all the servers
        EventMsg serverMsg = new EventMsg(EventType.ADD_NEWCLIENT, controller.getIdentifier(), eventMsg.getMap());
        List<Pair<InetAddress, Integer>> serverList = controller.getServerList();
        for (Pair<InetAddress, Integer> pair : serverList) {
            Utilities.send(socket, Utilities.serialize(serverMsg), pair.getKey(), pair.getValue());
        }*/
    }
}
