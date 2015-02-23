package server.handler;

import proto.EventMsg;
import server.DissentServer;
import template.BaseServer;
import template.Handler;

import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-22 21:22.
 * Package: server.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ConfirmationHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int srcPort) {
        DissentServer dissentServer = (DissentServer) server;
        // get the reply from the package
        Boolean confirmation = (Boolean) eventMsg.getField("reply");
        if (confirmation) {
            dissentServer.setConnected(true);
        }
    }
}
