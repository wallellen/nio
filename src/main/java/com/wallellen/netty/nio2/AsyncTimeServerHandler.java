package com.wallellen.netty.nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/30/16.
 */
public class AsyncTimeServerHandler implements Runnable {
    CountDownLatch latch;
    AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AsyncTimeServerHandler(int port) {
        try {
            asynchronousServerSocketChannel = AsynchronousServerSocketChannel.open();
            asynchronousServerSocketChannel.bind(new InetSocketAddress(port));
            System.err.println("The time server is start in port : " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);
        doAccept();

        try {
            latch.await();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void doAccept() {
        asynchronousServerSocketChannel.accept(this, new AcceptCompletionHandler());
    }
}
