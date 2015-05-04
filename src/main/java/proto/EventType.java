package proto;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 10:03.
 * Package: proto
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class EventType {
    public static final int EVENT_NUM = 14;
    // server register event
    public static final int SERVER_REGISTER = 1;
    // reply to server register request
    public static final int SERVER_REGISTER_REPLY = 2;
    // update next hop for server
    public static final int UPDATE_NEXT_HOP = 3;
    // register client request to controller
    public static final int CLIENT_REGISTER_CONTROLLERSIDE = 4;
    // register client request to server
    public static final int CLIENT_REGISTER_SERVERSIDE = 5;
    // confirmation for successfully registering client
    public static final int CLIENT_REGISTER_CONFIRMATION = 6;
    // add a new client
    public static final int ADD_NEWCLIENT = 7;
    // announce phase event
    public static final int ANNOUNCEMENT = 8;
    // synchronize reputation map among servers
    public static final int SYNC_REPMAP = 9;
    // message phase event
    public static final int MESSAGE = 10;
    // vote phase event
    public static final int VOTE = 11;
    // round end event
    public static final int ROUND_END = 12;
    // return vote status event
    public static final int VOTE_STATUS = 13;
}
