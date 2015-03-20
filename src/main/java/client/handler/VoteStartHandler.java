package client.handler;

import client.ClientStatus;
import client.DissentClient;
import proto.EventMsg;
import template.BaseServer;
import template.Handler;

import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-27 16:38.
 * Package: client.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class VoteStartHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentClient dissentClient = (DissentClient) server;
        if (dissentClient.getStatus() != ClientStatus.MESSAGE)
            return;
        System.out.println("*** [client] Vote Phase begins. Vote using the format... ***");
        System.out.println("vote <msg_id> (+-)1");
        dissentClient.setStatus(ClientStatus.VOTE);
    }
}
