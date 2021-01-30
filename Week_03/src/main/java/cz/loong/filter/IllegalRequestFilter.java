package cz.loong.filter;

import cz.loong.common.LoongException;
import cz.loong.outbound.netty.HttpCreateor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.io.UnsupportedEncodingException;

/**
 * @author : zhang.cao
 * @date : 2021/1/30 18:20
 */
public class IllegalRequestFilter implements CustomRequestFilter {
    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
        String uri = fullRequest.uri();
        if (uri.contains("api")) {
            try {
                HttpResponse illegalRequestHttpResponse = HttpCreateor.createIllegalRequestHttpResponse();
                ctx.write(illegalRequestHttpResponse);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ctx.flush();
        }
        throw new LoongException(500, "不合法的路径");
    }
}
