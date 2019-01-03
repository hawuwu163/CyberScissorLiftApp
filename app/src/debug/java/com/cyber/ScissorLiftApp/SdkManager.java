package com.cyber.ScissorLiftApp;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author Heshuai
 * @version V1.0
 * @Package com.meiji.toutiao
 * @date 2018/12/28 16:53
 * @Description: 该文件在debug模块的同路径下.用于给stetho/okhttp/leakcanary调试使用
 */
public class SdkManager {
    /**
     * Stetho初始化
     * @param context 全局Application
     */
    public static void initStetho(Context context) {
        Stetho.initializeWithDefaults(context);
    }

    /**
     * OkHttp 消息拦截器
     * @param builder
     * @return 加入拦截器后的builder
     */
    public static OkHttpClient.Builder initInterceptor(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);
        return builder;
    }

    /**
     * LeakCanary的装载
     * @param app 全局Application
     */
    public static void initLeakCanary(Application app) {
        if (LeakCanary.isInAnalyzerProcess(app)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(app);
    }
}
