package controller;

import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import util.Utilities;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 10:28.
 * Package: controller
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Controller extends BaseServer {
    private Topology topology = new Topology();
    private Map<BigInteger, Pair<InetAddress, Integer>> clientAddr = new HashMap<BigInteger, Pair<InetAddress, Integer>>();


    public Controller() throws SocketException, UnknownHostException {
        super();
    }

    public void addServer(InetAddress addr, int port) {
        topology.add(addr, port);
    }

    public Pair<InetAddress, Integer> getLastServer() {
        List<Pair<InetAddress, Integer>> serverList = topology.getServerList();
        return serverList.get(serverList.size() - 1);
    }

    public Pair<InetAddress, Integer> getFirstServer() {
        List<Pair<InetAddress, Integer>> serverList = topology.getServerList();
        return serverList.get(0);
    }

    public void addClient(BigInteger publicKey, Pair<InetAddress, Integer> client) {
        clientAddr.put(publicKey, client);
    }

    public Pair<InetAddress, Integer> getClientAddr(BigInteger publicKey) {
        return clientAddr.get(publicKey);
    }

    public List<Pair<InetAddress, Integer>> getServerList() {
        return topology.getServerList();
    }

    /**
     * start announce phase
     */
    public void announce() {
        Pair<InetAddress, Integer> des = getFirstServer();
        EventMsg eventMsg = new EventMsg(EventType.ANNOUNCEMENT, identifier, new HashMap<String, Object>());
        Utilities.send(socket, Utilities.serialize(eventMsg), des.getKey(), des.getValue());
    }


    public static void main(String[] args) {
        try {
            Controller controller = new Controller();
            ControllerListener controllerListener = new ControllerListener(controller);
            controllerListener.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
