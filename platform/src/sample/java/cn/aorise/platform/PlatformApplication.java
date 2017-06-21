package cn.aorise.platform;

import android.app.Application;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;

import cn.aorise.common.BaseApplication;
import cn.aorise.common.core.manager.AppManager;
import cn.aorise.common.core.utils.assist.AoriseUtil;
import cn.aorise.sample.BuildConfig;
import cn.aorise.sample.SampleApplication;

public class PlatformApplication extends BaseApplication {
    private static final String TAG = PlatformApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        AppManager.getInstance().add(new SampleApplication());
        super.onCreate();
        Log.i(TAG, "onCreate");

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        if (BuildConfig.DEBUG) {
            AoriseUtil.enabledStrictMode();
        }
        LeakCanary.install(this);
    }

    @Override
    public void create(Application context) {
        super.create(context);
        Log.i(TAG, "init");
    }

    @Override
    public void destroy(Application context, boolean isKillProcess) {
        super.destroy(context, isKillProcess);
        Log.i(TAG, "destroy");
    }
}
