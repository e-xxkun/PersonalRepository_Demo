package com.xxkun.personalrepository;

import java.io.*;
import java.net.Socket;

public class HeartbeatSocketThread extends Thread{

    private final Socket socket;
    private boolean stop = false;
    private boolean event = false;

    private final String KEEP_ALIVE = "ARE_YOU_OK_?";
    private final String KEEP_ALIVE_CHECK = "SURE";
    private final String CONNECT = "CONNECT";
    private final String EVENT_HEARD = "EVENT:";

    private OnHeartbeatSocketResponse heartbeatSocketResponse;

    public HeartbeatSocketThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(CONNECT);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Server Connecting");
            while (!stop) {
                String inData = bufferedReader.readLine();
                if (inData!=null){
                    if (KEEP_ALIVE.equals(inData)) {
                        System.out.println("SERVER_IP_" + socket.getRemoteSocketAddress() + "：" + inData);
                    } else if (inData.startsWith(EVENT_HEARD)) {
                        System.out.println("SERVER_IP_" + socket.getRemoteSocketAddress() + "：Request -> "+inData);
                        if (heartbeatSocketResponse != null) {
                            heartbeatSocketResponse.onResponse(socket.getInetAddress().getHostAddress(), socket.getPort(), inData);
                        }
                        event = true;
                    } else {
                        System.out.println("SERVER_IP_" + socket.getRemoteSocketAddress() + "：Command not recognized -> " + inData);
                    }
                } else {
                    System.out.println("SERVER_IP_" + socket.getRemoteSocketAddress() + "：CONNECT FAIL");
                    break;
                }
                if (event) {
                    String outData = handleResponse(inData);
                    bufferedWriter.write(outData);
                    event = false;
                } else {
                    bufferedWriter.write(KEEP_ALIVE_CHECK);
                }
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
            outputStream.close();
            inputStream.close();
            bufferedReader.close();
            bufferedWriter.close();
            socket.shutdownOutput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        } finally {
            System.out.println("END");
        }
    }

    private String handleResponse(String inData) {
        return "Hello My Leader";
    }

    public void setOnHeartbeatSocketResponse(OnHeartbeatSocketResponse heartbeatSocketResponse) {
        this.heartbeatSocketResponse = heartbeatSocketResponse;
    }

    public interface OnHeartbeatSocketResponse {
        public void onResponse(String hostAddress, int port, String data);
    }
}




