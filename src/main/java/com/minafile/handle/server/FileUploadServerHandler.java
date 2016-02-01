package com.minafile.handle.server;

import java.util.Random;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.Message;
import com.minafile.model.ResultMessage;

/**
 * 这个处理类是在服务器接收到了客户端发来的消息时，对客户端的消息进行处理。
 * 
 * @author king_fu
 * 
 */
public class FileUploadServerHandler extends IoHandlerAdapter {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(FileUploadServerHandler.class);

	@Override
	public void sessionOpened(IoSession session) {
		// 设置空闲时间为60秒
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		// message为客户端发来的消息。
		Message am = (Message) message;
		 
		ResultMessage rm = new ResultMessage();
		rm.setSequence(am.getSequence());  
		rm.setOk(true);
		rm.setValue(new Random().nextInt(10));
		session.write(rm);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) {
		LOGGER.info("空闲中");
		session.close(true);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		LOGGER.info(cause.getMessage());
		session.close(true);
	}
}
