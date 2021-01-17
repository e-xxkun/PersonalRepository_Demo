package com.xxkun.personalrepository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class Main {

//    public static final String HOST = "106.14.249.225";
    public static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input port of Relay Server:");
        int port = scanner.nextInt();
        System.out.println("Server connect: " + HOST + ":" + port);
        try {
            Socket socket = new Socket(HOST, port);
            HeartbeatSocketThread heartbeatSocketThread = new HeartbeatSocketThread(socket);
            heartbeatSocketThread.setOnHeartbeatSocketResponse(new HeartbeatSocketThread.OnHeartbeatSocketResponse() {
                @Override
                public void onResponse(String hostAddress, int port, String data) {
                    String[] udpData = data.split(":");
                    int udpPort = Integer.valueOf(udpData[1]);
                    String token = udpData[2];
                    System.out.println("Response Token: " + token);
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
