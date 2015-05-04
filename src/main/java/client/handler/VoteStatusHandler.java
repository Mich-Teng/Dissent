package client.handler;

import client.DissentClient;
import com.sun.org.apache.xpath.internal.operations.Bool;
import proto.EventMsg;
import template.BaseServer;
import template.Handler;

import java.math.BigInteger;
import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-04-28 11:19.
 * Package: client.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for VOTE_STATUS event
 * * print out info based on the return packet
 */
public class VoteStatusHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        Boolean status = (Boolean) eventMsg.getField("status");
        if(status == true) {
            System.out.println("Vote success!");
        }else {
            System.out.println("Failure. Duplicate vote or verification fails!");
        }
    }
}
