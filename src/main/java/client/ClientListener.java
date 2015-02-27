package client;

import client.handler.*;
import proto.EventType;
import template.BaseServerListener;
import template.Handler;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-24 20:07.
 * Package: client
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ClientListener extends BaseServerListener {
    public ClientListener(DissentClient client) {
        super(client);
    }

    @Override
    protected void assignHandlers(Handler[] handlers) {
        // assign the handlers for controller
        handlers[EventType.CLIENT_REGISTER_CONFIRMATION] = new RegisterConfirmationHandler();
        handlers[EventType.ANNOUNCEMENT] = new AnnouncementHandler();
        handlers[EventType.MESSAGE] = new MsgHandler();
        handlers[EventType.VOTE] = new VoteStartHandler();
        handlers[EventType.ROUND_END] = new RoundEndHandler();
    }
}
