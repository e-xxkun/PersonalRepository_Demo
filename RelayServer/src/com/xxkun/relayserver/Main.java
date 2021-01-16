package com.xxkun.relayserver;

public class Main {

    public static void main(String[] args) {
        new HeartbeatSocketServer().start(8888);
    }
}
