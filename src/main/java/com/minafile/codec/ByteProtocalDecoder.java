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

import com.minafile.model.ByteFileMessage;

/**
 * 这个是服务器对客户端发来的消息进行解码
 * 继承CumulativeProtocolDecoder
 * 实现doDecode
 * 父类会将数据读取完之后，再调用实现的方法doDecode。
 * 如果成功读取完之后，服务器会去Handle中进行业务处理。
 * 对发来的文件进行业务处理，比如说保存之类的 动作。
 * @author king_fu
 *
 */
public class ByteProtocalDecoder extends CumulativeProtocolDecoder{
	private static final Logger LOGGER = LoggerFactory.getLogger(ByteProtocalDecoder.class);
	public static final int MAX_FILE_SIZE = 1024 * 1024 * 1024; // 1G
	public ByteProtocalDecoder() {
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		LOGGER.info("服务器对客户端发来的消息进行解码。解码开始");
		LOGGER.info("limit:"+in.limit());
		LOGGER.info("remaining:"+in.remaining());
		try{
		 // 这个方法的调用是判断IoBuffer里的数据是否满足一条消息了
		//	 dataLength = getInt(position());	用绝对值的方式读取，position不会移动。
		  if (in.prefixedDataAvailable(4, MAX_FILE_SIZE)) {
			  ByteFileMessage bfm =  this.readFile(in);
			  out.write(bfm);
		  }else{
			  LOGGER.info("不符合读取条件");
			  return false;
		  }
		}catch (Exception e) {
			LOGGER.info("服务器解码过程中发生错误",e);
			return false;
		}
		return true;
	}
	
	private ByteFileMessage readFile(IoBuffer in) throws CharacterCodingException  {
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		in.position(0);
		ByteFileMessage bfm = new ByteFileMessage();
		bfm.setSeq(in.getInt()); //序号
		bfm.setFileNameLength(in.getInt()); // 文件名长度（字节）
		bfm.setFileName(in.getString(bfm.getFileNameLength(),decoder)); // 文件名。UTF-8格式
		bfm.setFileStreamLength(in.getInt()); // 文件长度（字节）
		byte[] byteValue = new byte[bfm.getFileStreamLength()];
		LOGGER.info("读取的文件大小：" +  bfm.getFileStreamLength()/1024/1024 + "M"  );
		in.get(byteValue);
		bfm.setFileStream(byteValue);
		LOGGER.info("解析完成"  );
		return bfm;
    }
    

}
