//package com.linqinen.library.widget;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.Path;
//import android.support.annotation.Nullable;
//import android.util.AttributeSet;
//import android.view.View;
//
//import com.linqinen.library.R;
//import com.linqinen.library.utils.LogT;
//
//
///**
// * Created by 林 on 2017/5/26.
// * 这个View控件属于空壳，没有完全开发好
// */
//
//public class MyDashboardView extends View {
//
//    private int mRadius; // 圆弧半径
//    private int mStartAngle; // 起始角度
//    private int mSweepAngle; // 绘制角度
//    private int mTextColor; // 字体颜色
//    private int mTextSize; // 刻度字体大小
//
//    private Paint mPaintText;
//    private Paint mPaintBitmap;//图片
//    private Paint mPaintPointer;//指针
//    private Path path;
//
//    private int mViewWidth; // 控件宽度
//    private int mViewHeight; // 控件高度
//    private float mCenterX;
//    private float mCenterY;
//
//    public MyDashboardView(Context context) {
//        this(context, null);
//    }
//
//    public MyDashboardView(Context context, @Nullable AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public MyDashboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyDashboardView, defStyleAttr, 0);
//        mRadius = a.getDimensionPixelSize(R.styleable.MyDashboardView_radius, 200);
//        mStartAngle = a.getInteger(R.styleable.MyDashboardView_startAngle, 180);
//        mSweepAngle = a.getInteger(R.styleable.MyDashboardView_sweepAngle, 180);
//        mTextColor = a.getColor(R.styleable.MyDashboardView_textColor, Color.BLACK);
//        mTextSize = a.getDimensionPixelSize(R.styleable.MyDashboardView_measureTextSize, 15);
//
//        a.recycle();
//
//        initView();
//
//        initData();
//
//    }
//
//    /*** wrap_parent -> MeasureSpec.AT_MOST
//     * match_parent -> MeasureSpec.EXACTLY
//     * 具体值 -> MeasureSpec.EXACTLY*/
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
////        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
////        LogT.i("widthMeasureSpec:" + widthMeasureSpec+ ", heightMeasureSpec:"  + heightMeasureSpec);
//
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//
//        mViewWidth = widthSize;
//        mViewHeight = heightSize;
//        setMeasuredDimension(mViewWidth, mViewHeight);
//        mCenterX = mViewWidth/2;
//        mCenterY = mViewHeight/2;
//        LogT.i("widthMode:" +widthMode + ", widthSize:"  + widthSize+",heightMode:" +heightMode + ", heightSize:"  + heightSize+",mViewWidth:" +mViewWidth + ", mViewHeight:"  + mViewHeight+",mCenterX:" + mCenterX+ ", mCenterY:"  + mCenterY);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//
//        drawBitmap(canvas);
//    }
//
//    /**初始化控件*/
//    private void initView() {
//        mPaintText = new Paint();
//        mPaintText.setAntiAlias(true);
//        mPaintText.setColor(mTextColor);
////        mPaintText.setStyle(Paint.Style.STROKE);
//
//        mPaintBitmap = new Paint();
//        mPaintBitmap.setAntiAlias(true);
////        p.setStyle(Paint.Style.STROKE);
////        mPaintBitmap.setColor(getResources().getColor(R.color.green_803CDF5F));
//
//        mPaintPointer = new Paint();
//        mPaintPointer.setAntiAlias(true);
//        mPaintPointer.setStyle(Paint.Style.FILL);
//        mPaintPointer.setColor(Color.BLACK);
//
//    }
//
//    /**初始化数据*/
//    private void initData() {
//
//    }
//
//    private void drawBitmap(Canvas canvas){
////        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.breathing_heart_rate_icon_heart), mCenterX, mCenterY, mPaintBitmap);
//
////        canvas.drawCircle(mCenterX, mCenterY, mCenterX-mStripeWidth-10, p);
//    }
//
//    /**
//     * 绘制指针
//     */
////    private void drawPointer(Canvas canvas) {
////        path.reset();
////        float[] point1 = getCoordinatePoint(mCircleRadius / 2, initAngle + 90);
////        path.moveTo(point1[0], point1[1]);
////        float[] point2 = getCoordinatePoint(mCircleRadius / 2, initAngle - 90);
////        path.lineTo(point2[0], point2[1]);
////        float[] point3 = getCoordinatePoint(mPointerRadius, initAngle);
////        path.lineTo(point3[0], point3[1]);
////        path.close();
////        canvas.drawPath(path, mPaintPointer);
////        // 绘制三角形指针底部的圆弧效果
////        canvas.drawCircle((point1[0] + point2[0]) / 2, (point1[1] + point2[1]) / 2,
////                mCircleRadius / 2, mPaintPointer);
////    }
//
////    private void drawText(){
////        //绘制圆盘上的数字
////        mPaintText.setTextSize(mTextSize);
//////        String number = mGraduations[i];
////        String number = "11";
////        mPaintText.getTextBounds(number, 0, number.length(), mRectMeasures);
////        if (angle % 360 > 135 && angle % 360 < 225) {
////            mPaintText.setTextAlign(Paint.Align.LEFT);
////        } else if ((angle % 360 >= 0 && angle % 360 < 45) || (angle % 360 > 315 && angle % 360 <= 360)) {
////            mPaintText.setTextAlign(Paint.Align.RIGHT);
////        } else {
////            mPaintText.setTextAlign(Paint.Align.CENTER);
////        }
////        float[] numberPoint = getCoordinatePoint(mNumMeaRadius, angle);
////        if (i == 0 || i == mBigSliceCount) {
////            canvas.drawText(number, numberPoint[0], numberPoint[1] + (mRectMeasures.height() / 2), mPaintText);
////        } else {
////            canvas.drawText(number, numberPoint[0], numberPoint[1] + mRectMeasures.height(), mPaintText);
////        }
////    }
//}
