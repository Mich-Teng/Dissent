package controller.handler;

import controller.Controller;
import proto.EventMsg;

import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-21 10:34.
 * Package: controller.handler
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public interface ControllerHandler {
    public void execute(EventMsg eventMsg, Controller controller, InetAddress srcAddr);
}
