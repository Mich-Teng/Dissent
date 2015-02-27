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
 * Date: 2015-02-27 16:52.
 * Package: server.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class RoundEndHandler implements Handler {
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
            // update the reputation map based on the offset
            Map<BigInteger, BigInteger> voteOffset = (Map<BigInteger, BigInteger>) eventMsg.getField("offset");
            list = new ArrayList<Pair<BigInteger, BigInteger>>();
            for (Map.Entry<BigInteger, BigInteger> entry : repMap.entrySet()) {
                BigInteger key = entry.getKey();
                if (voteOffset.containsKey(key)) {
                    // use voteOffset to update the reputation
                    list.add(new Pair<BigInteger, BigInteger>(key, entry.getValue().add(voteOffset.get(key))));
                } else {
                    list.add(new Pair<BigInteger, BigInteger>(key, entry.getValue()));
                }
            }
        } else {
            p = (BigInteger) eventMsg.getField("p");
            g = (BigInteger) eventMsg.getField("g");
            list = (List<Pair<BigInteger, BigInteger>>) eventMsg.getField("rep_list");
        }
        g = dissentServer.rsaDecrypt(g, p);
        List<Pair<BigInteger, BigInteger>> newList = new ArrayList<Pair<BigInteger, BigInteger>>();
        for (Pair<BigInteger, BigInteger> pair : list) {
            // decrypt the public key and encrypt the reputation back
            BigInteger newKey = dissentServer.rsaDecrypt(pair.getKey(), p);
            BigInteger encryptRep = dissentServer.encrypt(pair.getValue());
            newList.add(new Pair<BigInteger, BigInteger>(newKey, encryptRep));
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
