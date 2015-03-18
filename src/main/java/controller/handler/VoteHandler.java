package controller.handler;

import controller.Controller;
import proto.EventMsg;
import template.BaseServer;
import template.Handler;
import util.ElGamal;

import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-27 16:27.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class VoteHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        // collect the result in a temp variable, almost the same with msg handler
        // verify the identification of the client
        Controller controller = (Controller) server;
        BigInteger g = controller.getGenerator();
        String text = (String) eventMsg.getField("text");
        BigInteger[] signature = (BigInteger[]) eventMsg.getField("signature");
        BigInteger nym = (BigInteger) eventMsg.getField("nym");
        eventMsg.remove("signature");
        try {
            // hash the message
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            BigInteger data = new BigInteger(1, hash);
            // verification
            if (ElGamal.verify(nym, data, signature[0], signature[1], g, controller.getPrime())) {
                // the client pass the verification, collect vote
                // args[0] is msgid, args[1] is vote
                String[] args = text.split(";");
                BigInteger targetNym = controller.getMsgNym(Integer.parseInt(args[0]));
                controller.addVote(targetNym, new BigInteger(args[1]));
            }
        } catch (Exception e) {
            System.out.println("Fail to calculate hash of text!");
        }
    }
}
