package com.minafile.codec;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.ByteReturnFileMessage;

/**
 * 这个是服务器对返回客户端的消息进行编码。
 * @author king_fu
 *
 */
public class ByteResultProtocalEncoder extends ProtocolEncoderAdapter{
	private static final Logger LOGGER = LoggerFactory.getLogger(ByteResultProtocalEncoder.class);
	@Override
	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception {
		LOGGER.info("服务器对返回客户端的消息进行编码");
		CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();// 以UTF-8的方式进行解析字符串
		// 对message进行强转
		ByteReturnFileMessage returnMassage = (ByteReturnFileMessage)message;
		IoBuffer ioBuffer = IoBuffer.allocate( 4 + 4 /*+ returnMassage.getReturnMassage().length()*/).setAutoExpand(true);
		ioBuffer.putInt(returnMassage.getSeq()); // 序号
		// 传给客户端消息的字节数。
		// 这里要用到字节数。不过只是获取字符串的长度，那么不对的。
		// 因为这是根据字节数获取数据的。所以如果字符串中有中文，那么就会获取出错。
		 ioBuffer.putInt(returnMassage.getReturnMassage()); 
		
		/*LOGGER.info("服务器传给客户端字符长度(字节)：" + returnMassage.getReturnMassageLength() );*/
		/*ioBuffer.putString(returnMassage.getReturnMassage(), encoder);*/
		LOGGER.info("服务器准备发送的信息【" + (returnMassage.getReturnMassage() == 1?"成功":returnMassage.getReturnMassage() == 2?"失败":"其它")+ "】；字符串编码类型：" + encoder.toString());
		ioBuffer.flip();
		LOGGER.info("服务器编码完成");
		out.write(ioBuffer);
		
	}

}
