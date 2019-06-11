package com.dmrjkj.remotecontroller.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.alibaba.fastjson.JSON;
import com.dmrjkj.remotecontroller.AppApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by husiolois on 17-6-17.
 */

@SuppressLint("SimpleDateFormat")
public class ToolUtils {

    public static String hexString2binaryString(String paramString) {
        String localObject;
        if ((paramString == null) || (paramString.length() % 2 != 0)) {
            localObject = null;
            return localObject;
        }
        String str = "";
        int i = 0;
        for (; ; ) {
            localObject = str;
            if (i >= paramString.length()) {
                break;
            }
            localObject = "0000" + Integer.toBinaryString(Integer.parseInt(paramString.substring(i, i + 1), 16));
            str = str + localObject.substring(localObject.length() - 4);
            i += 1;
        }
        return localObject;
    }

    public static String formatTimeTwoPlace(String time) {
        if (time.length() == 1) {
            return "0" + time;
        }
        return time;
    }

    public static String getSN() {
        return getMAC() + getIMEI();
    }

    /**
     * 获得IMEI
     **/
    @SuppressLint("HardwareIds")
    public static String getIMEI() {
        try {
            TelephonyManager phoneMgr = (TelephonyManager) AppApplication.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (null != phoneMgr) {
                return phoneMgr.getDeviceId();
            }
        } catch (Exception e) {
        }
        return "";
    }

    @SuppressLint("HardwareIds")
    public static String getMAC() {
        WifiManager wifiMgr = (WifiManager) AppApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = (null == wifiMgr ? null : wifiMgr.getConnectionInfo());
        if (null != info) {
            return info.getMacAddress();
        }
        return "ffffffffffff";
    }


    /**
     * 复制到粘贴板
     * @param activity
     * @param label
     * @param text
     */
    public static void copyText(Activity activity, String label, String text) {
        ClipboardManager cm = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData myClip = ClipData.newPlainText(label, text);
        cm.setPrimaryClip(myClip);
        ToastUtils.toast("已复制");
    }

    /**
     * 跳转到浏览器
     * @param activity
     */
    public static void intentBrowser(Activity activity, String requestUrl) {
        Intent intent= new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(requestUrl);
        intent.setData(content_url);
        activity.startActivity(intent);
    }

    /**
     * 提取html中a标签的href
     * @param strs
     * @return
     */
    public static Map<String, String> getAHref(String strs){
        Map<String, String> map = new HashMap<>();
        String regex = "<a.*?/a>";

        Pattern pt = Pattern.compile(regex);
        Matcher mt = pt.matcher(strs);

        while (mt.find()) {
            String s2 = ">[^<].*?[^>]</a>";
            String s3 = "href=\"(.*?)\"";
            Pattern pt2 = Pattern.compile(s2);
            Matcher mt2 = pt2.matcher(mt.group());
            Pattern pt3 = Pattern.compile(s3);
            Matcher mt3 = pt3.matcher(mt.group());
            while (mt2.find() && mt3.find()) {
                String title = mt2.group().replaceAll(">|</a>", "");
                String href = mt3.group().replaceAll("href=\"|\"", "");
                map.put(title, href);
            }
        }
        return map;
    }

    public static String getBuildKey() {
        return Build.MANUFACTURER + "_" + Build.MODEL + "_" + Build.PRODUCT;
    }

    public static String getReportDurationTimeString(int duration) {
        int hour, minute, second;
        String timeStr = "";

        hour = duration / 3600000;
        if (hour > 0) {
            timeStr = hour + "小时";
        }

        minute = (duration - (hour * 3600000)) / 60000;
        if ((minute < 10) && (hour > 0)) {
            timeStr = timeStr + "零" + minute + "分";
        } else {
            timeStr = timeStr + minute + "分";
        }
        second = (duration - (hour * 3600000) - (minute * 60000)) / 1000;
        if (second < 10) {
            timeStr = timeStr + "零" + second + "秒";
        } else {
            timeStr = timeStr + second + "秒";
        }
        return timeStr;
    }

