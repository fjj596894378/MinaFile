package com.minafile.codec;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.ByteReturnFileMessage;

public class ByteResultProtocalEncoder extends ProtocolEncoderAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(ByteResultProtocalEncoder.class);
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		LOGGER.info("虽然没有用，但是还是进来了。");
		CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
		ByteReturnFileMessage returnMassage = (ByteReturnFileMessage)message;
		IoBuffer ioBuffer = IoBuffer.allocate( 4 + 4 + returnMassage.getReturnMassage().length()).setAutoExpand(true);
		ioBuffer.putInt(returnMassage.getSeq());
		ioBuffer.putInt(returnMassage.getReturnMassageLength());
		ioBuffer.putString(returnMassage.getReturnMassage(), encoder);
		LOGGER.info("encoder.toString()" + encoder.toString());
		ioBuffer.flip();
		LOGGER.info("编码完成");
		out.write(ioBuffer);
		
	}

}
