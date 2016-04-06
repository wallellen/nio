package com.wallellen.netty.section4;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/5/16.
 */
public class TimeClient {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup worker = new NioEventLoopGroup();

        Bootstrap client = new Bootstrap();
        ChannelFuture future = client
                .group(worker)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new TimeClientHandler());
                    }
                })
                .connect("localhost", 9090).sync();

        future.channel().closeFuture().sync();
    }
}
