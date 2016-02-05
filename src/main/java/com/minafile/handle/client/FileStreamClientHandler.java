package com.minafile.handle.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.FileMessage;
import com.minafile.util.IoStreamServerThread;

public class FileStreamClientHandler extends StreamIoHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(FileStreamClientHandler.class);
	@Override
	protected void processStreamIo(IoSession session, InputStream in,
			OutputStream out) {
		
		File sendFile = new File("D:\\Clientfile\\Constants.java");
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			
			LOGGER.info("文件路径" + sendFile.getPath()); // 文件名
			String fileName = sendFile.getName();
			//fileName = fileName.substring(0, fileName.lastIndexOf("."));
			
			//LOGGER.info("文件名" + fileName); // 文件名
			FileMessage fm = new FileMessage();
			fm.setFileEntry(sendFile);
			fm.setFileName(fileName);
			//fileProperty = sendFile.getName();
			//fileProperty = fileProperty.substring(fileProperty.lastIndexOf("."),fileProperty.length());
			//LOGGER.info("文件后缀" + fileProperty); // 文件后缀
			//fis =new ByteArrayInputStream());
			fis = new FileInputStream( sendFile);
		} catch (FileNotFoundException e) {
			LOGGER.error("客户端找不到文件",e);
		} catch (IOException e) {
			LOGGER.error("添加对象流出错",e);
			e.printStackTrace();
		}
		// Runnable target = new IoStreamClientThread(out,fis);
		// Runnable target = new IoStreamServerThread(out,fis);
		new Thread(new IoStreamServerThread(out,fis)).start(); 
		//session.write(fis);
		 return; 
	}
}
