package com.xxkun.relayserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    private ServerSocket serverSocket;
    private int clientCount = 0;

    private boolean stop = false;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);

            while (!stop) {
                Socket socket = serverSocket.accept();

                HeartbeatSocketThread heartbeatSocketThread = new HeartbeatSocketThread(socket);
                heartbeatSocketThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
