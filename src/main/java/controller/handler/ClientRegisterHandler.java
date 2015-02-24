package controller.handler;

import controller.Controller;
import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import template.Handler;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 12:21.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ClientRegisterHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer baseServer, InetAddress srcAddr, int srcPort) {
        Controller controller = (Controller) baseServer;
        // add client to current map. Currently, we store this data in controller. However, we could
        // randomly assign a server to deal with the client, which has a better load balance
        // todo
        BigInteger publicKey = (BigInteger) eventMsg.getField("public_key");
        controller.addClient(publicKey, new Pair<InetAddress, Integer>(srcAddr, srcPort));
        // send register info info to the first server
        Pair<InetAddress, Integer> firstServer = controller.getFirstServer();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("public_key", publicKey);
        map.put("reputation", new BigInteger("0"));
        EventMsg msg = new EventMsg(EventType.ENTRYPT_REQUEST, controller.getIdentifier(), map);
        Utilities.send(controller.getSocket(), Utilities.serialize(msg), firstServer.getKey(), firstServer.getValue());
    }
}
