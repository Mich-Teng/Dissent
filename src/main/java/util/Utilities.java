package util;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ***************************************************************
 * Author: Chao Teng
 * Date: 2015-02-20 22:54.
 * Package: util
 * Description:
 * Welcome to contact chao.teng@yale.edu if you have any questions.
 * ****************************************************************
 */

public class Utilities {
    /**
     * * get local ip of the server 
     * @return local ip address
     * @throws UnknownHostException unable to find info about localhost
     */
    public static String getLocalIPAddr() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        System.out.println(addr);
        return addr.getHostAddress();
    }

    /**
     * * get hostname of the server 
     * @return local hostname
     * @throws UnknownHostException unable to find info about localhost
     */
    public static String getLocalHostname() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostName();
    }

    /**
     * * generate a random number between (0,range) 
     * @param range range
     * @return random generated number
     */
    public static int getRandomNumber(int range) {
        return (int) (Math.random() * range);
    }

    /**
     * * serialize an object to a byte array 
     * @param object object
     * @return byte array
     */
    public static byte[] serialize(Object object) {
        if (object == null)
            return null;
        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * * deserialize byte array to a object 
     * @param bytes bytes array
     * @return object
     */
    public static Object deserialize(byte[] bytes) {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * * send data through the socket 
     * @param socket socket
     * @param content message content
     * @param ip target ip
     * @param port target port
     */
    public static void send(DatagramSocket socket, byte[] content, String ip, int port) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(content, content.length,
                    InetAddress.getByName(ip), port);
            socket.send(sendPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * * different input type version of sending message
     * @param socket socket
     * @param content message content
     * @param ip target ip, InetAddress
     * @param port target port
     */
    public static void send(DatagramSocket socket, byte[] content, InetAddress ip, int port) {
        try {
            DatagramPacket sendPacket = new DatagramPacket(content, content.length,
                    ip, port);
            socket.send(sendPacket);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
