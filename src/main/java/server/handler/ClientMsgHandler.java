package server.handler;

import javafx.util.Pair;
import proto.EventMsg;
import server.DissentServer;
import template.BaseServer;
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

public class ClientMsgHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentServer dissentServer = (DissentServer) server;
        BigInteger nym = (BigInteger) eventMsg.getField("nym");
        BigInteger rep = dissentServer.getReputationMap().get(nym);
        eventMsg.add("rep", rep);
        System.out.println("Receive msg from " + srcAddr + ":" + port);
        // Currently just send the msg to all the clients with nym and reputation
        Collection<Pair<InetAddress, Integer>> clients = dissentServer.getClientList().values();
        for (Pair<InetAddress, Integer> pair : clients) {
            Utilities.send(dissentServer.getSocket(), Utilities.serialize(eventMsg), pair.getKey(), pair.getValue());
        }
    }
}
