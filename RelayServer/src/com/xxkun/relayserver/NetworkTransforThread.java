package com.xxkun.relayserver;

import java.io.*;
import java.net.Socket;

public class NetworkTransforThread extends Thread{

    private final Socket socket;
    private boolean stop = false;
    private boolean event = false;

    private String outData = "";

    private final String KEEP_ALIVE = "ARE_YOU_OK_?";
    private final String KEEP_ALIVE_CHECK = "SURE";
    private final String CONNECT = "CONNECT";

    private long checkTimeInterval = 2000;

    private OnHeartbeatSocketResponse heartbeatSocketResponse;

    public NetworkTransforThread(Socket socket) {
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
            String inData = bufferedReader.readLine();
            if (inData!=null && CONNECT.equals(inData)){
                System.out.println("CLIENT_IP_" + socket.getRemoteSocketAddress() + "：CONNECT");
                if (heartbeatSocketResponse != null)  {
                    heartbeatSocketResponse.onConnect(this, inData);
                }
            } else {
                System.out.println("CLIENT_IP_" + socket.getRemoteSocketAddress() + "：CONNECT FAIL");
                stop = true;
            }
            while (!stop) {
                sleep(checkTimeInterval);
                if (event) {
                    bufferedWriter.write(outData);
                } else {
                    bufferedWriter.write(KEEP_ALIVE);
                }
                bufferedWriter.newLine();
                bufferedWriter.flush();
                inData = bufferedReader.readLine();
                if (inData!=null){
                    if (event) {
                        System.out.println("CLIENT_IP_" + socket.getRemoteSocketAddress() + "：Response -> " + inData);
                        if (heartbeatSocketResponse != null)  {
                            heartbeatSocketResponse.onResponse(this, inData);
                        }
                        event = false;
                    } else if (KEEP_ALIVE_CHECK.equals(inData)) {
                        System.out.println("CLIENT_IP_" + socket.getRemoteSocketAddress() + "：ALIVE");
                    } else {
                        System.out.println("CLIENT_IP_" + socket.getRemoteSocketAddress() + "：Command not recognized -> " + inData);
                    }
                } else {
                    System.out.println("CLIENT_IP_" + socket.getRemoteSocketAddress() + "：CONNECT FAIL");
                    break;
                }
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

    public void setOnHeartbeatSocketResponse(OnHeartbeatSocketResponse heartbeatSocketResponse) {
        this.heartbeatSocketResponse = heartbeatSocketResponse;
    }

    public interface OnHeartbeatSocketResponse {

        public void onConnect(NetworkTransforThread heartbeatSocketThread, String data);

        public void onResponse(NetworkTransforThread heartbeatSocketThread, String data);
    }
}




