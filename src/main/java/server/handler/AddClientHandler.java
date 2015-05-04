package server.handler;

import proto.EventMsg;
import server.DissentServer;
import template.BaseServer;
import template.Handler;

import java.math.BigInteger;
import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-24 20:01.
 * Package: server.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for ADD_CLIENT event
 */
public class AddClientHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentServer dissentServer = (DissentServer) server;
        // add the public key and encrypted reputation into map
        BigInteger publicKey = (BigInteger) eventMsg.getField("public_key");
        BigInteger reputation = (BigInteger) eventMsg.getField("reputation");
        dissentServer.addIntoReputationMap(publicKey, reputation);
    }
}
