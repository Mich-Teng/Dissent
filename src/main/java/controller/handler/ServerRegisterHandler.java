package controller.handler;

import controller.Controller;
import proto.EventMsg;

import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 11:42.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class ServerRegisterHandler implements ControllerHandler {
    @Override
    public void execute(EventMsg eventMsg, Controller controller, InetAddress srcAddr) {
        controller.addServer(srcAddr);
        // if we want to support dynamically adding server, we should check
        // the controller's status and return data to the new coming server
        // todo
    }
}
