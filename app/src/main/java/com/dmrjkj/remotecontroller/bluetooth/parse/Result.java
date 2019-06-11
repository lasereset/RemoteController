package com.dmrjkj.remotecontroller.bluetooth.parse;

/**
 * Created by xinchen on 19-5-27.
 */

public class Result<T> {
    private int code;
    private T frameObj;
    private String remainFrame;
    private String stringFrame;

    public Result() {
    }

    public Result(int paramInt) {
        this.code = paramInt;
    }

    public int getCode() {
        return this.code;
    }

    public T getFrameObj() {
        return (T) this.frameObj;
    }

    public String getRemainFrame() {
        return this.remainFrame;
    }

    public String getStringFrame() {
        return this.stringFrame;
    }

    public void setCode(int paramInt) {
        this.code = paramInt;
    }

    public void setFrameObj(T paramT) {
        this.frameObj = paramT;
    }

    public void setRemainFrame(String paramString) {
        this.remainFrame = paramString;
    }

    public void setStringFrame(String paramString) {
        this.stringFrame = paramString;
    }
}
