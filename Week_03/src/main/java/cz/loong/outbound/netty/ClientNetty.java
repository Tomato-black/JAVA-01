package cz.loong.outbound.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @author : zhang.cao
 * @date : 2021/1/30 13:42
 */
public class ClientNetty {

    private HttpRequest request;
    private String result;

    public ClientNetty(HttpRequest request) {
        this.request = request;
    }

    public String action() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(bossGroup)
                    .remoteAddress("127.0.0.1", 80)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(channelInitializer());
            ChannelFuture sync = bootstrap.connect().sync();
            sync.channel().closeFuture().sync();
            return this.result;
        } finally {
            bossGroup.shutdownGracefully();
        }
    }


    private ChannelInitializer channelInitializer() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                //包含编码器和解码器
                pipeline.addLast(new HttpClientCodec())
                        //聚合
                        .addLast(new HttpObjectAggregator(1024 * 10 * 1024))
                        //解压
                        .addLast(new HttpContentDecompressor())
                        .addLast(channelInboundHandlerAdapter());
            }
        };
    }


    private ChannelInboundHandlerAdapter channelInboundHandlerAdapter() {
        return new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                // 发送
                ctx.channel().writeAndFlush(request);
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                if (msg instanceof HttpContent) {
                    HttpContent content = (HttpContent) msg;
                    ByteBuf buf = content.content();
                    result = buf.toString(CharsetUtil.UTF_8);
                    System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
                    buf.release();
                    ctx.close();
                }
            }
        };
    }

    public static void main(String[] args) throws Exception {
        HttpRequest reqGet = HttpCreateor.createReqGet("re.100.com", new URI("/api/coderTool/employee/oaProcedureTask"));
        ClientNetty clientNetty = new ClientNetty(reqGet);
        String action = clientNetty.action();
        System.out.println(action);
    }
}
