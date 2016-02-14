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
 * 这个是服务器对客户端发来的消息进行解码 继承CumulativeProtocolDecoder 实现doDecode
 * 父类会将数据读取完之后，再调用实现的方法doDecode。 如果成功读取完之后，服务器会去Handle中进行业务处理。
 * 对发来的文件进行业务处理，比如说保存之类的 动作。
 * 
 * @author king_fu
 * 
 */
public class ByteProtocalDecoder extends CumulativeProtocolDecoder {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ByteProtocalDecoder.class);
	public static final int MAX_FILE_SIZE = 1024 * 1024 * 1024; // 1G
	private boolean isFinish = false; // 是否已经处理所有数据
	private static boolean isFirst = true; // 是否是第一次进来
	private ByteFileMessage bfm = new ByteFileMessage(); // 保存对象
	private static int dataPool = 0;
	private static IoBuffer newIoBuffer = IoBuffer.allocate(0).setAutoExpand(
			true);

	public ByteProtocalDecoder() {
	}

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		LOGGER.info("服务器对客户端发来的消息进行解码。解码开始");
		LOGGER.info("limit:" + in.limit());
		LOGGER.info("remaining:" + in.remaining());
		try {
			// 这个方法的调用是判断IoBuffer里的数据是否满足一条消息了
			// dataLength = getInt(position()); 用绝对值的方式读取，position不会移动。

			/* if ((!isFirst) || in.prefixedDataAvailable(4, MAX_FILE_SIZE)) { */
			this.readFile(in);
			if (isFinish) {
				// 解析完成
				out.write(bfm);
				dataInit();
			} else {
				return true;
			}
		} catch (Exception e) {
			LOGGER.info("服务器解码过程中发生错误", e);
			return false;
		}
		return true;
	}

	private void readFile(IoBuffer in) throws CharacterCodingException {
		CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
		in.position(0);
		
		LOGGER.info("【服务器解析】继续接受in：" + in.remaining());
		IoBuffer bufTmp = null;
		bufTmp = IoBuffer.allocate(
				newIoBuffer.remaining() + in.remaining())
				.setAutoExpand(true);
		bufTmp.order(newIoBuffer.order());
		bufTmp.put(newIoBuffer);
		bufTmp.put(in);
		bufTmp.flip();
		newIoBuffer = bufTmp;
		
		
		// ByteFileMessage bfm = new ByteFileMessage();
		if (isFirst) {
			bfm.setSeq(newIoBuffer.getInt()); // 序号
			bfm.setFileNameLength(newIoBuffer.getInt()); // 文件名长度（字节）
			bfm.setFileName(newIoBuffer.getString(bfm.getFileNameLength(), decoder)); // 文件名。UTF-8格式
			bfm.setFileStreamLength(newIoBuffer.getInt()); // 文件长度（字节）
			dataPool = bfm.getFileStreamLength();
		}
		// 如果读的文件的长度(字节：比如4096个字节)，远远大于这个缓冲区的容量（最大容量：2048字节），那么需要进行把一个个包黏起来
		byte[] byteValue = null;
		if (bfm.getFileStreamLength() > (newIoBuffer.remaining())) { // 2.14把limit改成remaining
			/*
			 * int dealDataLen = in.limit(); // 本次要处理的数据长度
			 * 
			 * byteValue = new byte[dealDataLen];
			 */
			bfm.setFileStreamLength(bfm.getFileStreamLength()
					- (newIoBuffer.remaining())); // 2.14把limit改成remaining
			
			if (isFirst) { // 第一次
				
				isFirst = false;
			/*} else {
				LOGGER.info("【服务器解析】继续接受in：" + in.remaining());
				bufTmp = IoBuffer.allocate(
						newIoBuffer.remaining() + in.remaining())
						.setAutoExpand(true);
			}*/
			
			isFinish = false; // 数据未读取完。
			LOGGER.info("【服务器解析】未解析数据：" + newIoBuffer.remaining() + "字节");
			}
		} else {
			/*IoBuffer bufTmp = IoBuffer.allocate(
					newIoBuffer.remaining() + in.remaining()).setAutoExpand(
					true);
			bufTmp.order(newIoBuffer.order());
			bufTmp.put(newIoBuffer);*/
			/*
			 * byteValue = new byte[bfm.getFileStreamLength()];
			 * bufTmp.put(in.get(byteValue,0,bfm.getFileStreamLength()));
			 */
			/*
			 * byteValue = new byte[in.remaining()];
			 * bufTmp.put(in.get(byteValue,0,byteValue.length));
			 */
			/*bufTmp.put(in);
			bufTmp.flip();
			newIoBuffer = bufTmp;*/
			int remainingData = newIoBuffer.remaining(); // 总共多少
			LOGGER.info("【服务器解析】当前数据池中总共多少：" + remainingData + " 字节");
			if (remainingData > dataPool) {
				byteValue = new byte[dataPool];
			} else {
				byteValue = new byte[remainingData];
			}
			// byteValue = new byte[remainingData];
			newIoBuffer.get(byteValue);
			LOGGER.info("当前读取的文件大小：" + remainingData / 1024 + "KB");
			bfm.setFileStream(byteValue);
			isFinish = true;
			LOGGER.info("【服务器解析】解析完成");
		}
		// LOGGER.info(new String(). bfm.getFileStream());
		// return isFinish;
	}

	/**
	 * 处理完成，将所有数据初始化
	 */
	private void dataInit() {
		isFinish = false; // 是否已经处理所有数据
		isFirst = true; // 是否是第一次进来
		bfm = new ByteFileMessage(); // 保存对象
		// newIoBuffer = IoBuffer.allocate(0).setAutoExpand(true) ;
	}
}
