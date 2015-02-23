package server;

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
    }
}
