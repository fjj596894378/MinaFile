package com.minafile.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import com.minafile.model.ResultMessage;

/**
 * 返回的消息编码
 * @author king_fu
 *
 * @param <T>
 */
public class ResultMessageEncoder<T extends ResultMessage> extends AbstractMessageEncoder<T> {
    public ResultMessageEncoder() {
        super(Constants.RESULT);
    }

    @Override
    protected void encodeBody(IoSession session, T message, IoBuffer out) {
        if (message.isOk()) {
            out.putShort((short) Constants.RESULT_OK);
            out.putInt(message.getValue());
        } else {
            out.putShort((short) Constants.RESULT_ERROR);
        }
    }

    public void dispose() throws Exception {
    }
}
