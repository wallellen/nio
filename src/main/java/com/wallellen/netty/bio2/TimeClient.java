package com.wallellen.netty.bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/28/16.
 */
public class TimeClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9090);

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        writer.println("abc");
System.err.println("write done");

       System.err.println(reader.readLine() + "----------");
    }
}
