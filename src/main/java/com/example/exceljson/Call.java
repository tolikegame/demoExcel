package com.example.exceljson;

import lombok.SneakyThrows;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Call extends Thread {

    public static void main(String[] args) throws Exception {
        for(int i=0;i<30;i++ ){
            Thread thread = new Call();
            thread.start();
        }

    }

    public void call() throws IOException {
        URL url = new URL("https://test71.gb-site.info:8989/acenter-api/summary/getPlayGameOrder.html");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","application/json");
        String param = "{\"createStart\": \"2020-08-29\",\"createEnd\": \"2020-09-29" +
                "\",\"pageNumber\": 1,\"pageSize\": 20,\n" +
                "\"siteId\": 1362,\"agentid\": -2}";
        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
        out.write(param.getBytes());
        out.flush();
        out.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line=reader.readLine())!=null){
            sb.append(line);
        }
        System.out.println(sb.toString());
    }

    @SneakyThrows
    @Override
    public void run() {
        this.call();
    }
}
