package com.xxkun.personalrepository;

import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8888);
            HeartbeatSocketThread heartbeatSocketThread = new HeartbeatSocketThread(socket);
            heartbeatSocketThread.setOnHeartbeatSocketResponse(new HeartbeatSocketThread.OnHeartbeatSocketResponse() {
                @Override
                public void onResponse(String data) {
                    new UDPTransfer().sendAndReceive();
                }
            });
            heartbeatSocketThread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
