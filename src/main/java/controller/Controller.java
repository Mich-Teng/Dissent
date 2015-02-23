package controller;

import javafx.util.Pair;
import template.BaseServer;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

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
