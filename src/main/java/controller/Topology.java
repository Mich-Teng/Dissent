package controller;

import javafx.util.Pair;

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
    List<Pair<InetAddress, Integer>> serverList = new ArrayList<Pair<InetAddress, Integer>>();

    public void add(InetAddress addr, int port) {
        serverList.add(new Pair<InetAddress, Integer>(addr, port));
    }

    public int size() {
        return serverList.size();
    }

    public List<Pair<InetAddress, Integer>> getServerList() {
        return serverList;
    }
}
