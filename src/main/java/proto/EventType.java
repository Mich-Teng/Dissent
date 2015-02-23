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
    public static final int EVENT_NUM = 10;
    // client -> server: request g
    public static final int SERVER_REGISTER = 1;
    public static final int SERVER_REGISTER_REPLY = 2;
    public static final int UPDATE_NEXT_HOP = 3;
    public static final int CLIENT_REGISTER = 4;

}
