package controller;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-20 23:14.
 * Package: server
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Topology {
    List<InetAddress> serverList = new ArrayList<InetAddress>();

    public void add(InetAddress addr) {
        serverList.add(addr);
    }
}
