package com.dmrjkj.remotecontroller.utils;

/**
 * Created by xinchen on 19-5-24.
 */

public class CRC8 {
    public static int calc(byte[] paramArrayOfByte) {
        int CRC = 0;
        int genPoly = 0X31;
        for (int i = 0; i < paramArrayOfByte.length; i++) {
            CRC ^= paramArrayOfByte[i];
            for (int j = 0; j < 8; j++) {
                if ((CRC & 0x80) != 0) {
                    CRC = (CRC << 1) ^ genPoly;
                } else {
                    CRC <<= 1;
                }
            }
        }
        CRC &= 0xff;
        return CRC;
    }
}
