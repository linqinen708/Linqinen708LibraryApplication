package com.linqinen.library.widget;

import android.app.Activity;
import android.os.Handler;

import com.linqinen.library.widget.flowerdialog.FlowerDialog;

/**
 * Created by 林 on 2017/4/11 0011.
 */

public class MyFlowerDialog {

    private Activity mActivity;

    private FlowerDialog mFlowerDialog;

    public FlowerDialog getmFlowerDialog() {
        return mFlowerDialog;
    }


    private static Handler mHandler = new Handler();

    public MyFlowerDialog(Activity activity) {
        if (activity == null) {
            return;
        }
        this.mActivity = activity;
        mFlowerDialog = new FlowerDialog.Builder(mActivity)
                .text("加载中")
                .build();
        mFlowerDialog.setCancelable(false);
    }

    private static final int DURATION = 10000;

    /**
     * 自定义dismiss 菊花dialog，如果无法触发人为dismiss代码，则在指定时间后消失，并给出提示
     */
    public void showDialogDurationTime() {
        try {
            if (mActivity != null && !mActivity.isFinishing() && !mFlowerDialog.isShowing()) {
                mFlowerDialog.show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mActivity != null && !mActivity.isFinishing() && mFlowerDialog.isShowing()) {
                            mFlowerDialog.dismiss();
    //                        Toast.makeText(mContext, "数据加载失败，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, DURATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismiss() {
        if (mActivity != null && !mActivity.isFinishing() && mFlowerDialog != null && mFlowerDialog.isShowing()) {
            mFlowerDialog.dismiss();
        }
    }
}
