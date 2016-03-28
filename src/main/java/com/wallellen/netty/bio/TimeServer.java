package com.wallellen.netty.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/28/16.
 */
public class TimeServer {
    public static void main(String[] args) throws IOException {
        int port = 9090;

        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            System.err.println("The time server is start in port: " + port);

            Socket socket = null;

            while (true) {
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (server != null) {
                System.err.println("The time server close");
                server.close();
                server = null;
            }
        }
    }
}
