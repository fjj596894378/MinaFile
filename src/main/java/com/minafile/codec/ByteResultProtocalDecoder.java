package com.minafile.codec;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.ByteReturnFileMessage;

/**
 * 这是客户端对服务器发来的消息进行解码 消息封装在ByteReturnFileMessage实体中。 在解析完之后，调用客户端定义的Handle
 * handle中的方法messageReceived 在方法中进行处理。
 * 
 * @author king_fu
 * 
 */
public class ByteResultProtocalDecoder extends CumulativeProtocolDecoder {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ByteResultProtocalDecoder.class);
	public static final int MAX_FILE_SIZE = 1024 * 1024 * 2; // 2M
	public ByteResultProtocalDecoder() {
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		LOGGER.info("客户端对服务器返回的消息进行解码。解码开始");
		LOGGER.info("当前pos:" + in.position());
		LOGGER.info("总共limit:" + in.limit());
		LOGGER.info("所有remaining:" + in.remaining());

		try {
			if (in.prefixedDataAvailable(4, MAX_FILE_SIZE)) {
				ByteReturnFileMessage brf =  this.readFile(in);
				out.write(brf);
			} else {
				LOGGER.info("【客户端解码】不符合读取条件");
				return false;
			}
		} catch (Exception e) {
			LOGGER.info("【客户端解码】解码过程中发生错误", e);
			return false;
		}
		return true;
	}
	
	private ByteReturnFileMessage readFile(IoBuffer in) throws CharacterCodingException  {
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		ByteReturnFileMessage brf = new ByteReturnFileMessage();
		in.position(0); // position归零
		brf.setSeq(in.getInt()); // 序号
		brf.setReturnMassageLength(in.getInt()); // 字节数
		brf.setReturnMassage(in.getString(brf.getReturnMassageLength(),
				decoder)); // 服务器返回的消息。字符串
		LOGGER.info("【客户端解码】服务器返回内容："  + brf.getReturnMassage());
		return brf;
    }

}
