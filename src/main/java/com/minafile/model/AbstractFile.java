package com.minafile.model;

import java.io.Serializable;
/**
 * 文件信息
 * 服务器接受的消息(即客户端发送的消息)和服务器返回的消息(即客户端接收的消息)的父类。
 * @author king_fu
 *
 */
public class AbstractFile implements Serializable{

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -4612078557669043959L;
	
	private int seq;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

}
