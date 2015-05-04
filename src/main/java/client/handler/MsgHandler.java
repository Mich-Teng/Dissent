package client.handler;

import proto.EventMsg;
import template.BaseServer;
import template.Handler;

import java.math.BigInteger;
import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-27 12:07.
 * Package: client.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for MESSAGE event.
 * * receive the One-time pseudonym, reputation, and msg from server side 
 */
public class MsgHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        // get the reputation
        BigInteger rep = (BigInteger) eventMsg.getField("rep");
        // get One-time pseudonym
        BigInteger nym = (BigInteger) eventMsg.getField("nym");
        // get msg text
        String text = (String) eventMsg.getField("text");
        // get msg id
        Integer msgId = (Integer) eventMsg.getField("msgID");

        // print out in client side
        System.out.println("Message from " + nym.toString() + "(reputation: " + rep + ")");
        System.out.println("Message ID: " + msgId);
        System.out.println(text);
        System.out.println();
    }
}
