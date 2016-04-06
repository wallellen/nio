package com.wallellen.netty.section4.linebaseframedecoder;

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
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.err.println("The time server receiver: " + body + ", and counter is :" + counter++);

        String currentTime = "TIME QUERY ORDER".equalsIgnoreCase(body) ?
                String.valueOf(new Date().getTime()) : "BAD ORDER";
        currentTime += System.getProperty("line.separator");

        ctx.writeAndFlush(Unpooled.copiedBuffer(currentTime.getBytes()));
    }
}
