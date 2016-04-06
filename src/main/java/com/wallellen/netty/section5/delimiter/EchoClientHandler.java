package com.wallellen.netty.section5.delimiter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class EchoClientHandler extends ChannelHandlerAdapter {
    private byte[] req;
    private int counter;

    public EchoClientHandler() {
        req = ("Hi, my name is wallellen. $_").getBytes();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(Unpooled.copiedBuffer(req));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String res = (String) msg;

        System.err.println("This is " + counter++ + " times receive server [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
