package com.dmrjkj.remotecontroller.bluetooth.action;

import com.clj.fastble.callback.BleWriteCallback;
import com.dmrjkj.remotecontroller.bluetooth.entity.Device;
import com.dmrjkj.remotecontroller.bluetooth.util.Command;

/**
 * Created by xinchen on 19-5-27.
 */

public class SPYAction
        extends Action {
    public SPYAction(Device paramDevice) {
        super(paramDevice);
    }

    public void actionChangePwd(String paramString) {
        actionChangePwd(paramString, null);
    }

    public void actionChangePwd(String paramString, BleWriteCallback paramBleWriteCallback) {
        write(Command.makeChangePwdFrame(paramString), paramBleWriteCallback);
    }

    public void actionDown() {
        actionDown(null);
    }

    public void actionDown(BleWriteCallback paramBleWriteCallback) {
        write(Command.DOWN, paramBleWriteCallback);
    }

    public void actionDownEnd() {
        actionDownEnd(null);
    }

    public void actionDownEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.DOWN_END, paramBleWriteCallback);
    }

    public void actionDownStart() {
        actionDwonStart(null);
    }

    public void actionDrop() {
        actionDrop(null);
    }

    public void actionDrop(BleWriteCallback paramBleWriteCallback) {
        write(Command.DROP, paramBleWriteCallback);
    }

    public void actionDwonStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.DOWN_START, paramBleWriteCallback);
    }

    public void actionHand() {
        actionHand(null);
    }

    public void actionHand(BleWriteCallback paramBleWriteCallback) {
        write(Command.HAND, paramBleWriteCallback);
    }

    public void actionHandEnd() {
        actionHandEnd(null);
    }

    public void actionHandEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.HAND_END, paramBleWriteCallback);
    }

    public void actionHandStart() {
        actionHandStart(null);
    }

    public void actionHandStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.HAND_START, paramBleWriteCallback);
    }

    public void actionInputPwd(String paramString) {
        actionInputPwd(paramString, null);
    }

    public void actionInputPwd(String paramString, BleWriteCallback paramBleWriteCallback) {
        write(Command.makeInputPwdFrame(paramString), paramBleWriteCallback);
    }

    public void actionLeft() {
        actionLeft(null);
    }

    public void actionLeft(BleWriteCallback paramBleWriteCallback) {
        write(Command.LEFT, paramBleWriteCallback);
    }

    public void actionLeftEnd() {
        actionLeftEnd(null);
    }

    public void actionLeftEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.LEFT_END, paramBleWriteCallback);
    }

    public void actionLeftStart() {
        actionLeftStart(null);
    }

    public void actionLeftStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.LEFT_START, paramBleWriteCallback);
    }

    public void actionRight() {
        actionRight(null);
    }

    public void actionRight(BleWriteCallback paramBleWriteCallback) {
        write(Command.RIGHT, paramBleWriteCallback);
    }

    public void actionRightEnd() {
        actionRightEnd(null);
    }

    public void actionRightEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.RIGHT_END, paramBleWriteCallback);
    }

    public void actionRightStart() {
        actionRightStart(null);
    }

    public void actionRightStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.RIGHT_START, paramBleWriteCallback);
    }

    public void actionScan() {
        actionScan(null);
    }

    public void actionScan(BleWriteCallback paramBleWriteCallback) {
        write(Command.SCAN, paramBleWriteCallback);
    }

    public void actionScanEnd() {
        actionScanEnd(null);
    }

    public void actionScanEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.SCAN_END, paramBleWriteCallback);
    }

    public void actionScanStart() {
        actionScanStart(null);
    }

    public void actionScanStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.SCAN_START, paramBleWriteCallback);
    }

    public void actionSetPwd(String paramString) {
        actionSetPwd(paramString, null);
    }

    public void actionSetPwd(String paramString, BleWriteCallback paramBleWriteCallback) {
        write(Command.makeSetPwdFrame(paramString), paramBleWriteCallback);
    }

    public void actionSlope() {
        actionSlope(null);
    }

    public void actionSlope(BleWriteCallback paramBleWriteCallback) {
        write(Command.SLOPE, paramBleWriteCallback);
    }

    public void actionSlopeEnd() {
        actionSlopeEnd(null);
    }

    public void actionSlopeEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.SLOPE_END, paramBleWriteCallback);
    }

    public void actionSlopeStart() {
        actionSlopeStart(null);
    }

    public void actionSlopeStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.SLOPE_START, paramBleWriteCallback);
    }

    public void actionSpeed() {
        actionSpeed(null);
    }

    public void actionSpeed(BleWriteCallback paramBleWriteCallback) {
        write(Command.SPEED, paramBleWriteCallback);
    }

    public void actionSpeedEnd() {
        actionSpeedEnd(null);
    }

    public void actionSpeedEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.SPEED_END, paramBleWriteCallback);
    }

    public void actionSpeedStart() {
        actionSpeedStart(null);
    }

    public void actionSpeedStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.SPEED_START, paramBleWriteCallback);
    }

    public void actionTilt() {
        actionTilt(null);
    }

    public void actionTilt(BleWriteCallback paramBleWriteCallback) {
        write(Command.TILT, paramBleWriteCallback);
    }

    public void actionTiltEnd() {
        actionTiltEnd(null);
    }

    public void actionTiltEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.TILT_END, paramBleWriteCallback);
    }

    public void actionTiltStart() {
        actionTiltStart(null);
    }

    public void actionTiltStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.TILT_START, paramBleWriteCallback);
    }

    public void actionUp() {
        actionUp(null);
    }

    public void actionUp(BleWriteCallback paramBleWriteCallback) {
        write(Command.UP, paramBleWriteCallback);
    }

    public void actionUpEnd() {
        actionUpEnd(null);
    }

    public void actionUpEnd(BleWriteCallback paramBleWriteCallback) {
        write(Command.UP_END, paramBleWriteCallback);
    }

    public void actionUpStart() {
        actionUpStart(null);
    }

    public void actionUpStart(BleWriteCallback paramBleWriteCallback) {
        write(Command.UP_START, paramBleWriteCallback);
    }
}