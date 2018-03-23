package com.linqinen.library.widget.flowerdialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.WindowManager;

import com.linqinen.library.R;


public abstract class BaseDialog extends Dialog {

    protected BaseDialog(Context context) {
        super(context, R.style.LoadingDialog);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    protected int getMinimumSideOfScreen(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return Math.min(size.x, size.y);
    }

}
