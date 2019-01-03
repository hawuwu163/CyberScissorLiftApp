package com.cyber.ScissorLiftApp.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.cyber.ScissorLiftApp.InitApp;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.cyber.ScissorLiftApp.util
 * @date 2018/12/28 16:49
 * @Description: 给App的一些主题/字体/颜色等参数的设置
 */
public class SettingUtil {
    public static SettingUtil getInstance() {
        return SettingsUtilInstance.instance;
    }
    private SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(InitApp.AppContext);

    private static final class SettingsUtilInstance {
        private static final SettingUtil instance = new SettingUtil();
    }
    /**
     * 获取字体大小
     */
    public int getTextSize() {
        return setting.getInt("textsize", 16);
    }

    /**
     * 设置字体大小
     */
    public void setTextSize(int textSize) {
        setting.edit().putInt("textsize", textSize).apply();
    }

    /**
     * 查看是否第一次使用
     * @return 第一次则为true
     */
    public boolean getIsFirstTime() {
        return setting.getBoolean("first_time", true);
    }

    /**
     * 设置第一次使用
     * @param flag
     */
    public void setIsFirstTime(boolean flag) {
        setting.edit().putBoolean("first_time", flag).apply();
    }

    /**
     * 获取是否开启夜间模式
     */
    public boolean getIsNightMode() {
        return setting.getBoolean("switch_nightMode", false);
    }

    /**
     * 设置夜间模式
     */
    public void setIsNightMode(boolean flag) {
        setting.edit().putBoolean("switch_nightMode", flag).apply();
    }
}
