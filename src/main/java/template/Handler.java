package template;

import proto.EventMsg;

import java.net.InetAddress;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-22 20:56.
 * Package: template
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

/**
 * * interface for handler
 */
public interface Handler {
    public void execute(EventMsg eventMsg, BaseServer server, InetAddress srcAddr, int port);
}
