package cn.aorise.sample;

import android.app.Application;
import android.util.Log;

import cn.aorise.common.core.interfaces.IAppCycle;
import cn.aorise.common.core.utils.assist.DebugUtil;
import cn.aorise.sample.db.DbHelper;

public class SampleApplication implements IAppCycle {
    private static final String TAG = SampleApplication.class.getSimpleName();

    @Override
    public void create(Application context) {
        Log.i(TAG, "init");
        // 设置联网请求是否mock模式
        DebugUtil.setDebug(false);
        DbHelper.getInstance().init(context);

    }

    @Override
    public void destroy(Application context, boolean isKillProcess) {
        DbHelper.getInstance().close();
    }
}
