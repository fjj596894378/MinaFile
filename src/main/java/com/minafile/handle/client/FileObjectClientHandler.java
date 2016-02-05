package com.minafile.handle.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.ByteFileMessage;
import com.minafile.model.ByteFileMessageTransport;
import com.minafile.model.ByteReturnFileMessage;
import com.minafile.model.MessageBufferedInputStream;
import com.minafile.model.PropertiesModel;
import com.minafile.util.ReadProperties;

public class FileObjectClientHandler extends IoHandlerAdapter {

	private static final  Logger LOGGER = LoggerFactory.getLogger(FileObjectClientHandler.class);
 



	@Override
	public void sessionOpened(IoSession session) throws Exception {
		
		ByteFileMessage fm = new ByteFileMessage();
		ByteFileMessageTransport fmt = new ByteFileMessageTransport();
		PropertiesModel pm = ReadProperties.getModel();
		File[] files = null;
		File file = null;
		MessageBufferedInputStream mbis = new MessageBufferedInputStream();
		if(pm.getClientFileName().equals("")){
			file = new File(pm.getClientFilePath());
			if(file.isFile()){
				// 是一个文件
				fmt.setDir(false);
				/*FileInputStream fis = new FileInputStream(file);
				
				FileChannel fileChannel = fis.getChannel();
				LOGGER.info("fileChannel.size()" + fileChannel.size());
				ByteBuffer bb = ByteBuffer.allocate((int)fileChannel.size());
				bb.clear();
				fileChannel.read(bb);
				fm.setFileName(file.getName());
				fm.setFileStream(bb.array());
				
				IoBuffer ioBuffer = IoBuffer.allocate((int)(fm.getFileName().length() + fileChannel.size()));
				ioBuffer.setAutoExpand(true);
				ioBuffer.putString(fm.getFileName(),character);
				ioBuffer.put(bb);
				ioBuffer.put(fm.getFileStream());
				LOGGER.info("send remaining" + ioBuffer.limit());
				ioBuffer.flip();
				LOGGER.info("数据已经准备好，准备发送");*/
				session.write(operateStream(file,1));
			}else{
				// 是一个目录
				fmt.setDir(true); // 文件名为空，表示传输保存的是一个目录
				files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					session.write(operateStream(files[i],i));
				}
			}
		}else{
			fmt.setDir(false);
			file = new File(pm.getClientFilePath() + pm.getClientFileName());
			session.write(operateStream(file,1));
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
		ByteReturnFileMessage returnMessage = (ByteReturnFileMessage)message;
		LOGGER.info("返回的序号："+ returnMessage.getSeq());
		LOGGER.info("服务器返回的消息："+ returnMessage.getReturnMassage());
		session.close(true);
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		LOGGER.info("客户端信息已送達");
	}
	
	private IoBuffer operateStream(File file,int seq) throws IOException{
		CharsetEncoder character = Charset.forName("UTF-8").newEncoder();
		ByteFileMessage fm = new ByteFileMessage();
		FileInputStream fis = new FileInputStream(file);
		FileChannel fileChannel = fis.getChannel();
		LOGGER.info("fileChannel.size()" + fileChannel.size());
		ByteBuffer bb = ByteBuffer.allocate((int)fileChannel.size());
		bb.clear();
		fileChannel.read(bb);
		fm.setSeq(seq);
		fm.setFileName(file.getName());
		LOGGER.info("file.getName()" + fm.getFileName());
		fm.setFileStream(bb.array());
		
		IoBuffer ioBuffer = IoBuffer.allocate((int)(4 + 4+4 + fm.getFileName().length() + fileChannel.size()));
		ioBuffer.setAutoExpand(true);
		ioBuffer.putInt(fm.getSeq());
		
		/*ioBuffer.flip();    
		ioBuffer.compact(); */
		ioBuffer.putInt(fm.getFileName().getBytes().length);
		LOGGER.info("fileChannel.size():" + fileChannel.size());
		ioBuffer.putString(fm.getFileName(),character);
		/*ioBuffer.flip();    
		ioBuffer.compact();*/
		
		ioBuffer.putInt((int)fileChannel.size());
		/*ioBuffer.flip();    
		ioBuffer.compact();*/
		
		LOGGER.info("character:" + character.toString());
		ioBuffer.put(bb);
		ioBuffer.put(fm.getFileStream());
		LOGGER.info("send remaining" + ioBuffer.limit());
		ioBuffer.flip();
		LOGGER.info("数据已经准备好，准备发送");
		
		return ioBuffer;
	}
}
