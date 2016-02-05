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

public class ByteProtocalDecoder extends CumulativeProtocolDecoder{
	private static final Logger LOGGER = LoggerFactory.getLogger(ByteProtocalDecoder.class);
	private String charSet = "UTF-8";
	public ByteProtocalDecoder() {
	}
	public ByteProtocalDecoder(String charSet) {
		this.charSet = charSet;
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		LOGGER.info("解码开始");
		  
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		int pos = in.position();
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
			ByteFileMessage bfm = new ByteFileMessage();
			bfm.setSeq(in.getInt());
			bfm.setFileNameLength(in.getInt());
			bfm.setFileName(in.getString(bfm.getFileNameLength(),decoder));
			bfm.setFileStreamLength(in.getInt());
			//byte[] byteValue = new byte[new Long(bfm.getFileStreamLength()).intValue()];
			byte[] byteValue = new byte[bfm.getFileStreamLength()];
			LOGGER.info("" +  bfm.getFileStreamLength()  );
			in.get(byteValue);
			bfm.setFileStream(byteValue);
			
			out.write(bfm);
		}catch (Exception e) {
			in.position(pos);
			LOGGER.info("解码过程中发生错误",e);
			return false;
		}
		
		return true;
	}

}
