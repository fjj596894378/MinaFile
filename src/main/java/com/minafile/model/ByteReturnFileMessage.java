package com.minafile.model;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 文件消息
 * 服务器发送的消息(即客户端接收的消息)
 * @author king_fu
 *
 */
public class ByteReturnFileMessage implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 2662457203413500892L;

	private int seq; // 序号
	
	private int returnMassage; // 返回消息 0:成功  1：失败  2：其他
	
	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public int getReturnMassage() {
		return returnMassage;
	}

	public void setReturnMassage(int returnMassage) {
		this.returnMassage = returnMassage;
	}
}
