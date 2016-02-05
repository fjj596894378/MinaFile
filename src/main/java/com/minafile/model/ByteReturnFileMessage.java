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
public class ByteReturnFileMessage implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 2662457203413500892L;

	private int seq; // 序号
	
	private String returnMassage; // 文件名
	
	private int returnMassageLength;//图片名字长度
	
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getReturnMassage() {
		return returnMassage;
	}

	public void setReturnMassage(String returnMassage) {
		this.returnMassage = returnMassage;
	}

	public int getReturnMassageLength() {
		return returnMassageLength;
	}

	public void setReturnMassageLength(int returnMassageLength) {
		this.returnMassageLength = returnMassageLength;
	}
	
	
}
