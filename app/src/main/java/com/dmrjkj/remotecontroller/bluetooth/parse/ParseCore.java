package com.dmrjkj.remotecontroller.bluetooth.parse;


import com.clj.fastble.utils.HexUtil;

/**
 * Created by xinchen on 19-5-27.
 */

public abstract class ParseCore
{
    public abstract Result parse(String paramString);

    public Result parse(byte[] paramArrayOfByte)
    {
        return parse(HexUtil.encodeHexStr(paramArrayOfByte));
    }
}