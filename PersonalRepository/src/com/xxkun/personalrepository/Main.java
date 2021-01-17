package com.xxkun.personalrepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Main {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);
            HeartbeatSocketThread heartbeatSocketThread = new HeartbeatSocketThread(socket);
            heartbeatSocketThread.setOnHeartbeatSocketResponse(new HeartbeatSocketThread.OnHeartbeatSocketResponse() {
                @Override
                public void onResponse(String hostAddress, int port, String data) {
                    String[] udpData = data.split(":");
                    int udpPort = Integer.valueOf(udpData[1]);
                    String token = udpData[2];
                    SocketAddress receiveAddress = new InetSocketAddress(hostAddress, udpPort);
                    new UDPTransferThread(receiveAddress, token).start();
                }
            });
            heartbeatSocketThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
