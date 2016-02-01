package com.minafile.server;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.minafile.handle.server.FileUploadServerHandle;
import com.minafile.handle.server.UploadFileProtocolCodecFactory;
 
/**
 * 服务器类。开启服务。
 * @author king_fu
 *
 */
public class Server {
    private static final int SERVER_PORT = 8080;

    // 是否是自定义的消息。实现接口MessageDecoder。
    private static final boolean USE_CUSTOM_CODEC = true;

    public static void main(String[] args) throws Throwable {
        NioSocketAcceptor acceptor = new NioSocketAcceptor();

        if (USE_CUSTOM_CODEC) {
            acceptor.getFilterChain()
                    .addLast(
                            "codec",
                            new ProtocolCodecFilter(
                            		// 自定义解码工厂。
                                    new UploadFileProtocolCodecFactory(true)));
        } else {
            acceptor.getFilterChain().addLast(
                    "codec",
                    new ProtocolCodecFilter(
                    		// 使用核心自带的解码。
                            new ObjectSerializationCodecFactory()));
        }
        acceptor.getFilterChain().addLast("logger", new LoggingFilter()); //日志过滤链

        acceptor.setHandler(new FileUploadServerHandle()); // 客户端传过来的Message服务器处理。
        acceptor.bind(new InetSocketAddress(SERVER_PORT)); // 绑定监听的端口。

        System.out.println("监听端口 " + SERVER_PORT);
    }
}
