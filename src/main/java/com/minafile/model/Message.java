package com.minafile.model;

/**
 * 服务器接受的消息(即客户端发送的消息)
 * @author king_fu
 *
 */
public class Message extends AbstractMessage {

    /**
	 * 序列化
	 */
	private static final long serialVersionUID = 3982238087285303145L;
	private int value;

    public Message() {
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        // it is a good practice to create toString() method on message classes.
        return getSequence() + ":value(" + value + ')';
    }
}
