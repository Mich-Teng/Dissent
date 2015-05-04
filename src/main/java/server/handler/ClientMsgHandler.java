package server.handler;

import javafx.util.Pair;
import proto.EventMsg;
import server.DissentServer;
import template.BaseServer;
import template.Config;
import template.Handler;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Collection;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-25 17:12.
 * Package: server.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for MESSAGE event
 * * broadcast messages to all clients
 */
public class ClientMsgHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentServer dissentServer = (DissentServer) server;
        // get the One-time pseudo name from client
        BigInteger nym = (BigInteger) eventMsg.getField("nym");
        BigInteger rep = dissentServer.getReputationMap().get(nym);
        // subtract the offset
        eventMsg.add("rep", rep.subtract(Config.OFFSET));
        // print out debug info
        System.out.println("[server] Receiving msg from " + srcAddr + ":" + port);
        // Currently just send the msg to all the clients with nym and reputation
        Collection<Pair<InetAddress, Integer>> clients = dissentServer.getClientList().values();
        for (Pair<InetAddress, Integer> pair : clients) {
            Utilities.send(dissentServer.getSocket(), Utilities.serialize(eventMsg), pair.getKey(), pair.getValue());
        }
    }
}
