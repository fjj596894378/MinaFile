package com.minafile.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class ByteProtocalCodecFactory  implements ProtocolCodecFactory {
	
	public ByteProtocalCodecFactory(boolean isServer){
		if(isServer){
			encoder = new ByteProtocalEncoder();
			decoder = new ByteProtocalDecoder();
		}else{
			// 客户端
		//	encoder = new ByteProtocalEncoder();
			decoder = new ByteResultProtocalDecoder();
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
