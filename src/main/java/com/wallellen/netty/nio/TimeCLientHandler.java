package com.wallellen.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/29/16.
 */
public class TimeClientHandler implements Runnable {
    private final String ip;
    private final int port;

    private Selector selector;
    private SocketChannel socketChannel;
    private volatile boolean stop;

    public TimeClientHandler(String ip, int port) {
        this.ip = ip;
        this.port = port;

        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        while (!stop) {
            try {
                selector.select(3000);
                Set<SelectionKey> keySet = selector.selectedKeys();
                Iterator<SelectionKey> set = keySet.iterator();
                SelectionKey key = null;

                System.err.println(set.hasNext() + "=====");
                while (set.hasNext()) {
                    key = set.next();
                    set.remove();

                    try {
                        handleInput(key);
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

            } catch (Exception e) {
                e.printStackTrace();
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

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel sc = (SocketChannel) key.channel();

            if (key.isConnectable()) {
                if (sc.finishConnect()) {
                    sc.register(selector, SelectionKey.OP_READ);

                    doWrite(sc);
                }
            } else {
                System.exit(-1);
            }

            if (key.isReadable()) {
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int readByte = sc.read(readBuffer);

                if (readByte > 0) {
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");

                    System.err.println("Now is " + body);

                    stop = true;
                } else {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] req = "QUERY TIME ORDER".getBytes();

        //ByteBuffer byteBuffer = ByteBuffer.wrap(req);
        //byteBuffer.flip();
        //sc.write(byteBuffer);

        ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
        byteBuffer.put(req);
        byteBuffer.flip();
        sc.write(byteBuffer);

        if (byteBuffer.hasRemaining()) {
            System.err.println("sending ...");
        } else {
            System.err.println("sending order 2 server succeed");
        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(ip, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            System.err.println("On read ...");
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            System.err.println("On connect ...");
        }
    }
}
