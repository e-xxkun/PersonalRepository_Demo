package com.xxkun.relayserver;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Input port of Local:");
        Scanner scanner = new Scanner(System.in);
        int port = scanner.nextInt();
        new HeartbeatSocketServer().start(port);
    }
}
