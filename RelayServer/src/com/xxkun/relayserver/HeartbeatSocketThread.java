package com.xxkun.relayserver;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HeartbeatSocketThread extends Thread{

    private final Socket socket;
    private boolean stop = false;
    private boolean event = false;

    private String outData = "";

    private final String KEEP_ALIVE = "ARE_YOU_OK_?";
    private final String KEEP_ALIVE_CHECK = "SURE";

    private long checkTimeInterval = 2000;

    public HeartbeatSocketThread(Socket socket) {
        this.socket = socket;
    }

    public void send(String data) {
        outData = data;
        event = true;
        interrupt();
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            while (!stop) {
                if (event) {
                    bufferedWriter.write(outData);
                } else {
                    bufferedWriter.write(KEEP_ALIVE);
                }
                bufferedWriter.newLine();
                bufferedWriter.flush();
                String inData = bufferedReader.readLine();
                if (inData!=null){
                    if (event) {
                        // TODO
                        System.out.println("客户端_IP_" + socket.getRemoteSocketAddress() + "：response -> " + inData);
                        event = false;
                    } else if (KEEP_ALIVE_CHECK.equals(inData)) {
                        System.out.println("客户端_IP_" + socket.getRemoteSocketAddress() + "：ALIVE");
                    } else {
                        System.out.println("客户端_IP_" + socket.getRemoteSocketAddress() + "：命令未识别 -> " + inData);
                    }
                } else {
                    System.out.println("客户端_IP_" + socket.getRemoteSocketAddress() + "：连接出错 ");
                    break;
                }
                sleep(checkTimeInterval);
            }
            outputStream.close();
            inputStream.close();
            bufferedReader.close();
            bufferedWriter.close();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        } finally {
            System.out.println("END");
        }
    }
}




