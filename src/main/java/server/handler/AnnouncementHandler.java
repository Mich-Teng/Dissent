package server.handler;

import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import server.DissentServer;
import template.BaseServer;
import template.Handler;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.*;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-25 12:34.
 * Package: server.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class AnnouncementHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentServer dissentServer = (DissentServer) server;
        List<Pair<BigInteger, BigInteger>> list = null;
        BigInteger g = null;
        BigInteger p = null;
        // get or create data from the previous server
        if (eventMsg.getField("g") == null) {
            // the first server in the server sequence
            g = dissentServer.getGenerator();
            p = dissentServer.getPrime();
            Map<BigInteger, BigInteger> repMap = dissentServer.getReputationMap();
            list = new ArrayList<Pair<BigInteger, BigInteger>>();
            for (Map.Entry<BigInteger, BigInteger> entry : repMap.entrySet()) {
                list.add(new Pair<BigInteger, BigInteger>(entry.getKey(), entry.getValue()));
            }
        } else {
            p = (BigInteger) eventMsg.getField("p");
            g = (BigInteger) eventMsg.getField("g");
            list = (List<Pair<BigInteger, BigInteger>>) eventMsg.getField("rep_list");
        }
        // encrypt
        g = dissentServer.rsaEncrypt(g, p);
        List<Pair<BigInteger, BigInteger>> newList = new ArrayList<Pair<BigInteger, BigInteger>>();
        for (Pair<BigInteger, BigInteger> pair : list) {
            // encrypt the public key and decrypt the reputation
            BigInteger newKey = dissentServer.rsaEncrypt(pair.getKey(), p);
            BigInteger decryptRep = dissentServer.decrypt(pair.getValue());
            newList.add(new Pair<BigInteger, BigInteger>(newKey, decryptRep));
        }
        // shuffle the list
        Collections.shuffle(newList);
        // send data to the next server
        Map<String, Object> repMap = new HashMap<String, Object>();
        repMap.put("g", g);
        repMap.put("rep_list", repMap);
        repMap.put("p", dissentServer.getPrime());
        EventMsg repMsg = new EventMsg(EventType.ANNOUNCEMENT, dissentServer.getIdentifier(), repMap);
        Pair<InetAddress, Integer> nextHop = dissentServer.getNextHop();
        Utilities.send(dissentServer.getSocket(), Utilities.serialize(repMsg), nextHop.getKey(), nextHop.getValue());

    }
}
