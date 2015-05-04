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
 * Date: 2015-02-24 20:05.
 * Package: client.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for REGISTER CONFIRMATION EVENT 
 */
public class RegisterConfirmationHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentClient dissentClient = (DissentClient) server;
        // set current status of client
        dissentClient.setStatus(ClientStatus.CONNECTED);
        // simply print out register success info here
        System.out.println("[client] Register success. Waiting for new round begin...");
    }
}
