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

public class FileObjectClientHandler extends IoHandlerAdapter {

	private static final  Logger LOGGER = LoggerFactory.getLogger(FileObjectClientHandler.class);
 



	@Override
	public void sessionOpened(IoSession session) throws Exception {
		
		ByteFileMessage bfm = new ByteFileMessage();
		PropertiesModel pm = ReadProperties.getModel();
		if(!(new File(pm.getClientFilePath()).isDirectory())){
			// 如果不是目录
			LOGGER.debug("当前目录：" + pm.getClientFilePath());
			throw new MyRuntimeException("在配置文件中未指定正确的目录路径:clientFilePath");
		}
		
		if(!(new File(pm.getClientFilePath() + pm.getClientFileName()).isFile())){
			// 如果不是文件
			LOGGER.debug("当前文件：" + pm.getClientFilePath() + pm.getClientFileName());
			throw new MyRuntimeException("在配置文件中未指定正确的文件路径:clientFileName");
		}
		
		bfm.setSeq(1);
		// 封装文件路径；路径名+文件名
		bfm.setFilePath(pm.getClientFilePath() + pm.getClientFileName());
		session.write(bfm);
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
		ByteReturnFileMessage returnMessage = (ByteReturnFileMessage)message;
		LOGGER.info("返回的序号："+ returnMessage.getSeq());
		LOGGER.info("服务器返回的消息："+ returnMessage.getReturnMassage());
		session.close(true);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		LOGGER.info("客户端信息已送達");
	}
}
