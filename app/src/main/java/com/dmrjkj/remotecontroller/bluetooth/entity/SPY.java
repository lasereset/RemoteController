package com.dmrjkj.remotecontroller.bluetooth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by xinchen on 19-5-27.
 */

@Data
@NoArgsConstructor
public class SPY {

    private boolean leveling;

    private boolean hand;

    private int warn;//1超范围报警 2TILT报警 3非报警

    private boolean titl;

    private int cal;//0 CAL关闭 1 CAL-X 2 CAL-Y 3 CAL-Z

    private int slope;//0 坡度关闭 1 X/Z坡度 2 Y坡度 3 Z坡度

    private int speed;// 0 0RPM 1 150RPM 2 300RPM 3 600RPM;

    private int scan;// 0 扇扫关闭/转速开启 1 15°扇扫 2 30°扇扫 3 60°扇扫;

    private boolean drop;

    private int batteryStatus;//0 碱性电池 1 锂电池且非充电中 2 锂电池充电中 3 锂电池充满电

    private int electricity;//0 空电量 1 1格电量 2 2格电量 3 3格电量

    public boolean makeLeveling(String substring) {
        return "00".equals(substring);
    }

    public boolean makeHand(String substring) {
        return "11".equals(substring);
    }

    public int makeWarn(String substring) {
        return "11".equals(substring) ? 1 : ("10".equals(substring) ? 2 : 0);
    }

    public boolean makeTilt(String substring) {
        return "11".equals(substring);
    }

    public int makeCal(String substring) {
        if ("11".equals(substring)) {
            return 3;
        } else if ("01".equals(substring)) {
            return 1;
        } else if ("10".equals(substring)) {
            return 2;
        } else {
            return 0;
        }
    }

    public int makeSlope(String substring) {
        if ("11".equals(substring)) {
            return 3;
        } else if ("01".equals(substring)) {
            return 1;
        } else if ("10".equals(substring)) {
            return 2;
        } else {
            return 0;
        }
    }

    public int makeSpeed(String substring) {
        if ("11".equals(substring)) {
            return 3;
        } else if ("01".equals(substring)) {
            return 1;
        } else if ("10".equals(substring)) {
            return 2;
        } else {
            return 0;
        }
    }

    public int makeScan(String substring) {
        if ("11".equals(substring)) {
            return 3;
        } else if ("01".equals(substring)) {
            return 1;
        } else if ("10".equals(substring)) {
            return 2;
        } else {
            return 0;
        }
    }

    public boolean makeDrop(String substring) {
        return "11".equals(substring);
    }

    public int makeBatteryStatus(String substring) {
        if ("11".equals(substring)) {
            return 3;
        } else if ("01".equals(substring)) {
            return 1;
        } else if ("10".equals(substring)) {
            return 2;
        } else {
            return 0;
        }
    }

    public int makeElectricity(String substring) {
        if ("11".equals(substring)) {
            return 3;
        } else if ("01".equals(substring)) {
            return 1;
        } else if ("10".equals(substring)) {
            return 2;
        } else {
            return 0;
        }
    }
}
