package com.wallellen.netty.section5.fixedlength;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class EchoServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("Receive client: [" + msg + "]");
    }
}
