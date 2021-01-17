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



        UDPTransfer udpTransfer = new UDPTransfer();

        System.out.println("Input port of Server:");
        Scanner scanner = new Scanner(System.in);
        String token = "pass";
        int port = scanner.nextInt();

        SocketAddress receiveAddress = new InetSocketAddress("127.0.0.1", port);
        udpTransfer.sendAndReceive(receiveAddress, token);
    }
}
