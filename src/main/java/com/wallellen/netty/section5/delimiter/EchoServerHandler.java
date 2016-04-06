package com.wallellen.netty.section5.delimiter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class EchoServerHandler extends ChannelHandlerAdapter {
    private int counter = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;

        System.err.println("This is " + ++counter + " times receive client: [" + body + "]");

        body += "$_";

        ctx.writeAndFlush(Unpooled.copiedBuffer(body.getBytes()));
    }
}
