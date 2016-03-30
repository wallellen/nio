package com.wallellen.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/29/16.
 */
public class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port), 1024);

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.err.println("The time server is start in port: " + port);

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(5000);
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();

                SelectionKey key = null;
System.err.println(it.hasNext());
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();

                    try {
                        System.err.println("handle key ===" + key);
                        handleKey(key);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleKey(SelectionKey key) throws IOException {
        if (!key.isValid()) {
            System.err.println("INVALID --- " + key);
            return;
        }

        if (key.isAcceptable()) {
            //Accept the connection
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);

            //Add the new connection to the selector
            sc.register(selector, SelectionKey.OP_READ);
        }
System.err.println(key.isAcceptable() + "---" + key.isReadable());
        if (key.isReadable()) {
            // read the data
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = sc.read(readBuffer);
System.err.println("read length === " + readBytes);
            if (readBytes > 0) {
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, "UTF-8");
                System.err.println("The time server receive order: " + body);

                String currentTime = "QUERY TIME ORDER".trim().equalsIgnoreCase(body) ? new Date().toString() : "Bad request";

                doWrite(sc, currentTime);
            }
            else if(readBytes <= 0) {
                key.cancel();
                sc.close();
                System.err.println("closed ...");
            }
        }
    }

    private void doWrite(SocketChannel sc, String currentTime) throws IOException {
        if (currentTime == null || currentTime.trim().length() <= 0) {
            return;
        }

        byte[] bytes = currentTime.getBytes();
        //ByteBuffer buffer = ByteBuffer.wrap(bytes);
        //buffer.flip();

        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        sc.write(byteBuffer);
        System.err.println("write message done: " + currentTime);
        sc.finishConnect();
    }

    public void stop() {
        stop = true;
    }
}
