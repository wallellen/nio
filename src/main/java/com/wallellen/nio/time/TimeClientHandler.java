package com.wallellen.nio.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

import java.util.Date;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/24/16.
 */
public class TimeClientHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;

        try {
            long time = buf.readInt() * 1000;
            System.err.println(new Date(time));
            ctx.close();
        } finally {
            ReferenceCountUtil.release(buf);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
