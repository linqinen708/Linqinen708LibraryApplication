package com.linqinen.library.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linqinen.library.R;


/**
 * Created by 林 on 2017/5/2.
 */

public class MyDialog350dp extends Dialog {

    private TextView mTvTitle;
    private TextView mTvMessage;
    private TextView mTvNegativeButton;
    private TextView mTvPositiveButton;
    private ImageView mIvMessageTopImage;

    private Context mContext;

    //在构造方法里预加载我们的样式，这样就不用每次创建都指定样式了
    public MyDialog350dp(Context context) {
        this(context, R.style.MyLinDialog);

    }

    public MyDialog350dp(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        setContentView(R.layout.my_dialog_350dp);
        initView();
    }

    /**
     * 只是在show之前改变一些属性
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 预先设置Dialog的一些属性
//        Window dialogWindow = getWindow();
//        WindowManager.LayoutParams p = dialogWindow.getAttributes();
//        p.x = 0;
//        p.y = 100;
//        p.gravity = Gravity.LEFT | Gravity.TOP;
//        dialogWindow.setAttributes(p);

//        LogT.i("开始显示dialog");
    }

    /**初始化控件*/
    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mTvNegativeButton = (TextView) findViewById(R.id.tv_negativeButton);
        mTvPositiveButton = (TextView) findViewById(R.id.tv_positiveButton);
        mIvMessageTopImage = (ImageView) findViewById(R.id.iv_messageTopImage);
    }

    /**
     * 设置标题栏
     */
    public MyDialog350dp setTitle(String title) {
        if (title != null) {
            mTvTitle.setText(title);
        }
        return this;
    }

    /**
     * 设置内容上方的图片
     */
    public MyDialog350dp setMessageTopImage(int resId) {
        if (resId != 0) {
            mIvMessageTopImage.setImageResource(resId);
            mIvMessageTopImage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置内容
     */
    public MyDialog350dp setMessage(String msg) {
        if (msg != null) {
            mTvMessage.setText(msg);
            mTvMessage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置内容
     */
    public MyDialog350dp setMessage(String msg, int gravity) {
        if (msg != null) {
            mTvMessage.setText(msg);
            mTvMessage.setGravity(gravity);
            mTvMessage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置右边确定点击按钮
     *
     * @param text     按钮上的显示字
     * @param listener 点击事件监听
     */
    public MyDialog350dp setPositiveButton(final String text, final OnMyDialogButtonClickListener listener) {
        if (text != null) {
            mTvPositiveButton.setText(text);
        }
        mTvPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick();
                }
                dismiss();
            }
        });
        return this;
    }

    public interface OnMyDialogButtonClickListener {
        void onClick();
    }


    /**
     * 设置右边确定点击按钮
     * 默认 点击 字体 为 确定
     *
     * @param listener 点击事件监听
     */
    public MyDialog350dp setPositiveButton(final OnMyDialogButtonClickListener listener) {

        return setPositiveButton(null, listener);
    }

    /**
     * 设置左边取消点击按钮
     *
     * @param text     按钮上的显示字
     * @param listener 点击事件监听
     */
    public MyDialog350dp setNegativeButton(String text, final OnMyDialogButtonClickListener listener) {
        if (text != null) {
            mTvNegativeButton.setText(text);
        }
        mTvNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick();
                }
                dismiss();
            }
        });
        return this;
    }

    /**
     * 设置右边取消点击按钮
     * 默认 点击 字体 为 取消
     *
     * @param listener 点击事件监听
     */
    public MyDialog350dp setNegativeButton(final OnMyDialogButtonClickListener listener) {
        return setNegativeButton(null, listener);
    }

    public MyDialog350dp setDialogCancelable(boolean flag) {
        setCancelable(flag);
        return this;
    }

    public void creatSimpleShow(final Activity mActivity) {
        setTitle("你确定要放弃当前操作吗？")
                .setPositiveButton("确定", new OnMyDialogButtonClickListener() {
                    @Override
                    public void onClick() {
                        if (mActivity != null) {
                            mActivity.finish();
                        }
                    }
                }).setNegativeButton(null).show();
    }


}
