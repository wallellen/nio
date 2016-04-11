package com.wallellen.netty.section10.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpHeaderNames.*;

/**
 * Created by <a href="mailto:wallellen@hotmail.com">WALLE</a> on 4/6/16.
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private String url;

    public HttpFileServerHandler(String url) {
        this.url = url;
    }


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        if (!msg.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        if (!HttpMethod.GET.equals(msg.method())) {
            sendError(ctx, METHOD_NOT_ALLOWED);
        }

        final String uri = msg.uri();
        final String path = sanitizerUri(uri);

        if (path == null) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        File file = new File(path);

        if (file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                sendList(ctx, file);
            } else {
                sendRedirect(ctx, uri);
            }

            return;
        }

        RandomAccessFile randomAccessFile = null;

        try {
            randomAccessFile = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException e) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        long fileLength = randomAccessFile.length();
        HttpResponse resp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, OK);
        setContentLength(resp, fileLength);
        setContentTypeHeader(resp, file);

        if (isKeepAlive(msg)) {
            //  resp.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        ctx.write(resp);

        ChannelFuture sendFileFuture =
                ctx.write(new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) throws Exception {
                if (total < 0) {
                    System.err.println("File Trans: " + progress);
                } else {
                    System.err.println("File Trans: " + progress + "/" + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                System.err.println("Trans completed");
            }
        });

        ChannelFuture lastFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);

        if (!isKeepAlive(msg)) {
            lastFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void sendRedirect(ChannelHandlerContext ctx, String uri) {
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, FOUND);
        resp.headers().set(LOCATION, uri);
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    private void setContentTypeHeader(HttpResponse resp, File file) {
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        resp.headers().set(CONTENT_TYPE, mimetypesFileTypeMap.getContentType(file));
    }

    private void setContentLength(HttpResponse resp, long fileLength) {

    }

    private void sendList(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, OK);
        resp.headers().set(CONTENT_TYPE, "text/html;charset=utf-8");

        StringBuffer buffer = new StringBuffer();
        String path = dir.getPath();
        buffer.append("<html><head><title>");
        buffer.append(path);
        buffer.append("</title></head><body>\r\n");

        buffer.append("<h3>\r\n");
        buffer.append("<ul>");
        buffer.append("<li><a href=\"../\">..</a></li>\r\n");
        for (File f : dir.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
               continue;
            }

            String name = f.getName();

            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }

            buffer.append("<li><a href=\"");
            buffer.append(name);
            buffer.append("/\">");
            buffer.append(name);
            buffer.append("</a></li>\r\n");
        }

        buffer.append("</ul></body></html>\r\n");

        ByteBuf buf = Unpooled.copiedBuffer(buffer.toString().getBytes());
        resp.content().writeBytes(buf);
        buf.release();
        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    private boolean isKeepAlive(FullHttpRequest msg) {
        return false;
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus badRequest) {
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, badRequest);
        Unpooled.copiedBuffer("Failure: " + badRequest.toString() + "\r\n", Charset.defaultCharset());
        resp.headers().set(CONTENT_TYPE, "text/plain; charset=utf-8");

        ctx.writeAndFlush(resp).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    private static final Pattern INSECURE_URL = Pattern.compile(".*[<>&\"].*");
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    private String sanitizerUri(String uri) {
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            try {
                uri = URLDecoder.decode(uri, "ISO-8859-1");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }

        if (!uri.startsWith(url) || !uri.startsWith("/")) {
            return null;
        }

        uri = uri.replace("/", File.separator);

        if (uri.contains(File.separator + ".") ||
                uri.startsWith("." + File.separator) ||
                uri.startsWith(".") ||
                uri.endsWith(".") ||
                INSECURE_URL.matcher(uri).matches()
                ) {
            return null;
        }

        return System.getProperty("user.dir") + uri;
    }
}
