package com.wallellen.netty.bio2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/28/16.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 9090;


        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
            }
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            System.err.println("The time server is start in port : " + port);
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 10000);

            while (true) {
                Socket socket = server.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } catch (Exception ex) {

        } finally {
            if (server != null) {
                System.err.println("The time server close");
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                server = null;
            }
        }
    }
}
