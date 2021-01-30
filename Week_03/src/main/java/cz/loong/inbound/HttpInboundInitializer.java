package cz.loong.inbound;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author : zhang.cao
 * @date : 2021/1/30 11:35
 */
public class HttpInboundInitializer extends ChannelInitializer<SocketChannel> {
    private String proxyServer;

    public HttpInboundInitializer(String proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec())
                .addLast(new HttpObjectAggregator(1024 * 1024))
                .addLast(new HttpInboundHandler(this.proxyServer));
    }
}
