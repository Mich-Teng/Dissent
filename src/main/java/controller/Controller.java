package controller;

import template.BaseServer;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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

    public void addServer(InetAddress addr) {
        topology.add(addr);
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
