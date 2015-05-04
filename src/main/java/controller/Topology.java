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

/**
 * * data structure for network topology 
 */
public class Topology {
    // whole server list in network
    List<Pair<InetAddress, Integer>> serverList = new ArrayList<Pair<InetAddress, Integer>>();
    // add a server into topology
    public void add(InetAddress addr, int port) {
        serverList.add(new Pair<InetAddress, Integer>(addr, port));
    }
    // get the size of topology
    public int size() {
        return serverList.size();
    }
    // get all server list in the topology
    public List<Pair<InetAddress, Integer>> getServerList() {
        return serverList;
    }
}
