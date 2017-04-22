package com.xkj.binaryoption.utils.SSLSOCKET;

import android.util.Log;

import com.xkj.binaryoption.utils.SocketUtil;
import com.xkj.binaryoption.utils.SystemUtil;

import java.nio.ByteBuffer;

/**
 * @author xjunda
 * @date 2016-09-01
 */
public class SSLEncodeImp implements Encoder<String> {
    @Override
    public void encode(String value, ByteBuffer buffer) throws EncoderException {
        Log.i(SystemUtil.getTAG(this.getClass()), "encode: Encoding request: " + value );
//        Log.i("123", "encode: buffer: " +buffer.remaining() + " strLengh" + value.length());
        synchronized (this){
            buffer.clear();
            buffer.put(SocketUtil.writePureByte(value));
        }
    }
}

