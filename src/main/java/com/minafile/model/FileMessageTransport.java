package com.minafile.model;

import java.io.Serializable;
import java.util.List;

/**
 * 文件消息
 * 服务器接受的消息(即客户端发送的消息)
 * @author king_fu
 *
 */
public class FileMessageTransport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4431084222740016304L;

	private List<FileMessage> fileList; // 文件名
	
	private boolean isDir; // 是否是目录

	public List<FileMessage> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileMessage> fileList) {
		this.fileList = fileList;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

}
