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

public class MsgHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        BigInteger rep = (BigInteger) eventMsg.getField("rep");
        BigInteger nym = (BigInteger) eventMsg.getField("nym");
        String text = (String) eventMsg.getField("text");
        Integer msgId = (Integer) eventMsg.getField("msgID");

        System.out.println("Message from " + nym.toString() + "(reputation: +" + rep + ")");
        System.out.println("Message ID: " + msgId);
        System.out.println(text);
        System.out.println();
    }
}
