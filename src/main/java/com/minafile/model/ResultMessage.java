package com.minafile.model;

/**
 * 服务器返回的消息(即客户端接收的消息)
 * 
 * @author king_fu
 * 
 */
public class ResultMessage extends AbstractMessage {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 2120629824004587146L;
	private boolean ok;

	private int value;

	public ResultMessage() {
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String toString() {
		if (ok) {
			return getSequence() + ":RESULT(" + value + ')';
		} else {
			return getSequence() + ":RESULT(ERROR)";
		}
	}
}
