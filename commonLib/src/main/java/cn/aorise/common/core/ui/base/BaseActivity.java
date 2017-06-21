package cn.aorise.common.core.ui.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.io.File;

import cn.aorise.common.R;
import cn.aorise.common.core.config.AoriseConstant;
import cn.aorise.common.core.manager.ActivityManager;
import cn.aorise.common.core.utils.assist.AoriseLog;
import cn.aorise.common.core.utils.handler.CrashHandlerUtil;
import cn.aorise.common.core.utils.handler.HandlerUtil;

/**
 * 公共Activity基类
 * Created by pc on 2017/3/1.
 */
public abstract class BaseActivity extends AppCompatActivity implements CrashHandlerUtil.CrashListener {
    private static final String TAG = BaseActivity.class.getSimpleName();
    private Toolbar mToolBar = null;
    private ProgressDialog mLoadingDialog = null;
    private Toast mToast = null;

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化布局
     */
    protected abstract void initView();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AoriseLog.i(TAG, getLocalClassName() + " - onCreate");
        CrashHandlerUtil.getInstance().init(this, this,
                getPackageName() + File.separator + AoriseConstant.Folder.LOG_PATH);
        ActivityManager.getInstance().addActivity(this);
        initData();
        initView();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        destroyToast();
        dismissLoadingDialog();
        ActivityManager.getInstance().finishActivity(this);
        HandlerUtil.HANDLER.removeCallbacksAndMessages(null);
        AoriseLog.i(TAG, getLocalClassName() + " - onDestroy");
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolbar();
    }

    /**
     * 提示语
     *
     * @param text 提示内容
     */
    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    /**
     * 提示语
     *
     * @param resId 提示内容ID
     */
    public void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    private void destroyToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }


    /**
     * 显示加载框
     */
    public void showLoadingDialog() {
        AoriseLog.i(TAG, "showLoadingDialog");
        if (null == mLoadingDialog) {
            mLoadingDialog = new ProgressDialog(this);
            mLoadingDialog.setCancelable(true);
            mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    dismissLoadingDialog();
                }
            });
            mLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mLoadingDialog.setMessage(getString(R.string.aorise_loading_tips));
            mLoadingDialog.show();
        }
    }

    /**
     * 取消加载框
     */
    public void dismissLoadingDialog() {
        AoriseLog.i(TAG, "dismissLoadingDialog");
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }

    /**
     * 获取标题
     *
     * @return toolbar
     */
    protected Toolbar getToolBar() {
        return mToolBar;
    }

    /**
     * 设置标题名字
     *
     * @param title 标题名字内容
     */
    protected void setToolBarTitle(CharSequence title) {
        TextView txtTitle = (TextView) findViewById(R.id.toolbar_title);
        if (null != txtTitle && !TextUtils.isEmpty(title)) {
            txtTitle.setText(title);
        }
    }

    private void initToolbar() {
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        if (null == mToolBar) {
            return;
        }

        mToolBar.setTitle("");
        // mToolBar.setBackgroundResource(R.color.base_blue);
        mToolBar.setNavigationIcon(R.drawable.aorise_ic_navigation);
        setSupportActionBar(mToolBar);

        TextView title = (TextView) findViewById(R.id.toolbar_title);
        title.setText(getTitle());
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (null == getSupportActionBar()) {
            return;
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    /**
     * 捕获APP异常
     *
     * @param file 异常写入的文件
     */
    @Override
    public void uploadExceptionToServer(File file) {

    }


    /**
     * APP异常后的动作或者提示
     */
    @Override
    public void crashAction() {
        showToast(R.string.aorise_label_crash_msg);
    }

    /**
     * 页面跳转
     *
     * @param pClass Activity类名
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 页面跳转
     *
     * @param pClass  Activity类名
     * @param pBundle 数据
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    /**
     * 打开WebView
     *
     * @param uri URL地址
     */
    public void openWebActivity(String uri) {
        Bundle bundle = new Bundle();
        bundle.putString(AoriseConstant.TransportKey.BUNDLE_URL, uri);
        openActivity(BaseWebActivity.class, bundle);
    }
}
