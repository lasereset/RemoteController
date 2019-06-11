package com.dmrjkj.remotecontroller.bluetooth.parse;

import android.util.Log;

import com.clj.fastble.utils.HexUtil;
import com.dmrjkj.remotecontroller.bluetooth.entity.Password;
import com.dmrjkj.remotecontroller.bluetooth.entity.SPY;
import com.dmrjkj.remotecontroller.utils.CRC8;
import com.dmrjkj.remotecontroller.utils.ToolUtils;

/**
 * Created by xinchen on 19-5-27.
 */

public class SPYParseCore extends ParseCore {
    private Object makeObj(String paramString1, String paramString2) {
        int i = -1;
        if (paramString2.equals("02")) {
            i = 0;
        } else if (paramString2.equals("05")) {
            i = 1;
        } else if (paramString2.equals("04")) {
            i = 2;
        } else if (paramString2.equals("03")) {
            i = 3;
        } else {
            return new SPY();
        }
        Log.d("Device", "this SPYParseCore makeObj:" + paramString1 + "===" + paramString2);
        if (i == 0) {
            paramString1 = ToolUtils.hexString2binaryString(paramString1);
            SPY spy = new SPY();
            spy.setLeveling(spy.makeLeveling(paramString1.substring(0, 2)));
            spy.setHand(spy.makeHand(paramString1.substring(2, 4)));
            spy.setWarn(spy.makeWarn(paramString1.substring(4, 6)));
            spy.setTitl(spy.makeTilt(paramString1.substring(6, 8)));
            spy.setCal(spy.makeCal(paramString1.substring(8, 10)));
            spy.setSlope(spy.makeSlope(paramString1.substring(10, 12)));
            spy.setSpeed(spy.makeSpeed(paramString1.substring(12, 14)));
            spy.setScan(spy.makeScan(paramString1.substring(14, 16)));
            spy.setDrop(spy.makeDrop(paramString1.substring(26, 28)));
            spy.setBatteryStatus(spy.makeBatteryStatus(paramString1.substring(28, 30)));
            spy.setElectricity(spy.makeElectricity(paramString1.substring(30, 32)));
            return spy;
        } else {
            if (paramString1.equals("00")) {
                return Password.NO;
            }
            return Password.YES;
        }
    }

    public Result parse(String paramString) {
        if (!paramString.contains("FF")) {
            return new Result(-1);
        }
        paramString = paramString.substring(paramString.indexOf("FF"));
        if (paramString.length() < 10) {
            return new Result(100);
        }
        int i = Integer.parseInt(paramString.substring(2, 4), 16);
        if (i * 2 + 5 > paramString.length()) {
            return new Result(100);
        }
        paramString.substring(4, 6);
        String str1 = paramString.substring(6, 8);
        String str2 = paramString.substring(8, i * 2 + 4);
        if (Integer.parseInt(paramString.substring(i * 2 + 4, i * 2 + 6), 16) == CRC8.calc(HexUtil.hexStringToBytes(paramString.substring(4, i * 2 + 4)))) {
            String str3 = paramString.substring(0, i * 2 + 6);
            Result localResult = new Result();
            localResult.setFrameObj(makeObj(str2, str1));
            localResult.setCode(200);
            localResult.setStringFrame(str3);
            if (paramString.length() > i * 2 + 6) {
                localResult.setRemainFrame(paramString.substring(i * 2 + 6));
                return localResult;
            }
            localResult.setRemainFrame("");
            return localResult;
        }
        return new Result();
    }
}
