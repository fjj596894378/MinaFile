package com.minafile.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ByteProtocalCodecFactory  implements ProtocolCodecFactory {
	
	public ByteProtocalCodecFactory(boolean isServer){
		if(isServer){
			encoder = new ByteResultProtocalEncoder(); // 返回给客户端的消息封装
			
			decoder = new ByteProtocalDecoder(); // 解析客户端发来的消息
		}else{
			// 客户端
			encoder = new ByteProtocalEncoder();	// 发给服务器的消息封装
			decoder = new ByteResultProtocalDecoder();	// 对服务器发来的回应信息进行解析
		}
		
	}
	
	private ProtocolDecoder decoder = new ByteProtocalDecoder();

	private ProtocolEncoder encoder = new ByteProtocalEncoder();
	@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

}
