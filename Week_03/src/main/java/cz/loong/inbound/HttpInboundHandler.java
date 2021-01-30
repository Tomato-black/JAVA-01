package cz.loong.inbound;

import cn.hutool.core.util.CharsetUtil;
import cz.loong.filter.FilterChain;
import cz.loong.filter.IllegalRequestFilter;
import cz.loong.outbound.netty.ClientNetty;
import cz.loong.outbound.netty.HttpCreateor;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Date;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @author : zhang.cao
 * @date : 2021/1/30 11:42
 */
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final String proxyServer;
    private FilterChain filterChain;

    public HttpInboundHandler(String proxyServer) {
        this.proxyServer = proxyServer;
        this.filterChain = new FilterChain();
        this.filterChain.addFilter(new IllegalRequestFilter());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            logger.info("channelRead流量接口请求开始，时间为{}", new Date());
            FullHttpRequest fullRequest = (FullHttpRequest) msg;
            filterChain.doFilter(ctx, fullRequest);
            HttpRequest httpRequest = getHttpRequest(fullRequest);
            if (null == httpRequest) {
                return;
            }
            // hutool http
//            String s = HttpUtil.get(this.proxyServer + uri);
            //netty http
            ClientNetty clientNetty = new ClientNetty(httpRequest);
            String action = clientNetty.action();
            handleResponse(fullRequest, ctx, action);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private HttpRequest getHttpRequest(FullHttpRequest fullRequest) throws Exception {
        HttpMethod method = fullRequest.method();
        String uri = fullRequest.uri();
        HttpRequest httpRequest = null;
        if (HttpMethod.GET.equals(method)) {
            httpRequest = HttpCreateor.createReqGet(proxyServer, new URI(uri));
        }
        return httpRequest;
    }


    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, String result) throws Exception {
        FullHttpResponse response = null;
        try {
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getBytes(CharsetUtil.UTF_8)));
            response.headers().set("Content-Type", "application/json");
//            response.headers().setInt("Content-Length", Integer.parseInt(endpointResponse.get("Content-Length").getValue()));

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if (fullRequest != null) {
                if (!io.netty.handler.codec.http.HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    //response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
            //ctx.close();
        }

    }
}
