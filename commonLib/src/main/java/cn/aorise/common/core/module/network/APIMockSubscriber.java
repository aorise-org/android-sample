package cn.aorise.common.core.module.network;

import android.support.annotation.Nullable;

import java.lang.reflect.Type;

import cn.aorise.common.core.exception.BaseException;
import cn.aorise.common.core.ui.base.BaseActivity;
import cn.aorise.common.core.utils.assist.AoriseLog;
import cn.aorise.common.core.utils.assist.GsonUtil;
import cn.aorise.common.core.utils.file.ResourceUtil;
import cn.aorise.common.core.utils.handler.HandlerUtil;

/**
 * 模拟网络请求
 * 执行顺序 onStart 到 onMock 到 onCompleted
 *
 * @param <T>
 */
public class APIMockSubscriber<T> extends APISubscriber<T> {
    /**
     * 模拟网络请求延迟
     */
    public static final int DELAY_MILLIS = 2000;
    private static final String TAG = APIMockSubscriber.class.getSimpleName();
    private String mMockPath;
    private Class<T> mClass;
    private Type mType;

    /**
     * 模拟网络请求
     *
     * @param activity 上下文
     * @param mockPath 测试路径
     * @param aClass   返回解析对象类名
     * @param callback 回调接口
     */
    public APIMockSubscriber(BaseActivity activity, String mockPath, Class<T> aClass, APICallback<T> callback) {
        super(activity, callback);
        mMockPath = mockPath;
        mClass = aClass;
    }

    /**
     * 模拟网络请求
     *
     * @param activity 上下文
     * @param mockPath 测试路径
     * @param typeOfT  返回解析对象java.lang.reflect.Type
     * @param callback 回调接口
     */
    public APIMockSubscriber(BaseActivity activity, String mockPath, Type typeOfT, APICallback<T> callback) {
        super(activity, callback);
        mMockPath = mockPath;
        mType = typeOfT;
    }

    @Override
    public void onStart() {
        super.onStart();
        AoriseLog.i(TAG, "onStart");

        // 测试模式
        if (null != mMockPath) {
            AoriseLog.i(TAG, "进入mock模式");
            HandlerUtil.runOnUiThreadDelay(new Runnable() {
                @Override
                public void run() {
                    delay();
                }
            }, DELAY_MILLIS);
        }
    }

    private void delay() {
        String json = getMockStream();
        if (null == json) {
            this.onError(new BaseException("Mock文件不存在"));
        } else {
            T t = null;

            if (null != mType) {
                t = GsonUtil.fromJson(json, mType);
            } else if (null != mClass) {
                t = GsonUtil.fromJson(json, mClass);
            }

            if (null == t) {
                this.onError(new BaseException("Mock解析异常"));
            } else {
                AoriseLog.i(TAG, "onMock: " + t.toString());
                mCallback.onMock(t);
            }
        }
        this.onCompleted();
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        AoriseLog.i(TAG, "onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        // super.onError(e);
        if (e instanceof BaseException) {
            AoriseLog.e(TAG, e.toString());
        } else {
            AoriseLog.e(TAG, "onError");
        }
    }

    @Override
    public void onNext(T t) {
        // super.onNext(t);
        AoriseLog.i(TAG, "onNext");
    }

    @Nullable
    private String getMockStream() {
        StringBuilder sb = new StringBuilder();
        sb.append("mock/").append(mMockPath).append(".json");
        return ResourceUtil.geFileFromAssets(mWeakReference.get(), sb.toString());
    }
}
