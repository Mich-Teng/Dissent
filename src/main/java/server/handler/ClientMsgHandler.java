package server.handler;

import proto.EventMsg;
import template.BaseServer;
import template.Handler;

import java.net.InetAddress;

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
        // Currently just send the msg to all the clients with nym and reputation
    }
}
