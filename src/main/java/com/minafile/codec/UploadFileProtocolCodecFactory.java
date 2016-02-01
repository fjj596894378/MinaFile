package com.minafile.codec;
 
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

import com.minafile.model.Message;
import com.minafile.model.ResultMessage;

/**
 * 自定义解码工厂。判断是否是服务器还是客户端。
 * @author king_fu
 *
 */
public class UploadFileProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public UploadFileProtocolCodecFactory(boolean isServer) {
    	if (isServer) {
    		//对客户端发过来的信息进行解析（接收）
            super.addMessageDecoder(MessageDecoder.class); 
            // 对发往客户端的信息进行封装（发送）
            super.addMessageEncoder(ResultMessage.class, ResultMessageEncoder.class);
        } else // Client
        {
        	// 对发往服务器的信息进行封装（发送）
            super.addMessageEncoder(Message.class, MessageEncoder.class);
            // 对服务器发过来的信息进行解析（接收）
            super.addMessageDecoder(ResultMessageDecoder.class);
        }
    }
}