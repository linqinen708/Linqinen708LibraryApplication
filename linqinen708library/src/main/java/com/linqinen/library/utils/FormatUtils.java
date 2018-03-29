package com.linqinen.library.utils;


import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这个类主要用作一些公式或者数字的转换，比如有小数点等数字的转换
 */
public class FormatUtils {


    //四舍五入
    public static BigDecimal twoDecimals(String string) {
        return new BigDecimal(string).setScale(2, BigDecimal.ROUND_HALF_UP);
    }


    /**
     * 在小数结尾增加0，比如12345.0增加为12345.00
     */
    private static String addDecimal(String object, int objectLength) {
        for (int i = 0; i < objectLength; i++) {
            if (objectLength != object.length()) {
                object = object.substring(0, object.length()) + "0";
            }
        }
        return object;
    }

    /**
     * 保留两位小数，格式为"###,##0.00"，例如123,456.00   ， 123,456,789.12
     *
     * textView.setText();
     */
    public static String keepTwoDecimalsPlaceToMoney(double object) {
        DecimalFormat mDecimalFormat = new DecimalFormat("###,##0.00");

        return mDecimalFormat.format(object);
    }
    /**
     * 保留两位小数，格式为"###,##0.00"，例如123,456.00   ， 123,456,789.12
     */
    public static String keepTwoDecimalsPlaceToMoneyAndDisplayYuan(double object) {
        DecimalFormat mDecimalFormat = new DecimalFormat("###,##0.00");

        return mDecimalFormat.format(object)+"元";
    }

    /**
     * 保留两位小数，格式为"0.00",例如123456.00   ， 123456789.12
     */
    public static String keepTwoDecimalsPlace(double object) {
        DecimalFormat mDecimalFormat = new DecimalFormat("0.00");
        return mDecimalFormat.format(object);
    }

    /**
     * 不保留小数，格式为"0",例如123456   ， 123456789，主要是用在加息劵上，比如 2%加息券
     */
    public static String keepNoDecimalsPlace(double object) {
        DecimalFormat mDecimalFormat = new DecimalFormat("0");
        return mDecimalFormat.format(object);
    }


//    //时间：年-月-日
//    public static String dateYMD(String str) {
//        String format2 = "";
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//        if (str != null && str.length() != 0) {
//            format2 = format.format(new Date(Long.parseLong(str)));
//        }
//        return format2;
//    }

    /**
     * 时间：年-月-日
     * textView.setText(FormatUtils.dateYMD());
     * */
    public static String dateYMD(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return format.format(new Date(date));
    }

    //时间：年-月-日
    public static String dateYMD2(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return format.format(new Date(date));
    }

    //时间：年-月-日-时-分-秒
    public static String dateYMDHMS(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(new Date(date));
    }

    /**
     * 获取 某个字符 后面的字符
     *
     * @param str
     * @param subStr
     * @return
     */
    public static String getCharacterSubstring(String str, String subStr) {
        if (str.length() != 0) {
            if (str.contains(subStr)) {
                return str.substring((str.lastIndexOf(subStr) + 1), str.length());
            }
        }
        return "str may be null";
    }

    /**
     * 提供精确的减法运算。
     *
     * @param v1 被减数
     * @param v2 减数
     * @return 两个参数的差
     */
    public static double preciseSubtraction(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }


    /**
     * 验证手机号
     */
    public static boolean isMobileNO(String mobiles) {

        if (mobiles == null) return false;
//        //^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$
//        Pattern p = Pattern.compile("^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$");
//        //
//        Matcher m = p.matcher(mobiles);


        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证密码:字母数字组合
     */
    public static boolean isPwdEnable(String password) {
        Pattern p = Pattern.compile("^((?=.*\\d)(?=.*\\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{8,16}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }


    /**
     * 将textView的最后一位变小，比如首页的7.00% ，就是将% 变小
     */
    public static Spannable beSmallLastPosition(String productRate) {
        if (productRate != null && productRate.length() > 1) {
            Spannable span = new SpannableString(productRate);
//            textView.getText().toString().indexOf()
            span.setSpan(new RelativeSizeSpan(0.6f), productRate.length() - 1, productRate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return span;
        }
        return null;
    }

    /**
     * 将String 中的 % 全部缩小
     */
    public static Spannable beSamll(String productRate) {
        if (productRate == null) {
            return null;
        }
        Spannable span = new SpannableString(productRate);
        int position = 0;
        position = productRate.indexOf("%", position);
        while (position > 0) {
//            LogT.i("position:" + position+",productRate.length()"+productRate.length());
            span.setSpan(new RelativeSizeSpan(0.6f), position, position + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (position >= productRate.length() - 1) {
                break;
            }
            position = productRate.indexOf("%", position + 1);
//            LogT.i("position2222:" + position);
        }

        return span;
    }

    /**
     * 比如12.00%+6.00%  以第一个%为基础，之后的字体全部变小
     */
    public static Spannable beSamllAfterFirstPercent(String productRate) {
        if (productRate == null) {
            return null;
        }
        Spannable span = new SpannableString(productRate);
        span.setSpan(new RelativeSizeSpan(0.6f), productRate.indexOf("%"), span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

}
