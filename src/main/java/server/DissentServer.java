package server;

import javafx.util.Pair;
import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import util.ElGamal;
import util.Utilities;

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
    private ElGamal commutativeElGamal = new ElGamal();
    // whether the server connects with controller or not
    private boolean connected = false;
    private String controllerIp = null;
    private int controllerPort = 0;
    // next hop server: ip:port
    private Pair<InetAddress, Integer> nextHop = null;
    private Pair<InetAddress, Integer> prevHop = null;
    // random private key for each round
    private BigInteger r = null;
    // generator
    private BigInteger g = null;
    // map current public key with previous key
    private Map<BigInteger, BigInteger> keyMap = null;

    private BigInteger a = null;


    public DissentServer() throws SocketException, UnknownHostException {
        super();
        ElGamal elGamal = new ElGamal();
        r = elGamal.getPrivateKey();
        g = elGamal.getGenerator();
    }

    public BigInteger getGenerator() {
        return g;
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
            prop.load(getClass().getResourceAsStream("/dissent_server.properties"));
            controllerPort = Integer.parseInt(prop.getProperty("CONTROLLER_PORT"));
            controllerIp = prop.getProperty("CONTROLLER_IP");
            nextHop = new Pair<InetAddress, Integer>(InetAddress.getByName(controllerIp), controllerPort);
            prevHop = new Pair<InetAddress, Integer>(InetAddress.getByName(controllerIp), controllerPort);
        } catch (IOException e) {
            System.out.print("Unable to load controller.properties. We will use default configuration");
        }
    }

    public Map<BigInteger, BigInteger> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<BigInteger, BigInteger> keyMap) {
        this.keyMap = keyMap;
    }

    public BigInteger[] encrypt(BigInteger data) {
        return commutativeElGamal.encrypt(data);
    }

    public BigInteger decrypt(BigInteger a, BigInteger data) {
        BigInteger[] arr = {a, data};
        return commutativeElGamal.decrypt(arr);
    }

    public BigInteger rsaEncrypt(BigInteger data, BigInteger p) {
        return data.modPow(r, p);
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

    public void addIntoReputationMap(BigInteger publicKey, BigInteger rep) {
        reputationMap.put(publicKey, rep);
    }

    public BigInteger getR() {
        return r;
    }

    public void setR(BigInteger r) {
        this.r = r;
    }

    public Map<BigInteger, BigInteger> getReputationMap() {
        return reputationMap;
    }

    public void setReputationMap(Map<BigInteger, BigInteger> reputationMap) {
        this.reputationMap = reputationMap;
    }

    public BigInteger getA() {
        return a;
    }

    public void setA(BigInteger a) {
        this.a = a;
    }

    public Pair<InetAddress, Integer> getPrevHop() {
        return prevHop;
    }

    public void setPrevHop(Pair<InetAddress, Integer> prevHop) {
        this.prevHop = prevHop;
    }

    public static void main(String[] args) {
        try {
            DissentServer dissentServer = new DissentServer();
            ServerListener serverListener = new ServerListener(dissentServer);
            new Thread(serverListener).start();
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
            System.out.println("[server] Register success...");
            while (true) {
                Thread.sleep(1000000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
