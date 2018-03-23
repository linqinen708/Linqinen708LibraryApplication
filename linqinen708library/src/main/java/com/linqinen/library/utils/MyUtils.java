package com.linqinen.library.utils;

import android.widget.ImageView;

import com.linqinen.library.R;

import java.lang.reflect.Field;

/**
 * Created by 林 on 2018/3/8.
 */

public class MyUtils {
    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**注意！！！
     * R.drawable.class 中的R包必须是图片所在项目的R包！！！
     * */
    public static void setImageResource(ImageView imageView, String variableName) {
        if (getResId(variableName, R.drawable.class) != -1) {
            imageView.setImageResource(getResId(variableName, R.drawable.class));
        }
    }
}
