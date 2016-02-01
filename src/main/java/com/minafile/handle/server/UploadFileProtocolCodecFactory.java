package com.minafile.handle.server;
 
import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

import com.minafile.codec.MessageDecoder;
import com.minafile.codec.MessageEncoder;
import com.minafile.codec.ResultMessageDecoder;
import com.minafile.codec.ResultMessageEncoder;
import com.minafile.model.Message;
import com.minafile.model.ResultMessage;

/**
 * 自定义解码工厂。判断是否是服务器还是客户端。
 * @author king_fu
 *
 */
public class UploadFileProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public UploadFileProtocolCodecFactory(boolean isServer) {
        
    }
}