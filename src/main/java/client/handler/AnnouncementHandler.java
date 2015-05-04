package client.handler;

import client.ClientStatus;
import client.DissentClient;
import proto.EventMsg;
import template.BaseServer;
import template.Handler;

import java.math.BigInteger;
import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-25 16:44.
 * Package: client.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * Handle the announcement event in client-side
 * get One-time pseudonym from server and start message phase
 */
public class AnnouncementHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentClient dissentClient = (DissentClient) server;
        // get shared g for this round from server
        BigInteger g = (BigInteger) eventMsg.getField("g");
        BigInteger p = (BigInteger) eventMsg.getField("p");
        // calculate One-time pseudonym based on y = g^x mod p
        BigInteger oneTimePseudonym = g.modPow(dissentClient.getPrivateKey(), p);
        // set this round's One-time pseudonym
        dissentClient.setOneTimePseudonym(oneTimePseudonym);
        dissentClient.setG(g);
        // print out the msg to suggest user to send msg or vote
        System.out.println("[client] One-Time pseudonym for this round is ");
        System.out.println(oneTimePseudonym);
        System.out.println("*** [client] Message Phase begins. Sending msg using the format... ***");
        System.out.println("msg <msg_text>");
        dissentClient.setStatus(ClientStatus.MESSAGE);
    }
}
