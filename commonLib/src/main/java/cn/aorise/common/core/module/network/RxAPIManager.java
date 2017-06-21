package cn.aorise.common.core.module.network;

import java.util.HashMap;
import java.util.Set;

import rx.Subscription;

/**
 * 删除网络请求管理类
 * Created by pc on 2017/3/14.
 */
public class RxAPIManager implements IRxAction<Object> {
    private static RxAPIManager sInstance = null;
    private HashMap<Object, Subscription> maps;

    public static RxAPIManager getInstance() {
        if (sInstance == null) {
            synchronized (RxAPIManager.class) {
                if (sInstance == null) {
                    sInstance = new RxAPIManager();
                }
            }
        }
        return sInstance;
    }

    private RxAPIManager() {
        maps = new HashMap<>();
    }

    /**
     * 记录网络请求
     *
     * @param tag          网络请求标识
     * @param subscription 网络请求
     */
    @Override
    public void add(Object tag, Subscription subscription) {
        maps.put(tag, subscription);
    }

    /**
     * 删除网络请求，但是不会取消网络请求
     *
     * @param tag 网络请求标识
     */
    @Override
    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            maps.remove(tag);
        }
    }

    /**
     * 删除全部网络请求，但是不会取消网络请求
     */
    public void removeAll() {
        if (!maps.isEmpty()) {
            maps.clear();
        }
    }

    /**
     * 取消网络请求
     *
     * @param tag 网络请求标识
     */
    @Override
    public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isUnsubscribed()) {
            maps.get(tag).unsubscribe();
            maps.remove(tag);
        }
    }

    /**
     * 取消全部网络请求
     */
    @Override
    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}
