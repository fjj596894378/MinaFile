package com.minafile.model;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 文件消息
 * 服务器接受的消息(即客户端发送的消息)
 * @author king_fu
 *
 */
public class ByteFileMessage implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 2662457203413500892L;

	private int seq; // 序号
	
	private String fileName; // 文件名
	
	private int fileStreamLength; // 文件长度
	private int fileNameLength;//图片名字长度
	
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

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getFileStreamLength() {
		return fileStreamLength;
	}

	public void setFileStreamLength(int fileStreamLength) {
		this.fileStreamLength = fileStreamLength;
	}

	public int getFileNameLength() {
		return fileNameLength;
	}

	public void setFileNameLength(int fileNameLength) {
		this.fileNameLength = fileNameLength;
	}

	
}
