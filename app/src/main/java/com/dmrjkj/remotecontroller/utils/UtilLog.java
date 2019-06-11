package com.dmrjkj.remotecontroller.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by husiolois on 16-8-12.
 */

public class UtilLog {

    public static void d(String message) {
        Log.d("UtilDebug", message);
    }

    public static void e(String message) {
        Log.e("UtilDebug", message);
    }

    public static void e(String message, Throwable throwable) {
        Log.e("UtilDebug", message, throwable);
    }

    public static void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
