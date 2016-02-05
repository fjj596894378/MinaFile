package com.minafile.model;

import java.io.File;
import java.io.InputStream;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 文件消息
 * 服务器接受的消息(即客户端发送的消息)
 * @author king_fu
 *
 */
public class FileMessage extends AbstractFile{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 2662457203413500892L;

	private String fileName; // 文件名
	
	private File fileEntry; // 文件实体
	
	private byte[] fileStream; // 文件流
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	 

	public byte[] getFileStream() {
		return fileStream;
	}

	public void setFileStream(byte[] fileStream) {
		this.fileStream = fileStream;
	}

	public File getFileEntry() {
		return fileEntry;
	}

	public void setFileEntry(File fileEntry) {
		this.fileEntry = fileEntry;
	}
	
}
