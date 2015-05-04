package controller.handler;

import controller.Controller;
import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import template.Handler;
import util.ElGamal;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-27 16:27.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for VOTE event 
 */
public class VoteHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int srcPort) {
        // collect the result in a temp variable, almost the same with msg handler
        Controller controller = (Controller) server;
        // get necessary info from request
        BigInteger g = controller.getGenerator();
        String text = (String) eventMsg.getField("text");
        BigInteger[] signature = (BigInteger[]) eventMsg.getField("signature");
        BigInteger nym = (BigInteger) eventMsg.getField("nym");
        eventMsg.remove("signature");
        Map<String, Object> reply = new HashMap<String, Object>();
        reply.put("status", false);
        // verify the identification of the client
        try {
            // hash the message
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            BigInteger data = new BigInteger(1, hash);
            // verification
            if (ElGamal.verify(nym, data, signature[0], signature[1], g, controller.getPrime())) {
                // the client pass the verification, collect vote args[0] is msgid, args[1] is vote
                String[] args = text.split(";");
                Integer msgId = Integer.parseInt(args[0]);
                // return the true status
                if(!controller.voteLog.contains(new Pair<BigInteger, Integer>(nym,msgId))) {
                    BigInteger targetNym = controller.getMsgNym(msgId);
                    controller.addVote(targetNym, new BigInteger(args[1]));
                    reply.put("status",true);
                    controller.voteLog.add(new Pair<BigInteger, Integer>(nym,msgId));
                }
            }
        } catch (Exception e) {
            System.out.println("Fail to calculate hash of text!");
        }
        // send confirmation to user-side
        EventMsg replyMsg = new EventMsg(EventType.VOTE_STATUS, controller.getIdentifier(), reply);
        Utilities.send(controller.getSocket(), Utilities.serialize(replyMsg), srcAddr, srcPort);
    }
}
