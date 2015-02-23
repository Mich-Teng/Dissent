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
    public static String getLocalIPAddr() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        System.out.println(addr);
        return addr.getHostAddress();
    }

    public static String getLocalHostname() throws UnknownHostException {
        InetAddress addr = InetAddress.getLocalHost();
        return addr.getHostName();
    }

    public static int getRandomNumber(int range) {
        return (int) (Math.random() * range);
    }

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
