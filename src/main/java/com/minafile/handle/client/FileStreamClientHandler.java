package com.minafile.handle.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.util.IoStreamServerThread;

public class FileStreamClientHandler extends StreamIoHandler {

	private final static Logger LOGGER = LoggerFactory.getLogger(FileStreamClientHandler.class);
	
	@Override
	protected void processStreamIo(IoSession session, InputStream in,
			OutputStream out) {
		
		File sendFile = new File("D:\\Clientfile\\Constants.java");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(sendFile);
		} catch (FileNotFoundException e) {
			LOGGER.error("客户端找不到文件",e);
		}
		// Runnable target = new IoStreamClientThread(out,fis);
		// Runnable target = new IoStreamServerThread(out,fis);
		new Thread(new IoStreamServerThread(out,fis)).start(); 
		//session.write(fis);
		 return; 
	}
	
	

}
