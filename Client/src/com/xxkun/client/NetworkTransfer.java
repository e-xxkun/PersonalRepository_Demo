package com.xxkun.client;

import java.io.*;
import java.net.Socket;

public class NetworkTransfer {
    private final String URL = "localhost";
    private OnTransferResponse transferResponse;

    public void send(String data) {
        try {
            Socket socket = new Socket(URL, 8888);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(data);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Server Connecting");
            String inData = bufferedReader.readLine();
            if (inData!=null){
                if (transferResponse != null) {
                    transferResponse.onResponse(inData);
                }
            } else {
                System.out.println("SERVER_IP_" + socket.getRemoteSocketAddress() + "：CONNECT FAIL");
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

    public void setOnTransferResponse(OnTransferResponse transferResponse) {
        this.transferResponse = transferResponse;
    }

    public interface OnTransferResponse {
        public void onResponse(String data);
    }
}
