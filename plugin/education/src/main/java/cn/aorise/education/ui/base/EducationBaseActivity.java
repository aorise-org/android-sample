package cn.aorise.education.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cn.aorise.common.core.ui.base.BaseActivity;
import cn.aorise.common.core.utils.assist.AoriseLog;


/**
 * Created by Administrator on 2016/3/14 0014.
 */
public abstract class EducationBaseActivity extends BaseActivity {
    private static final String TAG = EducationBaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void showLoadingDialog() {
        super.showLoadingDialog();
        AoriseLog.i(TAG, "showLoadingDialog");
    }

    @Override
    public void dismissLoadingDialog() {
        super.dismissLoadingDialog();
        AoriseLog.i(TAG, "dismissLoadingDialog");
    }
}
