package cz.loong.outbound.netty;

import cn.hutool.core.util.CharsetUtil;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author : zhang.cao
 * @date : 2021/1/30 14:41
 */
public class HttpCreateor {
    public static HttpRequest createReqGet(String server, URI uri) throws Exception {
        String req = "";
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                uri.toASCIIString(), Unpooled.wrappedBuffer(req.getBytes(CharsetUtil.UTF_8)));
//        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString());
        // 构建HTTP请求
        request.headers()
                .set(HttpHeaderNames.HOST, server)
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                //开启长连接
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                //设置传递请求内容的长度
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        // 返回
        return request;
    }

    public static HttpRequest createReqPost(byte[] body, String server, URI uri) throws Exception {

        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
                uri.toASCIIString(), Unpooled.wrappedBuffer(body));
        // 构建HTTP请求
        request.headers()
                .set(HttpHeaderNames.HOST, server)
                .set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8")
                //开启长连接
                .set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
                //设置传递请求内容的长度
                .set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());
        // 返回
        return request;
    }


    public static HttpResponse createIllegalRequestHttpResponse() throws UnsupportedEncodingException {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer("您的请求不合法".getBytes(CharsetUtil.UTF_8)));
        response.headers().set("Content-Type", "application/json");
        return response;
    }

}
