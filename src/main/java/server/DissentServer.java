package server;

import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import util.CommutativeElGamal;
import util.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-20 22:54.
 * Package: server
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class DissentServer extends BaseServer {
    // <public key of client, encrypted reputation>
    private Map<BigInteger, BigInteger> reputationMap = new HashMap<BigInteger, BigInteger>();
    // which contains g, private key, id and public key
    private CommutativeElGamal commutativeElGamal = new CommutativeElGamal();
    // whether the server connects with controller or not
    private boolean connected = false;
    private String controllerIp = null;
    private int controllerPort = 0;
    // next hop server: ip:port
    private Pair<InetAddress, Integer> nextHop = null;

    public DissentServer() throws SocketException, UnknownHostException {
        super();
    }

    /**
     * register itself to controller
     */
    public void register() {
        loadControllerProperties();
        EventMsg eventMsg = new EventMsg(EventType.SERVER_REGISTER, identifier, new HashMap<String, Object>());
        Utilities.send(socket, Utilities.serialize(eventMsg), controllerIp, controllerPort);
    }

    void loadControllerProperties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("server.properties"));
            controllerPort = Integer.parseInt(prop.getProperty("CONTROLLER_PORT"));
            controllerIp = prop.getProperty("CONTROLLER_IP");
            nextHop = new Pair<InetAddress, Integer>(InetAddress.getByName(controllerIp), controllerPort);
        } catch (IOException e) {
            System.out.print("Unable to load controller.properties. We will use default configuration");
        }
    }

    public BigInteger encrypt(BigInteger data) {
        return commutativeElGamal.encrypt(data)[2];
    }
    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Pair<InetAddress, Integer> getNextHop() {
        return nextHop;
    }

    public void setNextHop(Pair<InetAddress, Integer> nextHop) {
        this.nextHop = nextHop;
    }

    public static void main(String[] args) {
        try {
            DissentServer dissentServer = new DissentServer();
            dissentServer.register();
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                if (dissentServer.isConnected())
                    break;
            }
            if (!dissentServer.isConnected()) {
                System.out.println("Fails to connect with controller. Please check the configuration");
                System.exit(1);
            }
            ServerListener serverListener = new ServerListener(dissentServer);
            serverListener.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
