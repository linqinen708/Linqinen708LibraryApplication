package com.linqinen.library.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;



/**
 * Created by Ian on 2020/02/05.
 */
public abstract class BaseActivityIan extends AppCompatActivity {

    /**
     * 6.0以上额外的读取写入储存的权限
     */
    public static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1;
    public static final int REQUEST_CAMERA_PERMISSION = 2;

    protected Context mContext;

    private static Toast mToast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    public boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 检查权限结果是否拥有指定的所有权限
     */
    protected boolean checkGrantResultsAllGranted(int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    protected void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setMessage("未获得权限，无法使用视频")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", null)
                .show();

    }

    @SuppressLint("ShowToast")
    protected void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
                } else {
                    if (mToast.getDuration() != Toast.LENGTH_SHORT) {
                        mToast.setDuration(Toast.LENGTH_SHORT);
                    }
                    mToast.setText(message);
                }
                mToast.show();
            }
        });
    }

    @SuppressLint("ShowToast")
    protected void showToastLong(final String message) {
        runOnUiThread((new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
                } else {
                    if (mToast.getDuration() != Toast.LENGTH_LONG) {
                        mToast.setDuration(Toast.LENGTH_LONG);
                    }
                    mToast.setText(message);
                }

                mToast.show();
            }
        }));
    }
}
