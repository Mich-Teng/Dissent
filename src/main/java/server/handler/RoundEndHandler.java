package server.handler;

import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import server.DissentServer;
import template.BaseServer;
import template.Config;
import template.Handler;
import util.ElGamal;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.security.SecureRandom;
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
        // get or create data from the previous server
        if (eventMsg.getField("offset") != null) {
            // the first server in the server sequence
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
            List<BigInteger> newClient = (List<BigInteger>) eventMsg.getField("new_client");
            for (BigInteger key : newClient) {
                list.add(new Pair<BigInteger, BigInteger>(key, Config.OFFSET));
            }
        } else {
            list = (List<Pair<BigInteger, BigInteger>>) eventMsg.getField("rep_list");
        }
        Map<BigInteger, BigInteger> keyMap = dissentServer.getKeyMap();
        List<Pair<BigInteger, BigInteger>> newList = new ArrayList<Pair<BigInteger, BigInteger>>();
        // calculate a in ElGmal
        Random rng = new SecureRandom();
        BigInteger y = new BigInteger(dissentServer.getPrime().bitLength() - 1, rng);
        ;
        BigInteger a = dissentServer.getGenerator().modPow(y, dissentServer.getPrime());
        dissentServer.setA(a);
        for (Pair<BigInteger, BigInteger> pair : list) {
            // decrypt the public key and encrypt the reputation back
            BigInteger newKey = keyMap.get(pair.getKey());
            BigInteger[] encryptRep = dissentServer.encrypt(pair.getValue(),a,y);
            newList.add(new Pair<BigInteger, BigInteger>(newKey, encryptRep[1]));
        }
        // shuffle the list
        Collections.shuffle(newList);
        // send data to the next server
        Map<String, Object> repMap = new HashMap<String, Object>();
        repMap.put("rep_list", newList);
        EventMsg repMsg = new EventMsg(EventType.ROUND_END, dissentServer.getIdentifier(), repMap);
        Pair<InetAddress, Integer> prevHop = dissentServer.getPrevHop();
        Utilities.send(dissentServer.getSocket(), Utilities.serialize(repMsg), prevHop.getKey(), prevHop.getValue());
        // reset the private key
        dissentServer.setR(new ElGamal().getPrivateKey());
    }
}
