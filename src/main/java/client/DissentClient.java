package client;

import java.math.BigInteger;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-20 22:54.
 * Package: client
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class DissentClient {
    private BigInteger privateKey = null;
    private BigInteger publicKey = null;
    private BigInteger g = null;


    public DissentClient() {
        // generate private key
    }

    /**
     * register dissent client to the server side
     */
    public void register() {
        // request server side to return g
        // generate public key
        // send public key to server
    }

    public static void main(String[] args) {
        DissentClient client = new DissentClient();
        // register client to server cluster
        client.register();

    }
}
