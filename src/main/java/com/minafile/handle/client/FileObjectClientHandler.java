package com.minafile.handle.client;

import java.io.File;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.exception.MyRuntimeException;
import com.minafile.model.ByteFileMessage;
import com.minafile.model.ByteReturnFileMessage;
import com.minafile.model.PropertiesModel;
import com.minafile.util.ReadProperties;

/**
 * 客户端如果添加了自定义编码解码类 那么，在发送给服务器的时候，信息会先经过编码器(类) 然后再发送给服务器；
 * 
 * 如果实现了解码器，那么在服务器发送信息过来之后， 就会先调用解码器。然后再进行业务处理
 * 
 * @author king_fu
 * 
 */
public class FileObjectClientHandler extends IoHandlerAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileObjectClientHandler.class);

	private int transportNumber; // 传输数量

	@Override
	public void sessionOpened(IoSession session) throws Exception {

		ByteFileMessage bfm = new ByteFileMessage();
		PropertiesModel pm = ReadProperties.getModel();
		File fileDir = new File(pm.getClientFilePath());
		if (!(fileDir.isDirectory())) {
			// 如果不是目录
			LOGGER.debug("当前目录：" + pm.getClientFilePath());
			throw new MyRuntimeException("在配置文件中未指定正确的目录路径:clientFilePath");
		}

		if (pm.getClientFileName().equals("")) {
			// 就表示传输的是整个目录
			File[] files = fileDir.listFiles();
			transportNumber = files.length;
			for (int i = 0; i < files.length; i++) {
				LOGGER.info("序号：【" + i + "】文件名：【" + files[i].getName()+"】加进队列中");
				bfm.setSeq(i);
				bfm.setFilePath(files[i].getPath());
				session.write(bfm);
			}
		} else {
			// 不为空，那就代表传输单个文件
			if (!(new File(pm.getClientFilePath() + pm.getClientFileName())
					.isFile())) {
				// 如果不是文件
				LOGGER.debug("当前文件：" + pm.getClientFilePath()
						+ pm.getClientFileName());
				throw new MyRuntimeException("在配置文件中未指定正确的文件路径:clientFileName");
			}
			transportNumber = 1;
			bfm.setSeq(0);
			// 封装文件路径；路径名+文件名
			bfm.setFilePath(pm.getClientFilePath() + pm.getClientFileName());
			session.write(bfm);
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		LOGGER.info("客户端與服务器交互發生異常");
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		ByteReturnFileMessage returnMessage = (ByteReturnFileMessage) message;
		LOGGER.info("返回的序号：" + returnMessage.getSeq());
		LOGGER.info("服务器返回的消息：" + returnMessage.getReturnMassage());
		// 剩下最后一个才进行session的关闭
		if (returnMessage.getSeq() == transportNumber - 1) {
			// 如果是最后一个
			LOGGER.info("客户端自动关闭：所有发往服务器请求已保存成功");
			session.close(true);
		}

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		LOGGER.info("客户端信息已送達");
	}
}
