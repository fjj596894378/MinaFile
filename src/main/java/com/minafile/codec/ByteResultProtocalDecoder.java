package com.minafile.codec;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.ByteFileMessage;
import com.minafile.model.ByteReturnFileMessage;

public class ByteResultProtocalDecoder extends CumulativeProtocolDecoder{
	private static final Logger LOGGER = LoggerFactory.getLogger(ByteResultProtocalDecoder.class);
	private String charSet = "UTF-8";
	public ByteResultProtocalDecoder() {
	}
	public ByteResultProtocalDecoder(String charSet) {
		this.charSet = charSet;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		LOGGER.info("解码开始");
		  
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		int pos = in.position();
		LOGGER.info("当前pos:"+pos);
		int smsLength = 0;
		LOGGER.info("limit:"+in.limit());
		int remaining = in.remaining();
		LOGGER.info("remaining:"+remaining);
		try{
			if(remaining < 4){
				in.position();
				LOGGER.info("未读完");
				return false;
			}
			
			// 判断是否够解析出的长度
			smsLength = in.getInt();
			LOGGER.info("smsLength:" + smsLength);
			if (remaining < smsLength || smsLength < 0) {
				in.position(pos);
				LOGGER.info("未足够长度");
				return false;
			}
			in.position(0);
			ByteReturnFileMessage bfm = new ByteReturnFileMessage();
			bfm.setSeq(in.getInt());
			bfm.setReturnMassageLength(in.getInt());
			bfm.setReturnMassage(in.getString(bfm.getReturnMassageLength(),decoder));
			
			out.write(bfm);
		}catch (Exception e) {
			in.position(pos);
			LOGGER.info("解码过程中发生错误",e);
			return false;
		}
		
		return true;
	}

}
