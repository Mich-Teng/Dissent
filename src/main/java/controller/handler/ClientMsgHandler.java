package controller.handler;

import controller.Controller;
import javafx.util.Pair;
import proto.EventMsg;
import template.BaseServer;
import template.Handler;
import util.ElGamal;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-25 17:10.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * Handler for MESSAGE event
 * * accept the message from client, verify the identity and deliver to one server to process 
 */
public class ClientMsgHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        Controller controller = (Controller) server;
        // get info from the request
        BigInteger g = controller.getGenerator();
        String text = (String) eventMsg.getField("text");
        BigInteger[] signature = (BigInteger[]) eventMsg.getField("signature");
        BigInteger nym = (BigInteger) eventMsg.getField("nym");
        eventMsg.remove("signature");
        // print out debug info
        System.out.println("[debug] Receiving msg from " + srcAddr + ":" + port + ": " + text);
        // verify the identification of the client
        try {
            // hash the message
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            BigInteger data = new BigInteger(1, hash);
            // verification
            if (ElGamal.verify(nym, data, signature[0], signature[1], g, controller.getPrime())) {
                // the client pass the verification, randomly pick a server and deal with it
                // generate msg id
                Integer msgID = controller.addMsgLog(nym);
                eventMsg.add("msgID", msgID);
                // randomly send it to a server
                Random random = new Random();
                List<Pair<InetAddress, Integer>> serverList = controller.getServerList();
                int index = random.nextInt(serverList.size());
                Pair<InetAddress, Integer> selectedServer = serverList.get(index);
                Utilities.send(controller.getSocket(), Utilities.serialize(eventMsg), selectedServer.getKey(), selectedServer.getValue());
            }
        } catch (Exception e) {
            System.out.println("Fail to calculate hash of text!");
        }
    }
}
