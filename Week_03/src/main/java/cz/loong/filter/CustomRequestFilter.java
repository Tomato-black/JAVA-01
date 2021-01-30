package cz.loong.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface CustomRequestFilter {
    
    void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
    
}
