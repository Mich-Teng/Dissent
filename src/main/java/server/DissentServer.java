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
    // controller ip
    private String controllerIp = null;
    // controller port
    private int controllerPort = 0;
    // next hop server: ip:port
    private Pair<InetAddress, Integer> nextHop = null;
    private Pair<InetAddress, Integer> prevHop = null;
    // random private key for each round
    private BigInteger r = null;
    // generator
    private BigInteger g = null;
    // map current public key with previous key
    private Map<BigInteger, BigInteger> keyMap = new HashMap<BigInteger, BigInteger>();
    // value before encryption
    private BigInteger a = null;

    /**
     * Constructor to initialize DissentServer
     * @throws SocketException can't bind to socket
     * @throws UnknownHostException unknown host exception
     */
    public DissentServer() throws SocketException, UnknownHostException {
        super();
        ElGamal elGamal = new ElGamal();
        // generate private key
        r = elGamal.getPrivateKey();
        // generate generator
        // using default value configured in ElGmal
        g = elGamal.getGenerator();
    }

    /**
     * * get generator 
     * @return generator
     */
    public BigInteger getGenerator() {
        return g;
    }

    /**
     * register itself to controller
     */
    public void register() {
        // load controller property data
        loadControllerProperties();
        // send message to controller
        EventMsg eventMsg = new EventMsg(EventType.SERVER_REGISTER, identifier, new HashMap<String, Object>());
        Utilities.send(socket, Utilities.serialize(eventMsg), controllerIp, controllerPort);
    }

    /**
     * * load controller properties files and read attributes 
     */
    void loadControllerProperties() {
        Properties prop = new Properties();
        try {
            // load property files
            prop.load(getClass().getResourceAsStream("/dissent_server.properties"));
            controllerPort = Integer.parseInt(prop.getProperty("CONTROLLER_PORT"));
            controllerIp = prop.getProperty("CONTROLLER_IP");
            // initialize nexthop or prevhop
            nextHop = new Pair<InetAddress, Integer>(InetAddress.getByName(controllerIp), controllerPort);
            prevHop = new Pair<InetAddress, Integer>(InetAddress.getByName(controllerIp), controllerPort);
        } catch (IOException e) {
            System.out.print("Unable to load controller.properties. We will use default configuration");
        }
    }

    /**
     * * get key map (current public key -> previous public key)
     * @return key map
     */
    public Map<BigInteger, BigInteger> getKeyMap() {
        return keyMap;
    }

    /**
     * * add all the entries in keyMap into current keyMap 
     * @param keyMap key map
     */
    public void addKeyMap(Map<BigInteger, BigInteger> keyMap) {
        if (keyMap == null || keyMap.isEmpty())
            return;
        this.keyMap.putAll(keyMap);
    }

    /**
     * * using commutativeElGamal to encrypt data
     * * c = m * (h^y), a = g^y
     * @param data data need to be encrypted
     * @param a a in the formula
     * @param y y in the formula
     * @return encrypted data [a,c]
     */
    public BigInteger[] encrypt(BigInteger data, BigInteger a, BigInteger y) {
        return commutativeElGamal.encrypt(data, a, y);
    }

    /**
     * * using commutativeElGamal to encrypt data
     * @param a g^y
     * @param data encrypted data
     * @return decrypted data
     */
    public BigInteger decrypt(BigInteger a, BigInteger data) {
        BigInteger[] arr = {a, data};
        return commutativeElGamal.decrypt(arr);
    }

    /**
     * * modPow encryption 
     * @param data data need to be encrypted
     * @param p big prime number
     * @return encrypted data
     */
    public BigInteger rsaEncrypt(BigInteger data, BigInteger p) {
        return data.modPow(r, p);
    }

    /**
     * * whether it is connected 
     * @return isConnected
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * * set whether is connected 
     * @param connected isConnected
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * * get nextHop 
     * @return (ip,port) for next hop
     */
    public Pair<InetAddress, Integer> getNextHop() {
        return nextHop;
    }

    /**
     * * set next hop 
     * @param nextHop next hop ip and port
     */
    public void setNextHop(Pair<InetAddress, Integer> nextHop) {
        this.nextHop = nextHop;
    }

    /**
     * * add public key and reputation into reputation map 
     * @param publicKey public key
     * @param rep reputation
     */
    public void addIntoReputationMap(BigInteger publicKey, BigInteger rep) {
        reputationMap.put(publicKey, rep);
    }

    /**
     * * get private key
     * @return private key
     */
    public BigInteger getR() {
        return r;
    }

    /**
     * * set private key 
     * @param r private key
     */
    public void setR(BigInteger r) {
        this.r = r;
    }

    /**
     * * get reputation map 
     * @return reputation map
     */
    public Map<BigInteger, BigInteger> getReputationMap() {
        return reputationMap;
    }

    /**
     * * set reputation map 
     * @param reputationMap reputation map
     */
    public void setReputationMap(Map<BigInteger, BigInteger> reputationMap) {
        this.reputationMap = reputationMap;
    }

    /**
     * * get a, a is the key before encrypted in the announcement phase
     * @return a in the formula
     */
    public BigInteger getA() {
        return a;
    }

    /**
     * * set a, a is the key before encrypted in the announcement phase 
     * @param a a in the formula
     */
    public void setA(BigInteger a) {
        this.a = a;
    }

    /**
     * * get previous hop 
     * @return
     */
    public Pair<InetAddress, Integer> getPrevHop() {
        return prevHop;
    }

    /**
     * * add one entry into key map 
     * @param key key
     * @param val value
     */
    public void addKeyMapEntry(BigInteger key, BigInteger val) {
        keyMap.put(key, val);

    }

    /**
     * * set previous hop 
     * @param prevHop previous hop
     */
    public void setPrevHop(Pair<InetAddress, Integer> prevHop) {
        this.prevHop = prevHop;
    }

    public static void main(String[] args) {
        try {
            DissentServer dissentServer = new DissentServer();
            ServerListener serverListener = new ServerListener(dissentServer);
            // start server listener
            new Thread(serverListener).start();
            // register server to controller
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
