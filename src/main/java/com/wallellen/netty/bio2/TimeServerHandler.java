package com.wallellen.netty.bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/28/16.
 */
public class TimeServerHandler implements Runnable {
    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }


    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            String line = reader.readLine();
            writer.println("response: " + line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
