package client;

import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import util.ElGamal;
import util.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-20 22:54.
 * Package: client
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class DissentClient extends BaseServer {
    private BigInteger privateKey = null;
    private BigInteger publicKey = null;
    // current round's nym
    private BigInteger oneTimePseudonym = null;
    // current round's g
    private BigInteger g = null;

    private String controllerIp = null;
    private int controllerPort = 0;


    public DissentClient() throws SocketException, UnknownHostException {
        // generate private key
        ElGamal elGamal = new ElGamal();
        privateKey = elGamal.getPrivateKey();
        publicKey = elGamal.getPublicKey();
    }

    /**
     * register dissent client to the server side
     */
    public void register() {
        loadClientProperties();
        // send public key to server
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("public_key", publicKey);
        EventMsg eventMsg = new EventMsg(EventType.CLIENT_REGISTER_CONTROLLERSIDE, identifier, map);
        Utilities.send(socket, Utilities.serialize(eventMsg), controllerIp, controllerPort);
    }

    void loadClientProperties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("client.properties"));
            controllerPort = Integer.parseInt(prop.getProperty("CONTROLLER_PORT"));
            controllerIp = prop.getProperty("CONTROLLER_IP");
        } catch (IOException e) {
            System.out.print("Unable to load controller.properties. We will use default configuration");
        }
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public BigInteger getOneTimePseudonym() {
        return oneTimePseudonym;
    }

    public void setOneTimePseudonym(BigInteger oneTimePseudonym) {
        this.oneTimePseudonym = oneTimePseudonym;
    }

    public BigInteger getG() {
        return g;
    }

    public void setG(BigInteger g) {
        this.g = g;
    }

    public void sendMsg(String text) {
        // use private key to encrypt message and the server can use one-time pseudonym to decrypt
        // send data to controller
        // todo
    }

    public void vote(Integer msgId, Integer score) {
        // todo
    }

    public static void main(String[] args) {
        try {
            DissentClient client = new DissentClient();
            // register client to server cluster
            client.register();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
