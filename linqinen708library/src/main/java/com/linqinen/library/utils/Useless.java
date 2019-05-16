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

//https://aleej.com/2018/07/15/%E8%A7%A3%E5%86%B3Android-Studio%E4%BE%9D%E8%B5%96%E5%BA%93%E7%89%88%E6%9C%AC%E4%B8%8D%E4%B8%80%E8%87%B4%E7%9A%84%E9%97%AE%E9%A2%98/
//版本不一致的时候，在gradle中配置以下方法
//configurations.all {
//        //循环每个依赖库
//        resolutionStrategy.eachDependency { DependencyResolveDetails details ->
//        //获取当前循环到的依赖库
//        def requested = details.requested
//        //如果这个依赖库群组的名字是com.android.support
//        if (requested.group == 'com.android.support') {
//        //且其名字不是以multidex开头的
//        if (!requested.name.startsWith("multidex")) {
//        //这里指定需要统一的依赖版本 比如我的需要配置成27.1.1
//        details.useVersion '27.1.1'
//        }
//        }
//        }
//        }
//相当于（不过不建议这么干，版本号修改后可能导致第三方库出现不兼容的问题）
//configurations.all {
//   resolutionStrategy {
//       force 'com.android.support:support-fragment:26.1.0'
//   }
//}

//https://segmentfault.com/a/1190000015805844
// 或者在build.gradle 中添加下面节点
//configuration{
//        all*.exclude module: "support-fragment"
//        }