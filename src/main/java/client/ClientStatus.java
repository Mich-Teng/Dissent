package client;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-27 18:30.
 * Package: client
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ClientStatus {
    // configuration
    public static final int CONFIGURATION = 0;
    // connected with server
    public static final int CONNECTED = 1;
    // in message status, user can send message
    public static final int MESSAGE = 2;
    // in vote status, user can vote
    public static final int VOTE = 3;
}
