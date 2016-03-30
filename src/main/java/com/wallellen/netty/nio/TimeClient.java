package com.wallellen.netty.nio;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/29/16.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 9090;

        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }

        new Thread(new TimeClientHandler("127.0.0.1", port), "TimeClient-001").start();
    }
}
