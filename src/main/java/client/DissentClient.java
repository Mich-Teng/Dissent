package client;

import proto.EventMsg;
import proto.EventType;
import template.BaseServer;
import util.ElGamal;
import util.Utilities;

import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

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
    // controller ip
    private String controllerIp = null;
    // controller port
    private int controllerPort = 0;
    // initialize status to Configuration
    private int status = ClientStatus.CONFIGURATION;
    // encryption object
    ElGamal elGamal = new ElGamal();


    /**
     * * initialize the client 
     * @throws SocketException fails to open socket for the server
     * @throws UnknownHostException fails to find host
     */
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

    /**
     * * load initial client properties
     */
    void loadClientProperties() {
        Properties prop = new Properties();
        try {
            // load properties file
            prop.load(getClass().getResourceAsStream("/client.properties"));
            controllerPort = Integer.parseInt(prop.getProperty("CONTROLLER_PORT"));
            controllerIp = prop.getProperty("CONTROLLER_IP");
        } catch (IOException e) {
            System.out.println("Unable to load client.properties. We will use default configuration.");
        }
    }

    /**
     * * get public key 
     * @return client's public key
     */
    public BigInteger getPublicKey() {
        return elGamal.getPublicKey();
    }

    /**
     * * get private key 
     * @return client's private key
     */
    public BigInteger getPrivateKey() {
        return elGamal.getPrivateKey();
    }

    /**
     * * get client's One-time pseudonym 
     * @return One-time pseudonym
     */
    public BigInteger getOneTimePseudonym() {
        return oneTimePseudonym;
    }

    /**
     * * set One-time pseudonym 
     * @param oneTimePseudonym One-time pseudonym
     */
    public void setOneTimePseudonym(BigInteger oneTimePseudonym) {
        this.oneTimePseudonym = oneTimePseudonym;
    }

    /**
     * * get generator(shared with servers) 
     * @return generator
     */
    public BigInteger getG() {
        return g;
    }

    /**
     * * set generator(in each round) 
     * @param g new generator
     */
    public void setG(BigInteger g) {
        this.g = g;
    }

    /**
     * * broadcast message to all clients
     * @param text msg text
     * @param msgType msg type
     */
    public void sendMsg(String text, Integer msgType) {
        try {
            // use private key to encrypt message and the server can use one-time pseudonym to decrypt
            // currently use sha256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes("UTF-8"));
            BigInteger data = new BigInteger(1, hash);
            BigInteger[] signature = elGamal.sign(data, g);
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

    /**
     * * wrapper function for sendMsg 
     * @param text msg text
     */
    public void sendMsg(String text) {
        sendMsg(text, EventType.MESSAGE);
    }

    /**
     * * send vote to server 
     * @param msgId msg id
     * @param score +-1
     */
    public void vote(Integer msgId, Integer score) {
        // format msgid;score
        if(score > 0) {
            sendMsg(msgId + ";" + 1, EventType.VOTE);
        }else {
            sendMsg(msgId + ";" + -1, EventType.VOTE);
        }
    }

    /**
     * * set current client status 
     * @param status client status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * * get client status 
     * @return status of client
     */
    public Integer getStatus() {
        return status;
    }

    public ElGamal getElGamal() {
        return elGamal;
    }

    public static void main(String[] args) {
        try {
            DissentClient client = new DissentClient();
            // print out client's public key
            System.out.println("[client] Public key:");
            System.out.println(client.getPublicKey());
            // register client to server cluster
            ClientListener clientListener = new ClientListener(client);
            new Thread(clientListener).start();
            System.out.println("[client] Listener launched...");
            // initialize the status
            client.setStatus(ClientStatus.CONFIGURATION);
            // register client to the server cluster
            client.register();
            // waiting for the next round
            while (!(client.getStatus() == ClientStatus.MESSAGE))
                Thread.sleep(500);
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String str = scanner.nextLine();
                String[] cmdArgs = str.split(" ");
                if (cmdArgs[0].equals("msg"))
                    client.sendMsg(cmdArgs[1]);
                else if (cmdArgs[0].equals("vote"))
                    client.vote(Integer.parseInt(cmdArgs[1]), Integer.parseInt(cmdArgs[2]));
                else
                    throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
