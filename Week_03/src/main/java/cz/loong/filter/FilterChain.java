package cz.loong.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : zhang.cao
 * @date : 2021/1/30 18:14
 */
public class FilterChain {
    public static List<CustomRequestFilter> filterList = new ArrayList<CustomRequestFilter>();

    public void addFilter(CustomRequestFilter filter) {
        filterList.add(filter);
    }

    public void doFilter(ChannelHandlerContext ctx, FullHttpRequest fullRequest) {
        for (int i = 0; i < filterList.size(); i++) {
            filterList.get(i).filter(fullRequest, ctx);
        }
    }
}
