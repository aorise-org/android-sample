package cn.aorise.common.core.ui.base;

import android.support.v4.app.Fragment;

/**
 * 公共Fragment基类
 * Created by pc on 2017/3/1.
 */
public abstract class BaseFragment extends Fragment {
    @Override
    public void onDestroy() {
        super.onDestroy();
//        BaseApplication.getRefWatcher(getActivity()).watch(this);
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        BaseApplication.getRefWatcher(getActivity()).watch(this);
//    }

    /**
     * 获取公共基类Activity
     *
     * @return BaseActivity对象
     */
    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
