package com.minafile.model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class MessageBufferedInputStream  implements Serializable{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -8547361230397910247L;
	public MessageBufferedInputStream(){
		
	}
	InputStream bis;
	public InputStream getBis() {
		return bis;
	}
	public void setBis(InputStream bis) {
		this.bis = bis;
	}
	
}
