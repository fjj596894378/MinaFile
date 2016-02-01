package com.minafile.handle.client;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.minafile.model.Message;
import com.minafile.model.ResultMessage;

/**
 * 客户端接收服务器的消息并进行处理
 * @author king_fu
 *
 */
public class FileUploadClientHandler extends IoHandlerAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(FileUploadClientHandler.class);
    
    private final int[] values;

    private boolean finished;

    public FileUploadClientHandler(int[] values) {
        this.values = values;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public void sessionOpened(IoSession session) {
         
        for (int i = 0; i < values.length; i++) {
            Message m = new Message();
            m.setSequence(i);
            m.setValue(values[i]);
            session.write(m);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
    	// 接收服务器的消息。强转。
        ResultMessage rm = (ResultMessage) message;
        if (rm.isOk()) {
            // 服务器返回true
        	// 如果返回的Sequence是最后一个，那么就关闭连接。
            if (rm.getSequence() == values.length - 1) {
                // 打印值并关闭连接
                LOGGER.info("客户端收到服务器的值: " + rm.getValue());
                session.close(true);
                finished = true;
            }
        } else {
            LOGGER.warn("服务器停止, 断开连接...");
            session.close(true);
            finished = true;
        }
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.close(true);
    }
}