package com.minafile.handle.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.util.IoStreamServerThread;

public class FileStreamServerHandler extends StreamIoHandler{
	private final static Logger LOGGER = LoggerFactory
			.getLogger(FileStreamServerHandler.class);

	@Override
	protected void processStreamIo(IoSession session, InputStream in,
			OutputStream out) {
		//省略。
		// 现在需要获取上传文件的文件扩展名。
		// 但是并没有通过流的解析来获取，可以在session中，由客户端传过来。
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3, 6, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3), new ThreadPoolExecutor.DiscardOldestPolicy());
		FileOutputStream fos = null;
		File file =new File("D:\\Serverfile\\" + System.currentTimeMillis() + ".txt"); 
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			LOGGER.error("服务器读取文件流出错",e);
		}
		//将线程放入线程池 当连接很多时候可以通过线程池处理 
		threadPool.execute(new IoStreamServerThread(fos,in));
	}

	@Override
	public void sessionOpened(IoSession session) {
		LOGGER.info("客户端连接了:"+session.getRemoteAddress());
		session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		super.sessionOpened(session);
	}
	
	/**
     * Closes streams
     */
    /*@Override
    public void sessionClosed(IoSession session) throws Exception {
    	LOGGER.info("流自动关闭了。调用父类");
        super.sessionClosed(session);
    }*/
}
