package com.linqinen.library.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.linqinen.library.R;
import com.linqinen.library.utils.LogT;


/**
 * Created by 林 on 2017/5/2.
 */

public class MyDialog extends Dialog {

    private TextView mTvTitle;

    public TextView getTvTitle() {
        return mTvTitle;
    }

    private TextView mTvMessage;
    private TextView mTvNegativeButton;
    private TextView mTvPositiveButton;
    private ImageView mIvMessageTopImage;

    private float mWidth = 0.8f, mHeight = 0.6f,mTextSize = 15;
    private Context mContext;

    //在构造方法里预加载我们的样式，这样就不用每次创建都指定样式了
    public MyDialog(Context context) {
        this(context, R.style.MyLinDialog);

    }
    public MyDialog(Context context,float width, float height,float textSize) {
        this(context, R.style.MyLinDialog);
        mWidth = width;
        mHeight = height;
        mTextSize = textSize;
    }

    public MyDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        setContentView(R.layout.my_dialog);
        initView();
    }

    /**
     * 只是在show之前改变一些属性
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window dialogWindow = getWindow();
        if (dialogWindow != null && mContext != null) {

            setTextViewSize(mTextSize);

            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用

            if (mWidth <= 1 && mWidth > 0) {
                lp.width = (int) (displayMetrics.widthPixels * mWidth); // 宽度设置为屏幕的0.8
            }
            if (mHeight <= 1 && mHeight > 0) {
                lp.height = (int) (displayMetrics.heightPixels * mHeight); // 宽度设置为屏幕的0.8
            }
            dialogWindow.setAttributes(lp);
        }
    }



    /**初始化控件*/
    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mTvNegativeButton = (TextView) findViewById(R.id.tv_negativeButton);
        mTvPositiveButton = (TextView) findViewById(R.id.tv_positiveButton);
        mIvMessageTopImage = (ImageView) findViewById(R.id.iv_messageTopImage);
    }

    /**通过倍数适当缩小文字*/
    private void setTextViewSize(float textSize){
//        float initSize = mContext.getResources().getDimension(R.dimen.my_dialog_text_size);

        /*通过getResources().getDimension这种方式得到的实际上是px单位*/
        LogT.i("initSize:" + textSize);
        TextView textView[] = {mTvTitle,mTvMessage,mTvNegativeButton,mTvPositiveButton};
        for (int i = 0; i < textView.length; i++) {
            textView[i].setTextSize(textSize);//系统默认是sp单位
//            textView[i].setTextSize(20);
        }
    }

    /**
     * 设置标题栏
     */
    public MyDialog setTitle(String title) {
        if (title != null) {
            mTvTitle.setText(title);
        }
        return this;
    }

    /**
     * 设置内容上方的图片
     */
    public MyDialog setMessageTopImage(int resId) {
        if (resId != 0) {
            mIvMessageTopImage.setImageResource(resId);
            mIvMessageTopImage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置内容
     */
    public MyDialog setMessage(String msg) {
        if (msg != null) {
            mTvMessage.setText(msg);
            mTvMessage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置内容
     */
    public MyDialog setMessage(String msg, int gravity) {
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
    public MyDialog setPositiveButton(final String text, final OnMyDialogButtonClickListener listener) {
       return setPositiveButton(text,listener,true);
    }
    /**
     * 设置右边确定点击按钮
     *
     * @param text     按钮上的显示字
     * @param listener 点击事件监听
     * @param isDismiss 判断是否dismiss  Dialog
     */
    public MyDialog setPositiveButton(final String text, final OnMyDialogButtonClickListener listener,final boolean isDismiss) {
        if (text != null) {
            mTvPositiveButton.setText(text);
        }
        mTvPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick();
                }
                if(isDismiss){
                    dismiss();
                }
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
    public MyDialog setPositiveButton(final OnMyDialogButtonClickListener listener) {

        return setPositiveButton(null, listener);
    }

    /**
     * 设置左边取消点击按钮
     *
     * @param text     按钮上的显示字
     * @param listener 点击事件监听
     */
    public MyDialog setNegativeButton(String text, final OnMyDialogButtonClickListener listener) {
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
    public MyDialog setNegativeButton(final OnMyDialogButtonClickListener listener) {
        return setNegativeButton(null, listener);
    }

    public MyDialog setDialogCancelable(boolean flag) {
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
