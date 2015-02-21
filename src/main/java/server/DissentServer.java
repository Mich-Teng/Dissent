package server;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-20 22:54.
 * Package: server
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class DissentServer {
    // <public key of client, encrypted reputation>
    Map<BigInteger, BigInteger> reputationMap = new HashMap<BigInteger, BigInteger>();
    private BigInteger g = null;
    private BigInteger privateKey = null;
    private BigInteger publicKey = null;

    public DissentServer() {
        // load g from Config file
        // generate private key
        // generate public key
        // register itself to Master
    }

    public void announce() {

    }

    public static void main(String[] args) {

    }
}
