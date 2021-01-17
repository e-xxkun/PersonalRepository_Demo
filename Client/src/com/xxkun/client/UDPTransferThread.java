package com.xxkun.client;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPTransferThread extends Thread{

    private DatagramPacket outPacket;
    private DatagramPacket inPacket;

    private static final int DATA_LEN = 1024;

    private SocketAddress receiveAddress;
    private String token;

    public UDPTransferThread(SocketAddress receiveAddress, String token) {
        this.receiveAddress = receiveAddress;
        this.token = token;
    }

    @Override
    public void run() {
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

                for (int i = 0;i < 20;i ++) {
                    datagramSocket.send(outPacket);
                    System.out.println("SEND : " + i);
                    sleep(500);
                }

                if ("exit".equals(outData)) {
                    break;
                }

//                datagramSocket.receive(inPacket);
//                inBuff = inPacket.getData();
//                String inData = new String(inBuff);
//
//                System.out.println("Server_"+inPacket.getAddress()+":"+inPacket.getPort()+" ：" + inData);
            }
            scanner.close();
            datagramSocket.close();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
