package com.linqinen.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.linqinen.library.R;
import com.linqinen.library.utils.LogT;
import com.linqinen.library.widget.MyCheckedTextView;

/**
 * Created by Ian on 2020/4/10.
 */
public class MyAlertDialog extends Dialog {

    private TextView mMessage;
    private MyCheckedTextView mTvTitle;
    private MyCheckedTextView mNegativeButton;
    private MyCheckedTextView mPositiveButton;
//    private ImageView mIvMessageTopImage;

    private float mWidth = 0.8f, mHeight = 0.4f,mTextSize = 15;

    private Context mContext;

    public MyAlertDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public MyAlertDialog(Context context,float width, float height) {
        this(context);
        mWidth = width;
        mHeight = height;
//        mTextSize = textSize;
    }

    public MyAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        initView();
    }

    protected MyAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
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

//            setTextViewSize(mTextSize);

            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用

            if (mWidth <= 1 && mWidth > 0) {
                lp.width = (int) (displayMetrics.widthPixels * mWidth); // 宽度设置为屏幕的0.8
            }
//            if (mHeight <= 1 && mHeight > 0) {
//                lp.height = (int) (displayMetrics.heightPixels * mHeight); // 宽度设置为屏幕的0.8
//            }
            dialogWindow.setAttributes(lp);
            dialogWindow.setBackgroundDrawable(null);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        setContentView(R.layout.dialog_my_alert);
        mTvTitle = findViewById(R.id.tv_title);
        mMessage = findViewById(R.id.tv_message);
        mNegativeButton = findViewById(R.id.mctv_negative_button);
        mPositiveButton = findViewById(R.id.mctv_positive_button);
//        mIvMessageTopImage = (ImageView) findViewById(R.id.iv_messageTopImage);
    }

    /**通过倍数适当缩小文字*/
    private void setTextViewSize(float textSize){
//        float initSize = mContext.getResources().getDimension(R.dimen.my_dialog_text_size);

        /*通过getResources().getDimension这种方式得到的实际上是px单位*/
        LogT.i("initSize:" + textSize);
        TextView textView[] = {mTvTitle,mMessage,mNegativeButton,mPositiveButton};
        for (int i = 0; i < textView.length; i++) {
            textView[i].setTextSize(textSize);//系统默认是sp单位
//            textView[i].setTextSize(20);
        }
    }

    /**
     * 设置标题栏
     */
    public MyAlertDialog setTitle(String title) {
        if (title != null) {
            mTvTitle.setText(title);
        }
        return this;
    }

//    /**
//     * 设置内容上方的图片
//     */
//    public MyDialog setMessageTopImage(int resId) {
//        if (resId != 0) {
//            mIvMessageTopImage.setImageResource(resId);
//            mIvMessageTopImage.setVisibility(View.VISIBLE);
//        }
//        return this;
//    }

    /**
     * 设置内容
     */
    public MyAlertDialog setMessage(String msg) {
        if (msg != null) {
            mMessage.setText(msg);
//            mMessage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置内容
     */
    public MyAlertDialog setMessage(String msg, int gravity) {
        if (msg != null) {
            mMessage.setText(msg);
            mMessage.setGravity(gravity);
//            mMessage.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置右边确定点击按钮
     *
     * @param text     按钮上的显示字
     * @param listener 点击事件监听
     */
    public MyAlertDialog setPositiveButton(final String text, final View.OnClickListener listener) {
        return setPositiveButton(text,listener,true);
    }
    /**
     * 设置右边确定点击按钮
     *
     * @param text     按钮上的显示字
     * @param listener 点击事件监听
     * @param isDismiss 判断是否dismiss  Dialog
     */
    public MyAlertDialog setPositiveButton(final String text, final View.OnClickListener listener, final boolean isDismiss) {
        if (text != null) {
            mPositiveButton.setText(text);
        }
        mPositiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
                if(isDismiss){
                    dismiss();
                }
            }
        });
        return this;
    }


    /**
     * 设置右边确定点击按钮
     * 默认 点击 字体 为 确定
     *
     * @param listener 点击事件监听
     */
    public MyAlertDialog setPositiveButton(final View.OnClickListener listener) {

        return setPositiveButton(null, listener);
    }

    /**
     * 设置左边取消点击按钮
     *
     * @param text     按钮上的显示字
     * @param listener 点击事件监听
     */
    public MyAlertDialog setNegativeButton(String text, final View.OnClickListener listener) {
        if (text != null) {
            mNegativeButton.setText(text);
        }
        mNegativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
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
    public MyAlertDialog setNegativeButton(final View.OnClickListener listener) {
        return setNegativeButton(null, listener);
    }

//    public interface OnMyDialogButtonClickListener {
//        void onClick();
//    }
}
