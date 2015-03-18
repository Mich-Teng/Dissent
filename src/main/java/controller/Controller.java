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
import java.util.*;

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
    private BigInteger g = null;
    List<BigInteger> msgSenderList = new ArrayList<BigInteger>();
    Map<BigInteger, BigInteger> voteCollect = new HashMap<BigInteger, BigInteger>();
    private int status = ControllerStatus.CONFIGURATION;
    Map<BigInteger, BigInteger> newClientBuffer = new HashMap<BigInteger, BigInteger>();
    Map<InetAddress, Integer> newServerBuffer = new HashMap<InetAddress, Integer>();


    public Controller() throws SocketException, UnknownHostException {
        super();
    }

    public void addServer(InetAddress addr, int port) {
        topology.add(addr, port);
    }

    public void addServerIntoBuffer(InetAddress addr, Integer port) {
        newServerBuffer.put(addr, port);
    }

    public void clear() {
        voteCollect.clear();
        newClientBuffer.clear();
        newServerBuffer.clear();
        msgSenderList.clear();
    }

    public Pair<InetAddress, Integer> getLastServer() {
        List<Pair<InetAddress, Integer>> serverList = topology.getServerList();
        if (serverList == null || serverList.size() == 0)
            return null;
        return serverList.get(serverList.size() - 1);
    }

    public Pair<InetAddress, Integer> getFirstServer() {
        List<Pair<InetAddress, Integer>> serverList = topology.getServerList();
        if (serverList == null || serverList.size() == 0)
            return null;
        return serverList.get(0);
    }

    public Integer addMsgLog(BigInteger oneTimePseunym) {
        msgSenderList.add(oneTimePseunym);
        return msgSenderList.size();
    }

    public BigInteger getMsgNym(Integer msgId) {
        return msgSenderList.get(msgId - 1);
    }

    public List<Pair<InetAddress, Integer>> getServerList() {
        return topology.getServerList();
    }

    /**
     * start announce phase
     */
    public void announce() {
        Pair<InetAddress, Integer> des = getFirstServer();
        if (des == null) {
            status = ControllerStatus.MESSAGE;
            return;
        }
        EventMsg eventMsg = new EventMsg(EventType.ANNOUNCEMENT, identifier, new HashMap<String, Object>());
        Utilities.send(socket, Utilities.serialize(eventMsg), des.getKey(), des.getValue());
    }

    /**
     * start vote phase, actually, if we partition the clients to servers,
     * we can let server send this signal to clients. Here, for simplicity, we
     * just send it from controller
     */
    public void vote() {
        EventMsg eventMsg = new EventMsg(EventType.VOTE, identifier, new HashMap<String, Object>());
        Collection<Pair<InetAddress, Integer>> pairs = clientAddr.values();
        byte[] content = Utilities.serialize(eventMsg);
        for (Pair<InetAddress, Integer> pair : pairs) {
            Utilities.send(socket, content, pair.getKey(), pair.getValue());
        }
    }

    /**
     * round end. wrap up vote and send signal to clients
     */
    public void voteEnd() {
        // send signal to all clients
        EventMsg eventMsg = new EventMsg(EventType.ROUND_END, identifier, new HashMap<String, Object>());
        Collection<Pair<InetAddress, Integer>> pairs = clientAddr.values();
        byte[] content = Utilities.serialize(eventMsg);
        for (Pair<InetAddress, Integer> pair : pairs) {
            Utilities.send(socket, content, pair.getKey(), pair.getValue());
        }

        // send signal to server
        eventMsg.add("offset", voteCollect);
        Pair<InetAddress, Integer> firstServer = getFirstServer();
        if (firstServer == null) {
            status = ControllerStatus.READY_FOR_NEW_ROUND;
            return;
        }
        Utilities.send(socket, Utilities.serialize(eventMsg), firstServer.getKey(), firstServer.getValue());
    }

    public void addVote(BigInteger oneTimePseunym, BigInteger vote) {
        if (!voteCollect.containsKey(oneTimePseunym)) {
            voteCollect.put(oneTimePseunym, vote);
        } else {
            voteCollect.put(oneTimePseunym, voteCollect.get(oneTimePseunym).add(vote));
        }
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigInteger getGenerator() {
        return g;
    }

    public void setGenerator(BigInteger g) {
        this.g = g;

    }

    public Integer getStatus() {
        return status;
    }

    public void addNewClientIntoBuffer(BigInteger publicKey, BigInteger rep) {
        newClientBuffer.put(publicKey, rep);
    }

    public Map<BigInteger, BigInteger> getNewClientBuffer() {
        return newClientBuffer;
    }


    private void registerNewServer() {
        for (Map.Entry<InetAddress, Integer> entry : newServerBuffer.entrySet()) {
            addServer(entry.getKey(), entry.getValue());
        }
    }

    public static void main(String[] args) {
        try {
            Controller controller = new Controller();
            ControllerListener controllerListener = new ControllerListener(controller);
            new Thread(controllerListener).start();
            controller.setStatus(ControllerStatus.READY_FOR_NEW_ROUND);
            while (true) {
                for (int i = 0; i < 100; i++) {
                    if (controller.getStatus() == ControllerStatus.READY_FOR_NEW_ROUND)
                        break;
                    Thread.sleep(1000);
                }
                controller.registerNewServer();
                controller.clear();
                System.out.println("******************** New round begin ********************");
                if (!(controller.getStatus() == ControllerStatus.READY_FOR_NEW_ROUND))
                    throw new Exception("Fail to be ready for the new round");
                controller.setStatus(ControllerStatus.ANNOUNCE);
                System.out.println("> Start announcement phase...");
                controller.announce();

                for (int i = 0; i < 100; i++) {
                    if (controller.getStatus() == ControllerStatus.MESSAGE)
                        break;
                    Thread.sleep(1000);
                }
                if (!(controller.getStatus() == ControllerStatus.MESSAGE))
                    throw new Exception("Fail to be ready for message phase");
                System.out.println("> Start messaging phase...");
                // 10 secs for msg
                Thread.sleep(10000);
                System.out.println("> Start voting phase...");
                controller.vote();
                // 10 secs for vote
                Thread.sleep(10000);
                controller.voteEnd();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
