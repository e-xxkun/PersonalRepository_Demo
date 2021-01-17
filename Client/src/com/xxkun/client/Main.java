package com.xxkun.client;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        NetworkTransfer networkTransfer = new NetworkTransfer();
//        networkTransfer.setOnTransferResponse(new NetworkTransfer.OnTransferResponse() {
//            @Override
//            public void onResponse(String data) {
//                System.out.println("Server Response: " + data);
//                String[] UDPData = data.split(":");
//            }
//        });
//        networkTransfer.send("test:test");




        Scanner scanner = new Scanner(System.in);
        System.out.println("Input Host Name of Server:");
        String hostName = scanner.next();
        System.out.println("Input port of Server:");
        String token = "pass";
        int port = scanner.nextInt();

        SocketAddress receiveAddress = new InetSocketAddress(hostName, port);
        UDPTransferThread udpTransfer = new UDPTransferThread(receiveAddress, token);
        udpTransfer.start();
    }
}
