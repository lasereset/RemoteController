package com.dmrjkj.remotecontroller.bluetooth.util;

import com.clj.fastble.utils.HexUtil;
import com.dmrjkj.remotecontroller.utils.CRC8;

/**
 * Created by xinchen on 19-5-24.
 */

public class Command {

    public static final String UUID_WRITE_SERVICE = "0000ffe5-0000-1000-8000-00805f9b34fb";
    public static final String UUID_WRITE_CHARACTERISTIC = "0000ffe9-0000-1000-8000-00805f9b34fb";
    public static final String UUID_NOTIFY_SERVICE = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static final String UUID_NOTIFY_CHARACTERISTIC = "0000ffe4-0000-1000-8000-00805f9b34fb";

    public static final String CLOSE_STATUS = makeFrame("01", "02", "03");
    public static final String DOWN;
    public static final String DOWN_END;
    public static final String DOWN_START;
    public static final String DROP;
    public static final String HAND;
    public static final String HAND_END;
    public static final String HAND_START;
    public static final String LEFT;
    public static final String LEFT_1;
    public static final String LEFT_1_END;
    public static final String LEFT_1_START;
    public static final String LEFT_2;
    public static final String LEFT_2_END;
    public static final String LEFT_2_START;
    public static final String LEFT_END;
    public static final String LEFT_START;
    public static final String OPEN_STATUS;
    public static final String RANGE_BENCHMARK_CHANGE;
    public static final String RANGE_CLOSE;
    public static final String RANGE_DISTANCE;
    public static final String RANGE_OPEN;
    public static final String RANGE_SHUTDOWN;
    public static final String RIGHT;
    public static final String RIGHT_1;
    public static final String RIGHT_1_END;
    public static final String RIGHT_1_START;
    public static final String RIGHT_2;
    public static final String RIGHT_2_END;
    public static final String RIGHT_2_START;
    public static final String RIGHT_END;
    public static final String RIGHT_START;
    public static final String SCAN;
    public static final String SCAN_END;
    public static final String SCAN_START;
    public static final String SELECT_STATUS;
    public static final String SLOPE;
    public static final String SLOPE_END;
    public static final String SLOPE_START;
    public static final String SPEED;
    public static final String SPEED_END;
    public static final String SPEED_START;
    public static final String TILT;
    public static final String TILT_END;
    public static final String TILT_START;
    public static final String TX_HORI_FRONT;
    public static final String TX_HORI_FRONT_END;
    public static final String TX_HORI_FRONT_START;
    public static final String TX_MC;
    public static final String TX_MC_END;
    public static final String TX_MC_START;
    public static final String TX_VERT_AFTER;
    public static final String TX_VERT_AFTER_END;
    public static final String TX_VERT_AFTER_START;
    public static final String TX_VERT_LEFT;
    public static final String TX_VERT_LEFT_END;
    public static final String TX_VERT_LEFT_START;
    public static final String UP = makeFrame("01", "01", "06");
    public static final String UP_END;
    public static final String UP_START;

