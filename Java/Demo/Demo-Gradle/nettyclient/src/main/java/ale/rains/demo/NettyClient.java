package ale.rains.demo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

public class NettyClient {
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 7001;
    private Logger logger = Logger.getLogger("netty client:");
    private String host;
    private int port;
    Bootstrap boot;

    public void connect(String host, int port) {
        this.host = host;
        this.port = port;
        EventLoopGroup group = new NioEventLoopGroup();
        boot = new Bootstrap();
        boot.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInit());
        ChannelFuture f = null;
        try {
            boot.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 0)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            f = boot.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void connect() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
        EventLoopGroup group = new NioEventLoopGroup();
        boot = new Bootstrap();
        boot.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInit());
        ChannelFuture f = null;
        try {
            boot.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 0)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            f = boot.connect(host, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private class ChannelInit extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ClientReadHandler());
        }

    }

    private class ClientReadHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf) msg;
            logger.info("client recv info: " + in.toString(CharsetUtil.UTF_8));
            ctx.write(in);
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            logger.info("client exception: " + cause.toString());
            ctx.close();
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            logger.info("channelUnregistered");
            connect();
            super.channelUnregistered(ctx);
        }

    }

    public static void main(String[] args) {
        System.out.println("Netty Client start");
        new NettyClient().connect("127.0.0.1", 7001);
    }
}