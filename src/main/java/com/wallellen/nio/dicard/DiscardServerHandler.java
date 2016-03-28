package com.wallellen.nio.dicard;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 3/24/16.
 */
public class DiscardServerHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //super.channelRead(ctx, msg);
        try {
            //System.err.println(((ByteBuf) msg).toString());
            //((ByteBuf) msg).release();
            //ByteBuf byteBuf = (ByteBuf) msg;
            //System.err.println(byteBuf.toString(CharsetUtil.US_ASCII));


            ctx.write(msg);
            ctx.flush();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.close();
    }
}
