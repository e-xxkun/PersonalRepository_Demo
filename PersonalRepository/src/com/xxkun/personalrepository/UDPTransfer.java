package com.xxkun.personalrepository;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
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
            inPacket = new DatagramPacket(inBuff, inBuff.length);
            Scanner scanner = new Scanner(System.in);

            byte[] outBuff = token.getBytes();
            outPacket = new DatagramPacket(outBuff, outBuff.length, receiveAddress);
            datagramSocket.send(outPacket);
            boolean stop = false;
            while (!stop) {
                datagramSocket.receive(inPacket);
                inBuff = inPacket.getData();
                String inData = new String(inBuff);
                if (inData != null) {
                    System.out.println("Client_" + inPacket.getAddress() + ":" + inPacket.getPort() + " ：" + inData);
                } else {
                    System.out.println("Client_" + inPacket.getAddress() + ":" + inPacket.getPort() + " ：CONNECT FAIL");
                    break;
                }

                System.out.println("Local：");
                String outData = scanner.next();

                outBuff = outData.getBytes();
                outPacket.setSocketAddress(inPacket.getSocketAddress());
                outPacket.setData(outBuff);
                datagramSocket.send(outPacket);
                if ("exit".equals(outData)) {
                    break;
                }
            }
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
