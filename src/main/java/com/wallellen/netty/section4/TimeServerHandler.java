package com.wallellen.netty.section4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/5/16.
 */
public class TimeServerHandler extends ChannelHandlerAdapter {
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        byte[] req = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(req);

        String body = new String(req, "UTF-8").substring(0, req.length - System.getProperty("line.separator").length());
        System.err.println("The time server receive order " + body + "; the counter is: " + ++counter);

        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? String.valueOf(new Date().getTime()) : "BAD ORDER";
        currentTime = currentTime + System.getProperty("line.separator");

        ByteBuf res = Unpooled.copiedBuffer(currentTime.getBytes());
        ctx.writeAndFlush(res);
    }
}
