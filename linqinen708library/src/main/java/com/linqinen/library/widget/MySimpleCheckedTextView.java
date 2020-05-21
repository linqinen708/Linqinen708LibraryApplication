package com.linqinen.library.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.Checkable;


/**
 * Created by Ian on 2019/10/23.
 */

public class MySimpleCheckedTextView extends AppCompatTextView implements Checkable{
    private boolean checked = false;

    private static final int [] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public MySimpleCheckedTextView(Context context) {
        super(context);
    }

    public MySimpleCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySimpleCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);
        if(isChecked()){
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public void setChecked(boolean checked) {
        if(this.checked != checked){
            this.checked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return this.checked;
    }

    @Override
    public void toggle() {
        setChecked(!this.checked);
    }
}
