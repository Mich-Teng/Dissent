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
 * Date: 2015-02-27 16:53.
 * Package: client.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for ROUND_END event
 * * reset the status and prepare for the new round 
 */
public class RoundEndHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentClient dissentClient = (DissentClient) server;
        // reset the status
        dissentClient.setStatus(ClientStatus.CONNECTED);
        // print out info in the client side
        System.out.println("[client] Round ended. Waiting for new round start...");
        System.out.println("Please wait for the next round to start");
    }
}
