package cn.aorise.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;

import cn.aorise.common.core.interfaces.IAppCycle;
import cn.aorise.common.core.manager.ActivityManager;
import cn.aorise.common.core.manager.AppManager;
import cn.aorise.common.core.module.glide.GlideManager;
import cn.aorise.common.core.module.network.RxAPIManager;
import cn.aorise.common.core.module.statistics.Statistics;
import cn.aorise.common.core.utils.assist.DebugUtil;

/**
 * 公共Application类
 * Created tangjy tangjy on 2017/3/8.
 */
public class BaseApplication extends Application implements IAppCycle {
    private static final String TAG = BaseApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        create(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 初始化APP数据
     * 初始化DEBUG模式、LOG开关等 <br>
     * 初始化各个注册的plugin插件APP
     *
     * @param context 上下文
     */
    @Override
    public void create(Application context) {
        Log.i(TAG, "create");
        DebugUtil.syncIsDebug(context);
        Statistics.getInstance().init(this);
        // AppManager.getInstance().add(context);
        // AoriseLog.init(BuildConfig.LOG_DEBUG, BuildConfig.APPLICATION_ID);
        AppManager.getInstance().createAll(this);
    }

    /**
     * 销毁APP数据
     * 释放图片缓存、释放页面管理、释放网络资源等 <br>
     * 释放各个注册的plugin插件APP
     *
     * @param context       上下文
     * @param isKillProcess 为true强制退出APP
     */
    @Override
    public void destroy(Application context, boolean isKillProcess) {
        Log.i(TAG, "destroy");
        MobclickAgent.onKillProcess(this);
        GlideManager.getInstance().shutDown(context);
        ActivityManager.getInstance().appExit(context);
        RxAPIManager.getInstance().cancelAll();

        AppManager.getInstance().destroyAll(context, isKillProcess);
        if (isKillProcess) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
}
