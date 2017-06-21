package cn.aorise.common.core.interfaces;

import android.app.Application;

/**
 * 插件APP生命周期管理
 * Created by tangjy on 2017/3/21.
 */
public interface IAppCycle {
    /**
     * 插件APP初始化
     *
     * @param context 上下文
     */
    void create(Application context);

    /**
     * 插件APP释放
     *
     * @param context       上下文
     * @param isKillProcess 为true强制释放插件
     */
    void destroy(Application context, boolean isKillProcess);
}
