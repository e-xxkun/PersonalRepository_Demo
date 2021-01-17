package com.xxkun.personalrepository;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class UDPTransferThread extends Thread{

    private static final int DATA_LEN = 1024;

    private DatagramPacket outPacket;
    private DatagramPacket inPacket;

    private SocketAddress receiveAddress;
    private String token;

    public UDPTransferThread(SocketAddress receiveAddress, String token) {
        this.receiveAddress = receiveAddress;
        this.token = token;
    }

    @Override
    public void run() {
        try {
            DatagramSocket datagramSocket = new DatagramSocket();

            byte[] inBuff = new byte[DATA_LEN];
            inPacket = new DatagramPacket(inBuff, inBuff.length);
            Scanner scanner = new Scanner(System.in);

            String checkData = token;
            byte[] outBuff = checkData.getBytes();
            outPacket = new DatagramPacket(outBuff, outBuff.length, receiveAddress);
            datagramSocket.send(outPacket);

            System.out.println("UDP Server START");

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

//                System.out.println("Local：");
//                String outData = scanner.next();
//
//                outBuff = outData.getBytes();
//                outPacket.setSocketAddress(inPacket.getSocketAddress());
//                outPacket.setData(outBuff);
//                datagramSocket.send(outPacket);
//                if ("exit".equals(outData)) {
//                    break;
//                }
            }
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