    static {
        DOWN = makeFrame("01", "01", "07");
        LEFT = makeFrame("01", "01", "08");
        RIGHT = makeFrame("01", "01", "09");
        TILT = makeFrame("01", "01", "03");
        SCAN = makeFrame("01", "01", "04");
        HAND = makeFrame("01", "01", "02");
        SPEED = makeFrame("01", "01", "0a");
        SLOPE = makeFrame("01", "01", "05");
        DROP = makeFrame("01", "01", "0b");
        UP_START = makeFrame("01", "01", "16");
        DOWN_START = makeFrame("01", "01", "17");
        LEFT_START = makeFrame("01", "01", "18");
        RIGHT_START = makeFrame("01", "01", "19");
        TILT_START = makeFrame("01", "01", "13");
        SCAN_START = makeFrame("01", "01", "14");
        HAND_START = makeFrame("01", "01", "12");
        SPEED_START = makeFrame("01", "01", "1a");
        SLOPE_START = makeFrame("01", "01", "15");
        UP_END = makeFrame("01", "01", "26");
        DOWN_END = makeFrame("01", "01", "27");
        LEFT_END = makeFrame("01", "01", "28");
        RIGHT_END = makeFrame("01", "01", "29");
        TILT_END = makeFrame("01", "01", "23");
        SCAN_END = makeFrame("01", "01", "24");
        HAND_END = makeFrame("01", "01", "22");
        SPEED_END = makeFrame("01", "01", "2a");
        SLOPE_END = makeFrame("01", "01", "25");
        LEFT_1 = makeFrame("01", "01", "06");
        RIGHT_1 = makeFrame("01", "01", "07");
        LEFT_2 = makeFrame("01", "01", "08");
        RIGHT_2 = makeFrame("01", "01", "09");
        LEFT_1_START = makeFrame("01", "01", "16");
        RIGHT_1_START = makeFrame("01", "01", "17");
        LEFT_2_START = makeFrame("01", "01", "18");
        RIGHT_2_START = makeFrame("01", "01", "19");
        LEFT_1_END = makeFrame("01", "01", "26");
        RIGHT_1_END = makeFrame("01", "01", "27");
        LEFT_2_END = makeFrame("01", "01", "28");
        RIGHT_2_END = makeFrame("01", "01", "29");
        TX_HORI_FRONT = makeFrame("01", "01", "05");
        TX_VERT_LEFT = makeFrame("01", "01", "0a");
        TX_VERT_AFTER = makeFrame("01", "01", "0c");
        TX_MC = makeFrame("01", "01", "0d");
        TX_HORI_FRONT_START = makeFrame("01", "01", "15");
        TX_VERT_LEFT_START = makeFrame("01", "01", "1a");
        TX_VERT_AFTER_START = makeFrame("01", "01", "1c");
        TX_MC_START = makeFrame("01", "01", "1d");
        TX_HORI_FRONT_END = makeFrame("01", "01", "25");
        TX_VERT_LEFT_END = makeFrame("01", "01", "2a");
        TX_VERT_AFTER_END = makeFrame("01", "01", "2c");
        TX_MC_END = makeFrame("01", "01", "2d");
        RANGE_OPEN = makeFrame("01", "01", "01");
        RANGE_CLOSE = makeFrame("01", "01", "02");
        RANGE_DISTANCE = makeFrame("01", "01", "03");
        RANGE_SHUTDOWN = makeFrame("01", "01", "04");
        RANGE_BENCHMARK_CHANGE = makeFrame("01", "01", "05");
        SELECT_STATUS = makeFrame("01", "02", "01");
        OPEN_STATUS = makeFrame("01", "02", "02");
    }

    public static String makeChangePwdFrame(String paramString) {
        return makeFrame("01", "04", paramString);
    }

    private static String makeFrame(String paramString1, String paramString2, String paramString3) {
        String str1 = HexUtil.encodeHexStr(new byte[]{(byte) CRC8.calc(HexUtil.hexStringToBytes(paramString1 + paramString2 + paramString3))});
        String str2 = str2hexString(Integer.toHexString(paramString1.length() / 2 + paramString2.length() / 2 + paramString3.length() / 2));
        System.out.println("=====> makeFrameFF" + str2 + paramString1 + paramString2 + paramString3 + str1);
        return "FF" + str2 + paramString1 + paramString2 + paramString3 + str1;
    }

    public static String makeInputPwdFrame(String paramString) {
        return makeFrame("01", "05", paramString);
    }

    public static String makeSetPwdFrame(String paramString) {
        return makeFrame("01", "03", paramString);
    }

    public static String str2hexString(String paramString) {
        StringBuffer localStringBuffer = new StringBuffer();
        int i = 0;
        while (i < paramString.length()) {
            localStringBuffer.append("0" + paramString.substring(i, i + 1));
            i += 1;
        }
        return localStringBuffer.toString();
    }
}
