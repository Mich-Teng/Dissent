package server.handler;

import proto.EventMsg;
import server.DissentServer;
import template.BaseServer;
import template.Handler;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-25 16:40.
 * Package: server.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class SyncRepHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentServer dissentServer = (DissentServer) server;
        Map<BigInteger, BigInteger> repMap = (Map<BigInteger, BigInteger>) eventMsg.getField("rep_map");
        // reset the reputation map for this server
        dissentServer.setReputationMap(repMap);
    }
}
