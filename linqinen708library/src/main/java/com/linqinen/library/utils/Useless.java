//package com.linqinen.library.utils;
//
///**
// * Created by 林 on 2017/5/19.
// */
//
//public class Useless {
//    //圆角图
////        RoundedBitmapDrawable roundedDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
////        roundedDrawable.getPaint().setAntiAlias(true);
////        roundedDrawable.setCornerRadius(30);
////        mIvHeadPortrait.setImageDrawable(roundedDrawable);
//
//    //圆形图
////        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
////        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
////        circleDrawable.getPaint().setAntiAlias(true);
////        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
////        mIvHeadPortrait.setImageDrawable(circleDrawable);
//
////改变部分字体的颜色
////    textView = (TextView) findViewById(R.id.textview);
////    SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
//
////    //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
////    ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
////    ForegroundColorSpan whiteSpan = new ForegroundColorSpan(Color.WHITE);
////    ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
////    ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.GREEN);
////    ForegroundColorSpan yellowSpan = new ForegroundColorSpan(Color.YELLOW);
////
////
////
////builder.setSpan(redSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////builder.setSpan(whiteSpan, 1, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
////builder.setSpan(blueSpan, 2, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////builder.setSpan(greenSpan, 3, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////builder.setSpan(yellowSpan, 4,5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////
////textView.setText(builder);
//
//关于下划线的两种方法：
// 方法1
////    String content = "还需要邀请" + mProductDetailBean.getAttr().getInviteFriendsCount() + "名好友才可购买，立即邀请";
////    SpannableString spannableString = new SpannableString(content);
////                    spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_e06f36)), content.length() - 4, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //设置前景色为洋红色
////                    spannableString.setSpan(new UnderlineSpan(), content.length() - 4, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                    tv_pro_join.setText(spannableString);
//}
// 方法2
//mTvQuota.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);


/*圆形图*/
//    public Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//        final int color = 0xff424242;
//        final Paint paint = new Paint();
//        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//        final RectF rectF = new RectF(rect);
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, (float) pixels, (float) pixels, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//        return output;
//    }
