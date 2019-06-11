package com.dmrjkj.remotecontroller.bluetooth.parse;


import com.clj.fastble.utils.HexUtil;

/**
 * Created by xinchen on 19-5-27.
 */
public class ParseResult<T> {
    private byte[] bytesFrame;
    private T frameObj;
    private String stringFrame;

    public ParseResult() {
    }

    public ParseResult(String paramString, T paramT) {
        this.stringFrame = paramString;
        this.frameObj = paramT;
        this.bytesFrame = HexUtil.hexStringToBytes(paramString);
    }

    public byte[] getBytesFrame() {
        return this.bytesFrame;
    }

    public T getFrameObj() {
        return (T) this.frameObj;
    }

    public String getStringFrame() {
        return this.stringFrame;
    }

    public void setBytesFrame(byte[] paramArrayOfByte) {
        this.bytesFrame = paramArrayOfByte;
    }

    public void setFrameObj(T paramT) {
        this.frameObj = paramT;
    }

    public void setStringFrame(String paramString) {
        this.stringFrame = paramString;
    }
}