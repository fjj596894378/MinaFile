package com.minafile.handle.server;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.ByteFileMessage;
import com.minafile.model.ByteReturnFileMessage;
import com.minafile.model.PropertiesModel;
import com.minafile.util.ReadProperties;

public class FileObjectServerHandler extends IoHandlerAdapter {

	private static int BUFF_SIZE = 2048;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FileObjectServerHandler.class);

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		LOGGER.info("客戶端又來了");
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		LOGGER.info("客戶端:" + session.getRemoteAddress().toString());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		LOGGER.info("客戶端失去連接");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		LOGGER.info("服務器空閒中");
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		LOGGER.info("服務器與客戶端交互發生異常");
		super.exceptionCaught(session, cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		ByteFileMessage fm = (ByteFileMessage) message;
		 
		PropertiesModel pm = ReadProperties.getModel();
		OutputStream os = null;
		InputStream is = null;
		 
		/*int tmpdata = 0;
		byte[] byteBuf = new byte[BUFF_SIZE];*/
		try {

		//	for (int i = 0; i < fileList.size(); i++) {
				 
			//	is = new BufferedInputStream(new FileInputStream(fm.getFileEntry().getPath().toString()));
				os = new ObjectOutputStream(new FileOutputStream(
						pm.getServerFilePath() + System.currentTimeMillis()
								+ fm.getFileName()));
			/*	while ((tmpdata = is.read(byteBuf)) != -1) {
					os.write(byteBuf, 0, tmpdata);
				}*/
				os.write(fm.getFileStream());
				os.flush();
		//	}
		} finally {
			os.close();
		}
		ByteReturnFileMessage brf = new ByteReturnFileMessage();
		brf.setSeq(fm.getSeq());
		brf.setReturnMassage("服务器保存文件完成。");
		brf.setReturnMassageLength(brf.getReturnMassage().getBytes().length);
		session.write(brf);

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		LOGGER.info("服務器信息已送達");
	}
}
