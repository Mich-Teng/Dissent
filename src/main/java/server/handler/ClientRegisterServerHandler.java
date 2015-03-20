package server.handler;

import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import server.DissentServer;
import template.BaseServer;
import template.Handler;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-23 12:54.
 * Package: server.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ClientRegisterServerHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentServer dissentServer = (DissentServer) server;
        Pair<InetAddress, Integer> nextHop = dissentServer.getNextHop();


        // get the public key and entrypted reputation
        BigInteger publicKey = (BigInteger) eventMsg.getField("public_key");
        BigInteger reputation = (BigInteger) eventMsg.getField("reputation");
        Pair<InetAddress, Integer> addr = (Pair<InetAddress, Integer>) eventMsg.getField("addr");
        // add address into the server
        dissentServer.addClient(publicKey, addr);
        // commutative encrypt it and send it to next server
        Map<String, Object> map = new HashMap<String, Object>();
        BigInteger[] ret = dissentServer.encrypt(reputation);
        dissentServer.setA(ret[0]);
        map.put("public_key", publicKey);
        map.put("reputation", ret[1]);
        map.put("addr", addr);
        EventMsg msg = new EventMsg(EventType.CLIENT_REGISTER_SERVERSIDE, dissentServer.getIdentifier(), map);
        Utilities.send(dissentServer.getSocket(), Utilities.serialize(msg), nextHop.getKey(), nextHop.getValue());
    }
}
