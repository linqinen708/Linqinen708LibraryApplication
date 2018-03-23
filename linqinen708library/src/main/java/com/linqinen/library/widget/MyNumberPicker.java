package com.linqinen.library.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.linqinen.library.R;

import java.lang.reflect.Field;

/**
 * 数字选择器
 */
public class MyNumberPicker extends NumberPicker {

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNumberPicker(Context context) {
        super(context);

    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    public void updateView(View view) {
        //        beforeDescendants：viewgroup会优先其子类控件而获取到焦点
//        afterDescendants：viewgroup只有当其子类控件不需要获取焦点时才获取焦点
//        blocksDescendants：viewgroup会覆盖子类控件而直接获得焦点
        setDescendantFocusability(MyNumberPicker.FOCUS_BLOCK_DESCENDANTS);//使NumberPicker不可编辑（点击）
        if (view instanceof EditText) {
            // 这里修改字体的属性
            ((EditText) view).setTextColor(getResources().getColor(R.color.black_333333));
            ((EditText) view).setTextSize(20);
        }
        setNumberPickerDividerColor(this);
    }

    public String correctTime(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return "" + time;
        }
    }

    public void setNumberPickerDividerColor(NumberPicker numberPicker) {
        NumberPicker picker = numberPicker;
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    // 设置分割线的颜色值
                    pf.set(picker, new ColorDrawable(getResources().getColor(R.color.grey_787884)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 设置分割线的高度
            if (pf.getName().equals("mSelectionDividerHeight")) {
                pf.setAccessible(true);
                try {
                    int result = 1;
                    pf.set(picker, result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}
