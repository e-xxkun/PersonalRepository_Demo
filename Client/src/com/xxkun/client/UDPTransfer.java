package com.xxkun.client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPTransfer {

    private DatagramPacket outPacket;
    private DatagramPacket inPacket;

    private static final int DATA_LEN = 4096;

    public void sendAndReceive(SocketAddress receiveAddress, String token) {
//        SocketAddress receiveAddress = new InetSocketAddress("127.0.0.1", 8888);
        try {
            DatagramSocket datagramSocket = new DatagramSocket();

            byte[] inBuff = new byte[DATA_LEN];
            byte[] outBuff = token.getBytes();
            outPacket = new DatagramPacket(outBuff, outBuff.length, receiveAddress);
            inPacket = new DatagramPacket(inBuff, inBuff.length);

//            datagramSocket.send(outPacket);
//            datagramSocket.receive(inPacket);
//            inBuff = inPacket.getData();
//            String inData = new String(inBuff);
//
//            if (inData)

            Scanner scanner = new Scanner(System.in);
            boolean stop = false;
            while (!stop) {
                System.out.println("Local：");
                String outData = scanner.next();

                outBuff = outData.getBytes();
                outPacket.setData(outBuff);
                datagramSocket.send(outPacket);

                if ("exit".equals(outData)) {
                    break;
                }

                datagramSocket.receive(inPacket);
                inBuff = inPacket.getData();
                String inData = new String(inBuff);

                System.out.println("Server_"+inPacket.getAddress()+":"+inPacket.getPort()+" ：" + inData);
            }

            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
