package ale.rains.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.logging.Logger;

public class NettyServer {
    private Logger logger = Logger.getLogger("netty server:");
    private int port = 7001;
    public void bind() {
        // 侦听
        EventLoopGroup acceptor = new NioEventLoopGroup();
        // 读写
        EventLoopGroup worker = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(acceptor, worker)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ClientChannelHandler());
                    }
                });

        // 绑定端口，开始接收进来的连接
        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            // 等待服务器socket关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.info(e.toString());
        }
    }

    private class ClientChannelHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelActive();
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            ctx.fireChannelInactive();
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf in = (ByteBuf)msg;
            logger.info("Server received: " + in.toString(CharsetUtil.UTF_8));
            ctx.write(in);
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
                throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

    public static void main(String[] args) {
        System.out.println("Netty Server start");
        new NettyServer().bind();
    }
}