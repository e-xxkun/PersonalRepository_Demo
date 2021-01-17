package com.xxkun.relayserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

public class UDPReceiveThread extends Thread {

    private static final int DATA_LEN = 1024;

    private DatagramPacket outPacket;
    private DatagramPacket inPacket;

    private int port;

    private OnUDPResponse udpResponse;

    public UDPReceiveThread(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        String inData = null;
        try {
            DatagramSocket datagramSocket = new DatagramSocket(port);
            System.out.println("UDP SOCKET OPEN！");

            byte[] bytes = new byte[DATA_LEN];
            inPacket = new DatagramPacket(bytes, bytes.length);

            datagramSocket.receive(inPacket);
            System.out.println("UDP Check " + new Date().toString());

            byte[] data = inPacket.getData();
            inData = new String(data);
            if (inData != null) {
                System.out.println("UDP_CLIENT_" + inPacket.getAddress() + ":" + inPacket.getPort() + " ： Response -> " + inData);
                if (udpResponse != null) {
                    udpResponse.onResponse(inPacket.getAddress().getHostAddress(), inPacket.getPort(), inData);
                }
            } else {
                System.out.println("UDP_CLIENT_" + inPacket.getAddress() + ":" + inPacket.getPort() + " ： CONNECT FAIL");
                if (udpResponse != null) {
                    udpResponse.onResponse(inPacket.getAddress().getHostAddress(), inPacket.getPort(), "CONNECT FAIL");
                }
            }

            String UDP_CHECK = "UDP_ALIVE";
            byte[] outBuff = UDP_CHECK.getBytes();
            outPacket = new DatagramPacket(outBuff, outBuff.length, inPacket.getSocketAddress());
            for (int i = 0;i < 10000;i ++) {
                datagramSocket.send(outPacket);
                System.out.println("UDP_IP_" + outPacket.getAddress() + ": CHECK " + i);
                sleep(4000);
            }

            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void setOnUDPResponse(OnUDPResponse udpResponse) {
        this.udpResponse = udpResponse;
    }

    public interface OnUDPResponse {
        public void onResponse(String hostAddress, int port, String data);
    }
}
