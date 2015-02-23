package server.handler;

import javafx.util.Pair;
import proto.EventMsg;
import server.DissentServer;
import template.BaseServer;
import template.Handler;

import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-23 10:38.
 * Package: server.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class NextHopHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer baseServer, InetAddress srcAddr, int port) {
        DissentServer dissentServer = (DissentServer) baseServer;
        dissentServer.setNextHop((Pair<InetAddress, Integer>) eventMsg.getField("next_hop"));
    }
}
