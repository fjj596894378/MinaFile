/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.minafile.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.minafile.model.AbstractMessage;
import com.minafile.model.Message;

/**
 * A {@link MessageDecoder} that decodes {@link AddMessage}.
 *
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
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
