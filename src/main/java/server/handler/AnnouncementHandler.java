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

/**
 * * Handler for announcement phase
 */
public class AnnouncementHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port) {
        DissentServer dissentServer = (DissentServer) server;
        List<Pair<BigInteger, BigInteger>> list = null;
        BigInteger g = null;
        // get or create data from the previous server
        if (eventMsg.getField("g") == null) {
            // if I am the first server in the server sequence
            g = dissentServer.getGenerator();
            // initialize the reputation map
            Map<BigInteger, BigInteger> repMap = dissentServer.getReputationMap();
            list = new ArrayList<Pair<BigInteger, BigInteger>>();
            for (Map.Entry<BigInteger, BigInteger> entry : repMap.entrySet()) {
                list.add(new Pair<BigInteger, BigInteger>(entry.getKey(), entry.getValue()));
            }
        } else {
            // get the data from the previous server
            g = (BigInteger) eventMsg.getField("g");
            list = (List<Pair<BigInteger, BigInteger>>) eventMsg.getField("rep_list");
        }
        // encrypt g by modPow
        Map<BigInteger, BigInteger> keyMap = new HashMap<BigInteger, BigInteger>();
        g = dissentServer.rsaEncrypt(g, dissentServer.getPrime());
        List<Pair<BigInteger, BigInteger>> newList = new ArrayList<Pair<BigInteger, BigInteger>>();
        for (Pair<BigInteger, BigInteger> pair : list) {
            // encrypt the public key using modPow
            BigInteger newKey = dissentServer.rsaEncrypt(pair.getKey(), dissentServer.getPrime());
            // decrypt the reputation using ElGamal algorithm
            BigInteger decryptRep = dissentServer.decrypt(dissentServer.getA(), pair.getValue());
            keyMap.put(newKey, pair.getKey());
            // add the encrypted public key and decrypted reputation into the package
            newList.add(new Pair<BigInteger, BigInteger>(newKey, decryptRep));
        }
        dissentServer.addKeyMap(keyMap);
        // shuffle the list
        Collections.shuffle(newList);
        
        // send data to the next server
        Map<String, Object> repMap = new HashMap<String, Object>();
        repMap.put("g", g);
        repMap.put("rep_list", newList);
        EventMsg repMsg = new EventMsg(EventType.ANNOUNCEMENT, dissentServer.getIdentifier(), repMap);
        Pair<InetAddress, Integer> nextHop = dissentServer.getNextHop();
        Utilities.send(dissentServer.getSocket(), Utilities.serialize(repMsg), nextHop.getKey(), nextHop.getValue());
    }
}
