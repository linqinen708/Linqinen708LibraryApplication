package com.linqinen.library.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {
    private static Toast mToast = null;
    private static Context mContext;
    private static boolean isInit;

    /**最好在Application中初始化*/
    public static void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }
        isInit = true;
        mContext = context.getApplicationContext();
    }

    private static void isInitToast() {
        if (!isInit) {
            throw new IllegalArgumentException("You have not init toast");
        }
    }

    public static void showToast(String msg) {
        showToast(msg,Toast.LENGTH_SHORT);
    }

    public static void showToast(String msg, int duration) {
        isInitToast();
        if (mToast == null) {
            mToast = Toast.makeText(mContext, msg, duration);
        } else {
            if (mToast.getDuration() != duration) {
                mToast.setDuration(duration);
            }
            mToast.setText(msg);
        }
        mToast.show();
    }

    public static void showToast(int resId){
        showToast(resId,Toast.LENGTH_SHORT);

    }

    public static void showToast(int resId, int duration) {
        isInitToast();
        if (mToast == null) {
            mToast = Toast.makeText(mContext, resId, duration);
        } else {
            if (mToast.getDuration() != duration) {
                mToast.setDuration(duration);
            }
            mToast.setText(resId);
        }
        mToast.show();
    }





    // 主要针对需要在某个时候，取消提示
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

}
