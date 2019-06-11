package com.dmrjkj.remotecontroller;

import android.app.Activity;
import android.app.Application;

import com.clj.fastble.BleManager;
import com.dmrjkj.remotecontroller.bluetooth.BleController;
import com.dmrjkj.remotecontroller.entity.DeviceItem;
import com.dmrjkj.remotecontroller.support.FeedbackController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiajun on 17/4/6.
 */

public class AppApplication extends Application{
    private static AppApplication instance;

    private FeedbackController mFeedbackController;

    private List<Activity> activities = new ArrayList<>();

    private DeviceItem currentDeviceItem;

    public static AppApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        BleController.getInstance().initBle(this);
    }

    public FeedbackController getmFeedbackController() {
        if (mFeedbackController == null) {
            mFeedbackController = new FeedbackController(this);
        }
        return mFeedbackController;
    }

    /**
     * -----------activity操作！--------------
     **/
    //增加activity
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    //移除activity
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //结束指定的class
    public void finishActivityclass(Class<?> cls) {
        if (activities != null) {
            for (Activity activity : activities) {
                if (activity.getClass().equals(cls)) {
                    this.activities.remove(activity);
                    finishActivity(activity);
                    break;
                }
            }
        }
    }

    //结束指定的Activity
    public void finishActivity(Activity activity) {
        if (activity != null) {
            this.activities.remove(activity);
            activity.finish();
        }
    }

    public void exit() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    public void exitExceptSome(Activity except) {
        for (Activity activity : activities) {
            if (except == activity) {
                continue;
            }
            activity.finish();
        }
    }

    public DeviceItem getCurrentDeviceItem() {
        return currentDeviceItem;
    }

    public void setCurrentDeviceItem(DeviceItem currentDeviceItem) {
        this.currentDeviceItem = currentDeviceItem;
    }
}