    public static String getDurationTimeString(int duration) {
        int hour, minute, second;
        String timeStr = "";

        hour = duration / 3600000;
        if (hour > 0) {
            timeStr = hour + ":";
        }
        minute = (duration - (hour * 3600000)) / 60000;
        if (minute < 10) {
            timeStr = timeStr + "0" + minute + ":";
        } else {
            timeStr = timeStr + minute + ":";
        }
        second = (duration - (hour * 3600000) - (minute * 60000)) / 1000;
        if (second < 10) {
            timeStr = timeStr + "0" + second;
        } else {
            timeStr = timeStr + second;
        }

        return timeStr;
    }

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }

    public static String getFileSize(long total) {
        if (total > 1024 * 1024 * 1024) {
            return DF.format((float) total / (1024 * 1024 * 1024)) + "G";
        }
        if (total > 1024 * 1024) {
            return DF.format((float) total / (1024 * 1024)) + "M";
        }
        if (total > 1024) {
            return DF.format((float) total / (1024)) + "K";
        }
        return DF.format((float) total) + "B";
    }

    public static float EPSINON = 0.00001f;

    public static boolean floatIsZero(float x) {
        return x < EPSINON && x > -EPSINON;
    }

    public static void intentActivity(Context context, Class activity) {
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }

    public static void intentService(Context context, Class service) {
        Intent intent = new Intent(context, service);
        context.startService(intent);
    }

    public static int convertDpToPixel(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static RequestBody toRequestBody(String value) {
        MediaType mediaType = MediaType.parse("text/plain");
        return RequestBody.create(mediaType, value);
    }

    //去掉首尾空格
    public static String trimAllSpace(String str) {
        int startIndex, endIndex;

        if (str == null || str.length() == 0) {
            return str;
        }
        for (startIndex = 0; startIndex < str.length() && (str.charAt(startIndex) == ' ' || str.charAt(startIndex) == '　'); startIndex++)
            ;
        for (endIndex = str.length() - 1; endIndex >= 0 && (str.charAt(endIndex) == ' ' || str.charAt(endIndex) == '　'); endIndex--)
            ;
        if (startIndex >= str.length() || endIndex < 0) {
            return "";
        }
        return str.substring(startIndex, endIndex + 1);
    }

    //去掉首尾空格
    public static String trimAllSpace2(String str) {
        int startIndex, endIndex;

        if (str == null || str.length() == 0) {
            return str;
        }
        for (startIndex = 0; startIndex < str.length() && (str.charAt(startIndex) == '\n'); startIndex++)
            ;
        for (endIndex = str.length() - 1; endIndex >= 0 && (str.charAt(endIndex) == '\n'); endIndex--)
            ;
        if (startIndex >= str.length() || endIndex < 0) {
            return "";
        }
        return str.substring(startIndex, endIndex + 1);
    }

    public static BitmapDrawable getBackgroundBitmap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }

    /**
     * 判断对象是否为空
     *
     * @param object 对象实体
     * @return boolean
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return ((String) object).length() == 0;
        } else if (object instanceof List) {
            return ((List<?>) object).isEmpty();
        } else if (object instanceof Map) {
            return ((Map<?, ?>) object).isEmpty();
        } else if (object instanceof Collection) {
            return ((Collection<?>) object).isEmpty();
        } else if (object instanceof Object[]) {
            return ((Object[]) object).length == 0;
        }
        return false;
    }

    /**
     * JSON 反序列化
     *
     * @param ts  内容
     * @param tc  实体
     * @param <T> 实体
     * @return T 返回序列化成功实体
     */
    public static <T> List<T> getList(String ts, Class<T> tc) {
        if (isEmpty(ts)) {
            return null;
        }
        try {
            List<T> t = JSON.parseArray(ts, tc);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            // ig
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param ts  内容
     * @param tc  实体的class映射
     * @param <T> 实体
     * @return T 成功后返回的实体
     */
    public static <T> T getObject(String ts, Class<T> tc) {
        if (isEmpty(ts)) {
            return null;
        }
        try {
            T t = JSON.parseObject(ts, tc);
            return t;
        } catch (Exception e) {
            // ig
            e.printStackTrace();
        }
        return null;
    }

    public static String getMD5passwd(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            return pwd;
        }
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(pwd.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            // throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        return pwd;
    }

    /**
     * 获取屏幕的宽和高
     *
     * @param activity
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 将毫秒转换为时分秒
     */
    public static String formatMilliseconds(long millisecond) {
        long second = millisecond / 1000;
        if (second < 0) {
            return "--:--";
        }
        if (second == 0) {
            return "00:00";
        }
        if (second < 60 * 60) {
            int minute = (int) (second / 60);
            String minute_str = minute < 10 ? ("0" + minute) : ("" + minute);
            int surplus_second = (int) (second % 60);
            String surplus_second_str = surplus_second < 10 ? ("0" + surplus_second) : ("" + surplus_second);
            return minute_str + ":" + surplus_second_str;
        } else {
            int hour = (int) (second / 3600);
            String hour_str = hour < 10 ? ("0" + hour) : ("" + hour);
            int minute = (int) (second % 3600) / 60;
            String minute_str = minute < 10 ? ("0" + minute) : ("" + minute);
            int surplus_second = (int) ((second % 3600) % 60);
            String surplus_second_str = surplus_second < 10 ? ("0" + surplus_second) : ("" + surplus_second);
            return hour_str + ":" + minute_str + ":" + surplus_second_str;
        }
    }

    public static int spToPx(int sp) {
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public static DisplayMetrics getDisplayMetrics() {
        DisplayMetrics metrics = AppApplication
                .getInstance()
                .getResources()
                .getDisplayMetrics();
        return metrics;
    }

    public static int dpToPx(int dp) {
        DisplayMetrics metrics = getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static boolean isAccessibilityEnabled(Context context) {
        final int accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(),
                Settings.Secure.ACCESSIBILITY_ENABLED, 0);
        return accessibilityEnabled == 1;
    }


    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_BOOK_DATE = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_DATE_NORMAL = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_NORMAL_YMDHM = "yyyy-MM-dd HH:mm";
    public static final String FORMAT_FILE_DATE = "yyyy-MM-dd";

    //将时间转换成日期
    public static String dateConvert(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    private static final int HOUR_OF_DAY = 24;
    private static final int DAY_OF_YESTERDAY = 2;
    private static final int TIME_UNIT = 60;

    public static String formatTimeNormal(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat format = new SimpleDateFormat(FORMAT_DATE_NORMAL);
        return format.format(date);
    }

    public static String formatTimeNormalYMDHM(Date date) {
        if (date == null) {
            return "";
        }
        DateFormat format = new SimpleDateFormat(FORMAT_DATE_NORMAL_YMDHM);
        return format.format(date);
    }

    public static Date parseTimeNormalYMDHM(String dateStr) {
        if (ToolUtils.isEmpty(dateStr)) {
            return null;
        }
        DateFormat format = new SimpleDateFormat(FORMAT_DATE_NORMAL_YMDHM);
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    //将日期转换成昨天、今天、明天
    public static String dateConvert(String source, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = format.parse(source);
            long curTime = calendar.getTimeInMillis();
            calendar.setTime(date);
            //将MISC 转换成 sec
            long difSec = Math.abs((curTime - date.getTime()) / 1000);
            long difMin = difSec / 60;
            long difHour = difMin / 60;
            long difDate = difHour / 60;
            int oldHour = calendar.get(Calendar.HOUR);
            //如果没有时间
            if (oldHour == 0) {
                //比日期:昨天今天和明天
                if (difDate == 0) {
                    return "今天";
                } else if (difDate < DAY_OF_YESTERDAY) {
                    return "昨天";
                } else {
                    DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String value = convertFormat.format(date);
                    return value;
                }
            }

            if (difSec < TIME_UNIT) {
                return difSec + "秒前";
            } else if (difMin < TIME_UNIT) {
                return difMin + "分钟前";
            } else if (difHour < HOUR_OF_DAY) {
                return difHour + "小时前";
            } else if (difDate < DAY_OF_YESTERDAY) {
                return "昨天";
            } else {
                DateFormat convertFormat = new SimpleDateFormat("yyyy-MM-dd");
                String value = convertFormat.format(date);
                return value;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String dateConvertBookDate(String book_date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_BOOK_DATE);
        try {
            return new SimpleDateFormat(FORMAT_DATE_NORMAL).format(format.parse(book_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return book_date;
    }

    /**
     * 将文本中的半角字符，转换成全角字符
     *
     * @param input
     * @return
     */
    public static String halfToFull(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) //半角空格
            {
                c[i] = (char) 12288;
                continue;
            }
            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;

            if (c[i] > 32 && c[i] < 127)    //其他符号都转换为全角
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    /**
     * 获取导航栏的高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        Resources resources = AppApplication.getInstance().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 获取虚拟按键的高度
     *
     * @return
     */
    public static int getNavigationBarHeight() {
        int navigationBarHeight = 0;
        Resources rs = AppApplication.getInstance().getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && hasNavigationBar()) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 是否存在虚拟按键
     *
     * @return
     */
    private static boolean hasNavigationBar() {
        boolean hasNavigationBar = false;
        Resources rs = AppApplication.getInstance().getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try { //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 跳转到文字转语音设置界面
     */
    public static void intentTTSActivity(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("com.android.settings.TTS_SETTINGS");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        try {
            String[] proj = {MediaStore.Audio.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            String filePath = null;
            if (cursor != null && cursor.getCount() > 0) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                cursor.moveToFirst();
                filePath =  cursor.getString(column_index);
            }
            if (cursor != null) {
                cursor.close();
            }
            return filePath;
        } catch (IllegalArgumentException e) {
            //解决部分手机中：column '_data' does not exist
        }
        return null;
    }

    /**
     * 将字符串去除所有标点并转换成小写
     */
    public static String formatStrToLowerCase(String str) {
        if (str == null) {
            return null;
        }
        String titleSingle = str.replaceAll("\\p{P}","");//去除所有标点符号
        titleSingle = titleSingle == null ? null : titleSingle.toLowerCase();//转换成小写
        return titleSingle;
    }

    public static String getFormatValueNum(float d) {
        return String.format("%.1f", d);
    }
}
