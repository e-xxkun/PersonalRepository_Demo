package com.xxkun.client;

import jdk.nashorn.internal.parser.JSONParser;

public class Main {

    public static void main(String[] args) {
        NetworkTransfer networkTransfer = new NetworkTransfer();
        networkTransfer.setOnTransferResponse(new NetworkTransfer.OnTransferResponse() {
            @Override
            public void onResponse(String data) {
                System.out.println("Server Response: " + data);
                String[] UDPData = data.split(":");
            }
        });
        networkTransfer.send("test:test");
    }
}
