package com.xxkun.relayserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HeartbeatSocketServer implements HeartbeatSocketThread.OnHeartbeatSocketResponse{

    private ServerSocket serverSocket;
    private int clientCount = 0;

    private Map<String, HeartbeatSocketThread> hsThreadMap = new HashMap<>();

    private boolean stop = false;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("SocketServer start : port -> " + port);
            while (!stop) {
                Socket socket = serverSocket.accept();
                HeartbeatSocketThread heartbeatSocketThread = new HeartbeatSocketThread(socket);
                heartbeatSocketThread.setOnHeartbeatSocketResponse(this);
                heartbeatSocketThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnect(HeartbeatSocketThread heartbeatSocketThread, String data) {
        hsThreadMap.put(data, heartbeatSocketThread);
        int udpPort = 8794;
        UDPReceiveThread udpReceiveThread = new UDPReceiveThread(udpPort);
        udpReceiveThread.setOnUDPResponse(new UDPReceiveThread.OnUDPResponse() {
            @Override
            public void onResponse(String hostAddress, int port, String data) {

            }
        });
        String token = "pass";
        udpReceiveThread.start();
        heartbeatSocketThread.send(udpPort + ":" + token);
    }

    @Override
    public void onResponse(HeartbeatSocketThread heartbeatSocketThread, String data) {

    }
}
