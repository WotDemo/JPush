package ysn.com.jpushdemo.app;

import android.app.Application;

import com.lazy.library.logging.Builder;
import com.lazy.library.logging.Logcat;

import cn.jpush.android.api.JPushInterface;
import ysn.com.jpushdemo.BuildConfig;

/**
 * @Author yangsanning
 * @ClassName MyApplication
 * @Description 一句话概括作用
 * @Date 2019/7/25
 * @History 2019/7/25 author: description:
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initLogCat();
        initJPush();
    }

    private void initLogCat() {
        Builder builder = Logcat.newBuilder();
        builder.topLevelTag("test");
        if (BuildConfig.DEBUG) {
            builder.logCatLogLevel(Logcat.SHOW_ALL_LOG);
        } else {
            builder.logCatLogLevel(Logcat.SHOW_INFO_LOG | Logcat.SHOW_WARN_LOG | Logcat.SHOW_ERROR_LOG);
        }
        Logcat.initialize(this, builder.build());
    }

    /**
     * 初始化极光推送
     */
    private void initJPush() {
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(!BuildConfig.DEBUG);
        // 初始化 JPush
        JPushInterface.init(this);
        JPushInterface.setDebugMode(!BuildConfig.DEBUG);
    }
}
