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
import java.security.MessageDigest;
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
    // current round's nym
    private BigInteger oneTimePseudonym = null;
    // current round's g
    private BigInteger g = null;

    private String controllerIp = null;
    private int controllerPort = 0;

    ElGamal elGamal = new ElGamal();


    public DissentClient() throws SocketException, UnknownHostException {
        // generate private key
        elGamal = new ElGamal();
    }

    /**
     * register dissent client to the server side
     */
    public void register() {
        loadClientProperties();
        // send public key to server
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("public_key", getPublicKey());
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
        return elGamal.getPublicKey();
    }


    public BigInteger getPrivateKey() {
        return elGamal.getPrivateKey();
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

    public void sendMsg(String text, Integer msgType) {
        // use private key to encrypt message and the server can use one-time pseudonym to decrypt
        // currently use sha256
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            BigInteger data = new BigInteger(1, hash);
            BigInteger[] signature = elGamal.sign(data);
            // send data to controller
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("signature", signature);
            map.put("text", text);
            map.put("nym", oneTimePseudonym);
            EventMsg msg = new EventMsg(msgType, identifier, map);
            Utilities.send(socket, Utilities.serialize(msg), controllerIp, controllerPort);
        } catch (Exception e) {
            System.out.println("Fails to send Message!");
            e.printStackTrace();
        }
    }

    public void sendMsg(String text) {
        sendMsg(text, EventType.MESSAGE);
    }

    public void vote(Integer msgId, Integer score) {
        // format msgid;score
        sendMsg(msgId + ";" + score, EventType.VOTE);
    }

    public ElGamal getElGamal() {
        return elGamal;
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
