package com.wallellen.netty.nio2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/1/16.
 */
public class ReadCompletionHandler implements CompletionHandler<Integer, ByteBuffer> {
    private AsynchronousSocketChannel channel;

    public ReadCompletionHandler(AsynchronousSocketChannel channel) {
        if (this.channel == null) {
            this.channel = channel;
        }
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] body = new byte[attachment.remaining()];
        attachment.get(body);

        try {
            String req = new String(body, "UTF-8");
            System.err.println("The time server receive order: " + req);
            String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(req) ? String.valueOf(new Date().getTime()) : "BAD ORDER ";

            doWrite(currentTime);
        } catch (Exception ex) {

        }
    }

    private void doWrite(String currentTime) {
        if (currentTime == null || currentTime.length() == 0) {
            return;
        }

        byte[] bytes = currentTime.getBytes();
        final ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);

        buffer.flip();
        // channel.write(buffer);
        // buffer.clear();

        channel.write(buffer, buffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                if (attachment.remaining() > 0) {
                    channel.write(buffer, buffer, this);
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
