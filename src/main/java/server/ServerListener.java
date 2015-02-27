package server;

import client.handler.RoundEndHandler;
import proto.EventType;
import server.handler.*;
import template.BaseServerListener;
import template.Handler;


/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-22 20:34.
 * Package: server
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ServerListener extends BaseServerListener {

    public ServerListener(DissentServer dissentServer) {
        super(dissentServer);
    }

    @Override
    protected void assignHandlers(Handler[] handlers) {
        // assign the handlers for dissent server
        handlers[EventType.SERVER_REGISTER_REPLY] = new ConfirmationHandler();
        handlers[EventType.UPDATE_NEXT_HOP] = new NextHopHandler();
        handlers[EventType.CLIENT_REGISTER_SERVERSIDE] = new ClientRegisterServerHandler();
        handlers[EventType.SYNC_REPMAP] = new SyncRepHandler();
        handlers[EventType.UPDATE_NEXT_HOP] = new NextHopHandler();
        handlers[EventType.ADD_NEWCLIENT] = new AddClientHandler();
        handlers[EventType.MESSAGE] = new ClientMsgHandler();
        handlers[EventType.ROUND_END] = new RoundEndHandler();
    }
}
