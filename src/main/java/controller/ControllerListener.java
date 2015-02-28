package controller;


import controller.handler.*;
import proto.EventType;
import template.BaseServerListener;
import template.Handler;


/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 10:29.
 * Package: controller
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ControllerListener extends BaseServerListener {

    public ControllerListener(Controller controller) {
        super(controller);
    }

    @Override
    protected void assignHandlers(Handler[] handlers) {
        // assign the handlers for controller
        handlers[EventType.SERVER_REGISTER] = new ServerRegisterHandler();
        handlers[EventType.CLIENT_REGISTER_CONTROLLERSIDE] = new ClientRegisterHandler();
        handlers[EventType.CLIENT_REGISTER_SERVERSIDE] = new ClientRegisterPublisher();
        handlers[EventType.ANNOUNCEMENT] = new AnnouncementHandler();
        handlers[EventType.MESSAGE] = new ClientMsgHandler();
        handlers[EventType.VOTE] = new VoteHandler();
        handlers[EventType.ROUND_END] = new RoundEndHandler();
    }
}
