package com.minafile.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.minafile.codec.ByteProtocalCodecFactory;
import com.minafile.codec.UploadFileProtocolCodecFactory;
import com.minafile.handle.client.FileObjectClientHandler;
import com.minafile.handle.client.FileStreamClientHandler;
import com.minafile.handle.client.FileUploadClientHandler;

/**
 * 客户端
 * @author king_fu
 *
 */
public class Client {
    private static final String HOSTNAME = "localhost";

    private static final int PORT = 8080;

    private static final long CONNECT_TIMEOUT = 30*1000L; // 30秒

    private static final int USE_CUSTOM_CODEC = 2; // 是否是自定义的编码
    

    public static void main(String[] args) throws Throwable {
       
        NioSocketConnector connector = new NioSocketConnector();
        FileStreamClientHandler clientHandler = new FileStreamClientHandler();
        connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
        if (USE_CUSTOM_CODEC == 0) {
        	 int[] values = new int[50];
             for(int i = 0;i< 50;++i){
             	values[i] = i;
             }
             
            connector.getFilterChain().addLast(
                    "codec",
                    new ProtocolCodecFilter(
                            new UploadFileProtocolCodecFactory(false)));
            connector.getFilterChain().addLast("logger", new LoggingFilter());

            connector.setHandler(new FileUploadClientHandler(values));

        } else if (USE_CUSTOM_CODEC == 1) {
            connector.getFilterChain().addLast(
                    "codec",
                    new ProtocolCodecFilter(
                            new ObjectSerializationCodecFactory()));
            
            connector.getFilterChain().addLast("logger", new LoggingFilter());
            
            connector.setHandler(clientHandler);
        }else{
        	 /*connector.getFilterChain().addLast("logger", new LoggingFilter());
        	 connector.getFilterChain().addLast(
                     "codec",
                     new ProtocolCodecFilter(
                             new ObjectSerializationCodecFactory()));*/
        	connector.getFilterChain()
            .addLast(
                    "codec",
                    new ProtocolCodecFilter(new ByteProtocalCodecFactory(false)));
             connector.setHandler(new FileObjectClientHandler());
        }
        
       
        IoSession session;
        for (;;) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress(
                        HOSTNAME, PORT));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }
        
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }
}
