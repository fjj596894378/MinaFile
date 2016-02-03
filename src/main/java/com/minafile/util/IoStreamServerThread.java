package com.minafile.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IoStreamServerThread extends Thread{

	public static final int BUFFER_SIZE = 1024*2;  // 缓冲。
	private final static Logger LOGGER = LoggerFactory.getLogger(IoStreamServerThread.class);
	
	private BufferedOutputStream bos;
	private BufferedInputStream bis;
	public IoStreamServerThread() {
		super();
	}

	public IoStreamServerThread(OutputStream bosParam, InputStream bisParam) {
		super();
		this.bos = new BufferedOutputStream(bosParam);
		this.bis = new BufferedInputStream(bisParam);
	}

	public BufferedOutputStream getBos() {
		return bos;
	}

	public void setBos(BufferedOutputStream bos) {
		this.bos = bos;
	}

	public BufferedInputStream getBis() {
		return bis;
	}

	public void setBis(BufferedInputStream bis) {
		this.bis = bis;
	}


	@Override
	public synchronized void run() {
		 byte[] byteValue = new byte[BUFFER_SIZE];
		 int tmpData = 0;
		 try {
			 while(true){
				 tmpData = bis.read(byteValue);
	                if(tmpData == -1){
	                    break;
	                }
	                bos.write(byteValue,0,tmpData);
	          }      
			try {
				bos.flush();
			}catch (IOException e) {
				LOGGER.error("输出流出错",e);
			}
		} catch (IOException e) {
			LOGGER.error("读取流出错",e);
		}finally{
			try {
				bos.close();
				bis.close();
			} catch (IOException e) {
				LOGGER.error("关闭流出错",e);
			}
		}
		
	}

}
