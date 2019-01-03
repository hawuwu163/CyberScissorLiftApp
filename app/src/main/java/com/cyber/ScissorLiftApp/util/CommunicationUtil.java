package com.cyber.ScissorLiftApp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;


/**
 * creat_user: Heshuai
 * creat_date: 2018/12/28 20:38
 * description: 网络连接相关的工具类,静态方法不会有线程安全的前提是静态方法没有引用静态变量,
 * 因为静态方法共享静态变量的内存位置
 **/

public class CommunicationUtil {


    /**
     * 判断是否有网络连接(可以传输数据)
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空
            return null != networkInfo && networkInfo.isAvailable();
        }
        return false;
    }


    /**
     * 判断WIFI网络是否可用
     */

    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            // 获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // 获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为WIFI
            return null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取NetworkInfo对象
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            return null != networkInfo && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE &&  networkInfo.isAvailable();
        }
        return false;
    }


    /**
     * 获取当前的网络状态
     * @param context 全局context
     * @return String
     * 没有网络-NONE
     * WIFI网络-WIFI
     * 4G网络-4G
     * 3G网络-3G
     * 2G网络-2G
     * 未知-Unknown
     */
    public static String getAPNType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo == null) {      //无网络
            return "NONE";
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {       //wifi
            return "WIFI";
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_LTE
                    && !telephonyManager.isNetworkRoaming()) {
                return "4G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_A
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSUPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSPA
                    || nSubType == TelephonyManager.NETWORK_TYPE_EVDO_B
                    || nSubType == TelephonyManager.NETWORK_TYPE_EHRPD
                    || nSubType == TelephonyManager.NETWORK_TYPE_HSPAP
                    && !telephonyManager.isNetworkRoaming()) {
                return "3G";
            } else if (nSubType == TelephonyManager.NETWORK_TYPE_GPRS
                    || nSubType == TelephonyManager.NETWORK_TYPE_EDGE
                    || nSubType == TelephonyManager.NETWORK_TYPE_CDMA
                    || nSubType == TelephonyManager.NETWORK_TYPE_1xRTT
                    || nSubType == TelephonyManager.NETWORK_TYPE_IDEN
                    && !telephonyManager.isNetworkRoaming()) {
                return "2G";
            } else {
                return "Unknown";
            }
        }
        return "Unknown";
    }



}
