package template;

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
 * Date: 2015-02-22 20:42.
 * Package: PACKAGE_NAME
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class BaseServer {
    private Map<BigInteger, InetAddress> clientAddr = new HashMap<BigInteger, InetAddress>();
    protected DatagramSocket socket = null;
    protected int localPort = 12345;
    protected String identifier = null;
    protected String localIp = null;

    public BaseServer() throws SocketException, UnknownHostException {
        loadProperties();
        localIp = Utilities.getLocalIPAddr();
        identifier = Utilities.getLocalHostname() + ":" + Utilities.getRandomNumber(100000000);
        socket = new DatagramSocket(localPort);
    }

    /**
     * load config from config file
     */
    private void loadProperties() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("server.properties"));
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
}
