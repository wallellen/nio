package com.wallellen.netty.nio2;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/1/16.
 */
public class TimeClient {
    public static void main(String[] args) {
        int port = 9090;

        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsynchTimeClientHandler-001").start();
    }
}
