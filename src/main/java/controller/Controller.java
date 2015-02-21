package controller;

import util.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 10:28.
 * Package: controller
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Controller {
    private Topology topology = new Topology();
    private Map<BigInteger, InetAddress> clientAddr = new HashMap<BigInteger, InetAddress>();
    private DatagramSocket socket = null;
    private int localPort = 12345;
    private String identifier = null;
    private String localIp = null;

    public Controller() throws SocketException, UnknownHostException {
        // load properties from config file
        loadProperties();
        localIp = Utilities.getLocalIPAddr();
        identifier = Utilities.getLocalHostname() + ":" + Utilities.getRandomNumber(100000000);
        socket = new DatagramSocket(localPort);
    }

    public void addServer(InetAddress addr) {
        topology.add(addr);
    }

    /**
     * load config from config file
     */
    private void loadProperties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("controller.properties"));
            localPort = Integer.parseInt(prop.getProperty("PORT_NUM"));
        } catch (IOException e) {
            System.out.print("Unable to load controller.properties. We will use default configuration");
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public int getLocalPort() {
        return localPort;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLocalIp() {
        return localIp;
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
