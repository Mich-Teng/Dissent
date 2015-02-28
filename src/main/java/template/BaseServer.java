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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-22 20:42.
 * Package: PACKAGE_NAME
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class BaseServer {
    protected DatagramSocket socket = null;
    protected int localPort = 12345;
    protected String identifier = null;
    protected String localIp = null;
    protected Map<BigInteger, Pair<InetAddress, Integer>> clientAddr = new HashMap<BigInteger, Pair<InetAddress, Integer>>();
    // big prime number
    protected final BigInteger p = ElGamal.prime1024;

    public BaseServer() throws SocketException, UnknownHostException {
        loadProperties();
        localIp = Utilities.getLocalIPAddr();
        identifier = Utilities.getLocalHostname() + ":" + Utilities.getRandomNumber(100000000);
        for (int i = 0; i <= 5; i++) {
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

    public void addClient(BigInteger publicKey, Pair<InetAddress, Integer> client) {
        clientAddr.put(publicKey, client);
    }

    public Map<BigInteger, Pair<InetAddress, Integer>> getClientList() {
        return clientAddr;
    }

    public Pair<InetAddress, Integer> getClientAddr(BigInteger publicKey) {
        return clientAddr.get(publicKey);
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public BigInteger getPrime() {
        return p;
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
}
