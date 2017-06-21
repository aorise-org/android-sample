package cn.aorise.common.core.module.network;

import rx.Subscription;

/**
 * 删除网络请求接口
 * Created by pc on 2017/3/14.
 */
public interface IRxAction<T> {
    void add(T tag, Subscription subscription);

    void remove(T tag);

    void cancel(T tag);

    void cancelAll();
}
