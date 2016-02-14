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
	private boolean readHead = false; // 是否读取头部信息
	private int NEEDDATA = 8; // 一个文件中需要的字节数
	private boolean isFirst = true; // 是否是第一次进来
	private boolean isFinish = false; // 是否已经处理所有数据（当前）
	private static IoBuffer newIoBuffer = IoBuffer.allocate(0).setAutoExpand(
			true);
	private ByteReturnFileMessage brf = new ByteReturnFileMessage();
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
			/*
			 * if (in.prefixedDataAvailable(4, MAX_FILE_SIZE)) { } else {
			 * LOGGER.info("【客户端解码】不符合读取条件"); return false; }
			 */
			this.readFile(in);
			if (isFinish) {
				// 解析完成
				out.write(brf);
				dataInit();
			} else {
				return true;
			}
		} catch (Exception e) {
			LOGGER.info("【客户端解码】解码过程中发生错误", e);
			return false;
		}
		return true;
	}

	private void readFile(IoBuffer in)
			throws CharacterCodingException {
		/* CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder(); */
		byte[] byteValue = null;
		IoBuffer bufTmp = null;
		if (isFirst) {
			bufTmp = IoBuffer
					.allocate(newIoBuffer.remaining() + in.remaining())
					.setAutoExpand(true);
			bufTmp.order(newIoBuffer.order());
			if (!readHead && (newIoBuffer.remaining() + in.remaining()) >= 8) {
				// 未读取了头部信息
				brf.setSeq(in.getInt(in.position())); // 序号 （字节）

				brf.setReturnMassage(in.getInt(in.position() + 4)); // 返回消息长度（字节）
				 
				byteValue = new byte[NEEDDATA];

				readHead = true;
			}
			// 判断是否可以读取所有的数据
			if (NEEDDATA > newIoBuffer.remaining() + in.remaining()) {
				bufTmp.put(newIoBuffer);
				bufTmp.put(in);
			} else {
				bufTmp.put(newIoBuffer);
				in.get(byteValue);
				bufTmp.put(byteValue);
			}
			bufTmp.flip();
			newIoBuffer = bufTmp;
		}else {
			bufTmp = IoBuffer
					.allocate(newIoBuffer.remaining() + in.remaining())
					.setAutoExpand(true);
			bufTmp.order(newIoBuffer.order());

			if ( NEEDDATA > newIoBuffer.remaining()
					+ in.remaining()) {
				bufTmp.put(newIoBuffer);
				bufTmp.put(in);
			} else {

				byteValue = new byte[in.remaining()
						- (newIoBuffer.remaining() + in.remaining() - NEEDDATA)];
				bufTmp.put(newIoBuffer);
				in.get(byteValue);
				bufTmp.put(byteValue);
			}
			bufTmp.flip();
			newIoBuffer = bufTmp;
		}
		if (NEEDDATA > (newIoBuffer.remaining())) { // 2.14把limit改成remaining
			/*
			 * bfm.setFileStreamLength(bfm.getFileStreamLength() -
			 * (newIoBuffer.remaining())); // 2.14把limit改成remaining
			 */if (isFirst) { // 第一次
				isFirst = false;
				LOGGER.info("【客户端解码】未解析数据：" + newIoBuffer.remaining() + "字节");
			}
			isFinish = false; // 数据未读取完。
		} else {
			int remainingData = newIoBuffer.remaining(); // 总共多少
			LOGGER.info("【客户端解码】当前数据池中总共多少：" + remainingData + " 字节");
			if (remainingData >= NEEDDATA) {
				//byteValue = new byte[8];
			} else {
				// byteValue = new byte[remainingData];
				LOGGER.info("【客户端解码】数据长度未满足，【退出】");
				return  ; // 退出
			}
			brf.setSeq(newIoBuffer.getInt()); // 序号
			brf.setReturnMassage(newIoBuffer.getInt());  //字节数
			//newIoBuffer.get(byteValue);
			isFinish = true;
			LOGGER.info("【客户端解析】解析完成");
		}

		/*
		 * brf.setReturnMassage(in.getString(brf.getReturnMassageLength(),
		 * decoder)); // 服务器返回的消息。字符串
		 */LOGGER.info("【客户端解码】服务器返回内容："
				+ (brf.getReturnMassage() == 1 ? "成功"
						: brf.getReturnMassage() == 2 ? "失败" : "其它"));
	}
	
	/**
	 * 处理完成，将所有数据初始化
	 */
	private void dataInit() {
		isFinish = false; // 是否已经处理所有数据
		isFirst = true; // 是否是第一次进来
		brf = new ByteReturnFileMessage(); // 保存对象
		readHead = false;
	}

}
