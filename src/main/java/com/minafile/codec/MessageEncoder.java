package com.minafile.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.minafile.model.Message;

/**
 * 消息编码
 * @author king_fu
 *
 * @param <T>
 */
public class MessageEncoder<T extends Message> extends AbstractMessageEncoder<T> {
    public MessageEncoder() {
        super(Constants.ADD);
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        out.putInt(message.getValue());
    }

    public void dispose() throws Exception {
    }
}
