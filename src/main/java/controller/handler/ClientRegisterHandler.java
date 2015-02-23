package controller.handler;

import controller.Controller;
import proto.EventMsg;
import template.BaseServer;
import template.Handler;

import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 12:21.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ClientRegisterHandler implements Handler {
    @Override
    public void execute(EventMsg eventMsg, BaseServer baseServer, InetAddress srcAddr, int srcPort) {
        Controller controller = (Controller) baseServer;
    }
}
