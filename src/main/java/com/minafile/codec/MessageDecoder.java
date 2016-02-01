package com.minafile.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.minafile.model.AbstractMessage;
import com.minafile.model.Message;

/**
 * 消息解码
 * @author king_fu
 *
 */
public class MessageDecoder extends AbstractMessageDecoder {

    public MessageDecoder() {
        super(Constants.ADD);
    }

    @Override
    protected AbstractMessage decodeBody(IoSession session, IoBuffer in) {
        if (in.remaining() < Constants.ADD_BODY_LEN) {
            return null;
        }

        Message m = new Message();
        m.setValue(in.getInt());
        return m;
    }

    public void finishDecode(IoSession session, ProtocolDecoderOutput out)
            throws Exception {
    }
}
