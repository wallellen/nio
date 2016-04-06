package com.wallellen.netty.section5.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class EchoClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap client = new Bootstrap();

        try {
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            String delimiter = "$_";

                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(delimiter.getBytes())));
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture sync = client.connect("127.0.0.1", 8888).sync();
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
