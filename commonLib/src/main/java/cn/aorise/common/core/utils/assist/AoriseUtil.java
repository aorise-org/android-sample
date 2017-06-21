package cn.aorise.common.core.utils.assist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;

import cn.aorise.common.R;
import cn.aorise.common.core.config.AoriseConstant;
import cn.aorise.common.core.module.glide.GlideManager;
import cn.aorise.common.core.module.network.APICallback;
import cn.aorise.common.core.module.network.APIMockSubscriber;
import cn.aorise.common.core.module.network.APISubscriber;
import cn.aorise.common.core.ui.base.BaseActivity;
import cn.aorise.common.core.utils.file.FileUtil;
import cn.aorise.common.core.utils.file.SdCardUtil;
import cn.aorise.common.core.utils.system.AppUtil;
import rx.Subscriber;

/**
 * 公共Utils接口
 * Created by tangjy on 2015/11/6 0006.
 */
public class AoriseUtil {
    private static final String TAG = AoriseUtil.class.getSimpleName();

    /**
     * 开启APP严格模式
     */
    public static void enabledStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog() 
                .penaltyDeath()
                .build());
    }

    /**
     * 获取手机SD卡
     *
     * @return 手机SD卡路径
     */
    public static String getSdCard() {
        // String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
        String sdcard = SdCardUtil.getNormalSDCardPath();
        AoriseLog.i(TAG, "sdcard = " + sdcard);
        return sdcard;
    }

    /**
     * 获取APP在手机T卡里面创建的根目录地址
     *
     * @param root APP包名
     * @return APP手机T卡根目录地址
     */
    public static String getRootPath(String root) {
        String absolutePath = getSdCard() + File.separator + root;
        AoriseLog.i(TAG, "getRootPath = " + absolutePath);
        return absolutePath;
    }

    /**
     * 获取APP在手机T卡里面创建的下载、LOG等文件夹地址
     *
     * @param root    APP包名
     * @param relPath 下载、LOG等文件夹名字
     * @return 下载、LOG等文件夹在手机卡里面的绝对路径地址
     */
    public static String getSdPath(String root, String relPath) {
        String absolutePath = getRootPath(root) + File.separator + relPath;
        AoriseLog.i(TAG, "getSdPath = " + absolutePath);
        return absolutePath;
    }

    private static void makeFolders(String root) {
        if (SdCardUtil.isSdCardAvailable()) {
            try {
                FileUtil.forceMkdir(new File(getRootPath(root)));
                FileUtil.forceMkdir(new File(getSdPath(root, AoriseConstant.Folder.LOG_PATH)));
                FileUtil.forceMkdir(new File(getSdPath(root, AoriseConstant.Folder.CACHE_PATH)));
                FileUtil.forceMkdir(new File(getSdPath(root, AoriseConstant.Folder.DOWNLOAD_PATH)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            AoriseLog.i(TAG, "T卡无效");
        }
    }

    /**
     * 初始化APP环境
     * 创建文件夹<br>
     *
     * @param root 包名
     */
    public static void initAppEnvironment(String root) {
        makeFolders(root);
    }


    /**
     * 获取应用渠道ID
     *
     * @param context 上下文
     * @return
     */
    public static String getChannel(Context context) {
        String channel = AppUtil.getAppSource(context, AoriseConstant.CHANNEL);
        AoriseLog.i(TAG, "channel = " + channel);
        return channel;
    }

    /**
     * 加载图片
     *
     * @param context     Activity, Fragment等上下文
     * @param view        ImageView对象
     * @param uri         图片地址
     * @param <T>         请求地址类型（URL FILE ASSETS）
     * @param placeholder 占位图
     * @param error       网络错误的图片
     */
    public static <T> void loadImage(final Context context, ImageView view, T uri,
                                     @DrawableRes int placeholder, @DrawableRes int error) {
        GlideManager.getInstance().load(context, view, uri, placeholder, error);
    }

    /**
     * 加载图片
     *
     * @param context Activity, Fragment等上下文
     * @param view    ImageView对象
     * @param uri     图片地址
     * @param <T>     请求地址类型（URL FILE ASSETS）
     */
    public static <T> void loadImage(final Context context, ImageView view, T uri) {
        GlideManager.getInstance().load(context, view, uri);
    }

    /**
     * 标记Intent
     *
     * @param context 上下文
     * @param bundle  数据
     * @param cls     跳转的页面
     * @return Intent
     */
    public static Intent getMaskIntent(Context context, Bundle bundle, Class<?> cls) {
        Intent intent = new Intent();
        // intent.putExtra(AoriseConstant.TransportKey.INTENT_KEY, bundle);
        intent.putExtras(bundle);
        intent.setClass(context.getApplicationContext(), cls);
        return intent;
    }

    /**
     * 标记Intent
     *
     * @param context 上下文
     * @param bundle  数据
     * @return Intent
     */
    public static Intent getMaskIntent(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        return intent;
    }

    /**
     * 获取序列化的数据
     *
     * @param intent intent对象
     * @return 序列化数据
     */
    public static Serializable getMaskSerializable(Intent intent) {
        if (null != intent) {
            // Bundle bundle = intent.getBundleExtra(AoriseConstant.TransportKey.INTENT_KEY);
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                return bundle.getSerializable(AoriseConstant.TransportKey.BUNDLE_KEY);
            }
        }
        return null;
    }

    /**
     * 创建绑定序列化数据的Bundle对象
     *
     * @param value 序列化对象
     * @return Bundle
     */
    public static Bundle getMaskBundle(Serializable value) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AoriseConstant.TransportKey.BUNDLE_KEY, value);
        return bundle;
    }

    /**
     * 创建联网请求mock模式
     *
     * @param mock     是否mock模式
     * @param activity 上 文
     * @param mockPath 测试路径
     * @param aClass   返回解析对象类名
     * @param callback 回调接口
     * @param <T>      类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Subscriber<T> mockSubscriber(boolean mock, BaseActivity activity, String mockPath, Class<T> aClass, APICallback<T> callback) {
        Subscriber subscriber;
        if (mock && !TextUtils.isEmpty(mockPath)) {
            subscriber = new APIMockSubscriber(activity, mockPath, aClass, callback);
        } else {
            subscriber = new APISubscriber(activity, callback);
        }
        return subscriber;
    }

    /**
     * 创建联网请求mock模式
     *
     * @param mock     是否mock模式
     * @param activity 上下文
     * @param mockPath 测试路径
     * @param typeOfT  返回解析对象java.lang.reflect.Type
     * @param callback 回调接口
     * @param <T>      类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Subscriber<T> mockSubscriber(boolean mock, BaseActivity activity, String mockPath, Type typeOfT, APICallback<T> callback) {
        Subscriber subscriber;
        if (mock && !TextUtils.isEmpty(mockPath)) {
            subscriber = new APIMockSubscriber(activity, mockPath, typeOfT, callback);
        } else {
            subscriber = new APISubscriber(activity, callback);
        }
        return subscriber;
    }

    /**
     * 通过布局文件获取view
     *
     * @param context  上下文
     * @param resource 布局文件ID
     * @param root     根布局view
     * @return View对象
     */

    public static View inflateLayout(Activity context, @LayoutRes int resource, @Nullable ViewGroup root) {
        // return LayoutInflater.from(context).inflate(R.layout.aorise_include_empty_tips, null);
        return LayoutInflater.from(context).inflate(resource, root, false);
    }

    /**
     * 获取公共列表为空的view
     *
     * @param context 上下文
     * @param root    根布局view
     * @return View对象
     */
    public static View inflateEmptyView(Activity context, @Nullable ViewGroup root) {
        return inflateLayout(context, R.layout.aorise_include_empty_tips, root);
    }

    /**
     * 获取公共列表底部加载更多的view
     *
     * @param context 上下文
     * @param root    根布局view
     * @return View对象
     */
    public static View inflateFooterView(Activity context, @Nullable ViewGroup root) {
        return inflateLayout(context, R.layout.aorise_include_load_more, root);
    }
}
