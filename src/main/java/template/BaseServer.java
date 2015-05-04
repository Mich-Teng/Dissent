package template;

import javafx.util.Pair;
import util.ElGamal;
import util.Utilities;

import java.io.IOException;
import java.math.BigInteger;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-22 20:42.
 * Package: PACKAGE_NAME
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * necessary components for server to launch 
 */
public class BaseServer {
    // socket
    protected DatagramSocket socket = null;
    // local port
    protected int localPort = 12345;
    // server unique identifier
    protected String identifier = null;
    // local ip
    protected String localIp = null;
    // client Addr map
    protected Map<BigInteger, Pair<InetAddress, Integer>> clientAddr = new HashMap<BigInteger, Pair<InetAddress, Integer>>();
    // big prime number
    protected final BigInteger p = ElGamal.prime1024;

    /**
     * * constructor for BaseServer 
     * @throws SocketException can't bind to socket
     * @throws UnknownHostException unkown host
     */
    public BaseServer() throws SocketException, UnknownHostException {
        loadProperties();
        localIp = Utilities.getLocalIPAddr();
        // randomly generate a unique identifier
        identifier = Utilities.getLocalHostname() + ":" + Utilities.getRandomNumber(100000000);
        // bind socket to some port available
        for (int i = 0; i <= 10; i++) {
            try {
                socket = new DatagramSocket(localPort + i);
                break;
            } catch (Exception e) {
            }
        }
    }

    /**
     * load config from config file
     */
    private void loadProperties() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("/server.properties"));
            localPort = Integer.parseInt(prop.getProperty("PORT_NUM"));
        } catch (IOException e) {
            System.out.println("Unable to load server.properties. We will use default configuration.");
        }
    }

    /**
     * * add a client into map 
     * @param publicKey public key of client
     * @param client client's ip address and port
     */
    public void addClient(BigInteger publicKey, Pair<InetAddress, Integer> client) {
        // remove all the client with same ip and port
        for(Iterator<Map.Entry<BigInteger,Pair<InetAddress,Integer>>> it = clientAddr.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<BigInteger,Pair<InetAddress,Integer>> entry = it.next();
            if(entry.getValue().equals(client)) {
                it.remove();
            }
        }
        clientAddr.put(publicKey, client);
    }

    /**
     * * get list of clients registered 
     * @return client list
     */
    public Map<BigInteger, Pair<InetAddress, Integer>> getClientList() {
        return clientAddr;
    }

    /**
     * * get client address for a specific client 
     * @param publicKey public key of client
     * @return ip address and port of client
     */
    public Pair<InetAddress, Integer> getClientAddr(BigInteger publicKey) {
        return clientAddr.get(publicKey);
    }

    /**
     * * get socket 
     * @return socket
     */
    public DatagramSocket getSocket() {
        return socket;
    }

    /**
     * * get big prime number 
     * @return big prime number
     */
    public BigInteger getPrime() {
        return p;
    }

    /**
     * * get local port  
     * @return local port
     */
    public int getLocalPort() {
        return localPort;
    }

    /**
     * * get unique identifier 
     * @return unique identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    public String getLocalIp() {
        return localIp;
    }
}
