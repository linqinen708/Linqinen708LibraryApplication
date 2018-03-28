package com.linqinen.library.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

/**
 * Created by 林 on 2017/5/18.
 * 用来判断网络的状态
 *
 * 使用案例：@initWifi
 *
 */


public class MyNetWorkUtil {

    private void initWifi(){
//        if (MyNetWorkUtil.getAPNType(this) == MyNetWorkUtil.CURRENT_NETWORK_STATES_NO) {
//            mIvWifi.setVisibility(View.GONE);
//            new MyDialog(this)
//                    .setTitle("网络设置提示")
//                    .setMessage("当前网络不可用,是否进行设置?")
//                    .setPositiveButton("设置网络", new MyDialog.OnMyDialogButtonClickListener() {
//                        @Override
//                        public void onClick() {
//                            startActivity(new Intent().setAction("android.net.wifi.PICK_WIFI_NETWORK"));
//                        }
//                    }).setNegativeButton("退出", new MyDialog.OnMyDialogButtonClickListener() {
//                @Override
//                public void onClick() {
//                    finish();
//                }
//            }).show();
//        } else if (MyNetWorkUtil.getAPNType(this) != MyNetWorkUtil.CURRENT_NETWORK_STATES_WIFI) {
//            mIvWifi.setVisibility(View.GONE);
//            new MyDialog(this)
//                    .setTitle("网络设置提示")
//                    .setMessage("当前不是WiFi网路，是否继续使用慈硕养老？")
//                    .setPositiveButton(null).setNegativeButton("退出", new MyDialog.OnMyDialogButtonClickListener() {
//                @Override
//                public void onClick() {
//                    finish();
//                }
//            }).show();
//        }
    }

    //获取当前的网络状态
    public static final int CURRENT_NETWORK_STATES_NO = -1;//-1：没有网络
    public static final int CURRENT_NETWORK_STATES_WIFI = 1;//1：WIFI网络
    private static final int CURRENT_NETWORK_STATES_WAP = 2;//2：wap网络
    private static final int CURRENT_NETWORK_STATES_NET = 3;//3：net网络

    public static int getAPNType(Context context) {
        //设置默认网路类型
        int netType = CURRENT_NETWORK_STATES_NO;
        //获取当前的网络管理器
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //获取网络信息
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        //得到网络类型
        int nType = networkInfo.getType();
        LogT.i("网络类型:" + nType);
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = CURRENT_NETWORK_STATES_WIFI;
        }
        else if (nType == ConnectivityManager.TYPE_MOBILE) {
            netType = networkInfo.getExtraInfo().toLowerCase().equals("cmnet") ? CURRENT_NETWORK_STATES_NET : CURRENT_NETWORK_STATES_WAP;
        }
        return netType;
    }

    /**
     * 判断WiFi网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConn(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断数据流量是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileConn(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断是否有网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConn(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    //判断当前的进行或者服务是否存在（是否运行）
    public static boolean isServiceRunning(Context mContext, String className) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        //30 就是一个数字 可以用常量Integer.MAX_VALUE 代替

        if (!(serviceList.size() > 0)) {
            return false;
        }
        boolean isRunning = false;
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
