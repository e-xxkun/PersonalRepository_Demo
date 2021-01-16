package com.xxkun.relayserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Scanner;

public class UDPTransfer {

    private DatagramPacket outPacket;
    private DatagramPacket inPacket;

    private static final int DATA_LEN = 4096;

    private OnUDPResponse udpResponse;

    public String receive(int port) {
        String inData = null;
        try {
            DatagramSocket datagramSocket = new DatagramSocket(port);
            System.out.println("UDP SOCKET OPEN！");

            byte[] bytes = new byte[DATA_LEN];
            inPacket = new DatagramPacket(bytes, bytes.length);

            datagramSocket.receive(inPacket);

            byte[] data = inPacket.getData();
            inData = new String(data);
            if (inData != null) {
                System.out.println("CLIENT_" + inPacket.getAddress() + ":" + inPacket.getPort() + " ： Response -> " + inData);
            } else {
                System.out.println("CLIENT_" + inPacket.getAddress() + ":" + inPacket.getPort() + " ： CONNECT FAIL");
            }
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return inData;
    }

    public void setOnUDPResponse(OnUDPResponse udpResponse) {
        this.udpResponse = udpResponse;
    }

    public interface OnUDPResponse {
        public void onResponse(String data);
    }
}
