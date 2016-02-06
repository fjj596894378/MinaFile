package com.minafile.server;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.codec.ByteProtocalCodecFactory;
import com.minafile.handle.server.FileObjectServerHandler;
 
/**
 * 服务器类。开启服务。
 * @author king_fu
 *
 */
public class Server {
    private static final int SERVER_PORT = 8080;
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    // 是否是自定义的消息。实现接口MessageDecoder。

    public static void main(String[] args) throws Throwable {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();
       
        	/*acceptor.getFilterChain().addLast("logger", new LoggingFilter()); //日志过滤链
*/      
        LOGGER.info("当前服务器发送缓存大小：" + acceptor.getSessionConfig().getReadBufferSize());
        LOGGER.info("当前服务器空闲时间：" + acceptor.getSessionConfig().getBothIdleTime());
       // acceptor.getSessionConfig().setReadBufferSize(2048*5000);//发送缓冲区10M
		LOGGER.info("【设置缓存】当前服务器发送缓存大小：" + acceptor.getSessionConfig().getReadBufferSize());
        
    	acceptor.getFilterChain()
        .addLast(
                "codec",
                new ProtocolCodecFilter(new ByteProtocalCodecFactory(true)));
        acceptor.setHandler(new FileObjectServerHandler()); // 客户端传过来的Message服务器处理。
        
        acceptor.bind(new InetSocketAddress(SERVER_PORT)); // 绑定监听的端口。

        System.out.println("监听端口 " + SERVER_PORT);
    }
}
