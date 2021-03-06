package com.wallellen.netty.nio2;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/30/16.
 */
public class TimeServer {
    public static void main(String[] args) {
        int port = 9090;

        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
        new Thread(timeServer, "AIO-timeserverhandler-001").start();
    }
}
