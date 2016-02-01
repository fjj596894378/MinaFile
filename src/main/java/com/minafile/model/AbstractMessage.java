package com.minafile.model;

import java.io.Serializable;

/**
 * 服务器接受的消息(即客户端发送的消息)和服务器返回的消息(即客户端接收的消息)的父类。
 * @author king_fu
 *
 */
public abstract class AbstractMessage implements Serializable {
    static final long serialVersionUID = 1L;
    
    private int sequence;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
