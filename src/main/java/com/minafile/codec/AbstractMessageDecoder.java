package com.minafile.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import com.minafile.model.AbstractMessage;

/**
 * 父类解码
 * @author king_fu
 *
 */
public abstract class AbstractMessageDecoder<T extends AbstractMessage> implements MessageDecoder {
    private final int type;

    private int sequence;

    private boolean readHeader;

    protected AbstractMessageDecoder(int type) {
        this.type = type;
    }

    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
    	// 判断是否符合最小的头长度。一个short+一个int类型。
    	/**
    	 *  // 编码头
         * 	   buf.putShort((short) type);
         *     buf.putInt(message.getSequence());
    	 */
        if (in.remaining() < Constants.HEADER_LEN) {
            return MessageDecoderResult.NEED_DATA;
        }
        if (type == in.getShort()) {
            return MessageDecoderResult.OK;
        }
        return MessageDecoderResult.NOT_OK;
    }

    public MessageDecoderResult decode(IoSession session, IoBuffer in,
            ProtocolDecoderOutput out) throws Exception {
         
        if (!readHeader) {
            in.getShort();  
            sequence = in.getInt(); 
            readHeader = true;
        }

        AbstractMessage m = decodeBody(session, in);
        if (m == null) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            readHeader = false;  
        }
        m.setSequence(sequence);
        out.write(m);

        return MessageDecoderResult.OK;
    }

  
    /**
     * 模版方法
     * @param session 当前的session
     * @param in 输入流
     * @return	如果对象没有读完返回null
     */
    protected abstract AbstractMessage decodeBody(IoSession session,
            IoBuffer in);
}
