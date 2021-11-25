package com.kikis.commnlibrary.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 格式类
 * 创建时间：2015-12-07
 *
 * @author pjw
 */
public class FormatUtils {
    /**
     * 手机号段是否合法
     */
    public static boolean isMobileNum(String num) {
        Pattern p = Pattern.compile("^(1[3,4,5,7,8][0-9])\\d{8}$");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    /**
     * 城市名格式
     *
     * @return
     */
    public static String cityNameFormat(String mCityName) {
        if (mCityName != null) {
            String mLast = mCityName.substring(mCityName.length() - 1);
            //去掉市字
            if (mLast.equals("市")) {
                mCityName = mCityName.substring(0, mCityName.length() - 1);
                return mCityName;
            } else {
                return mCityName;
            }
        }
        return "";
    }

    /**
     * 是否是整型数字
     *
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     */
    public static String subZeroAndDot(double value) {
        return subZeroAndDot(String.valueOf(value));
    }

    public static String subZeroAndDot(String value) {
        if (value != null && !value.equals("")) {
            if (value.indexOf(".") > 0) {
                value = value.replaceAll("0+?$", "");// 去掉多余的0
                value = value.replaceAll("[.]$", "");// 如最后一位是.则去掉
            }
        }
        return value;
    }

    /**
     * 获取价格格式
     *
     * @return
     */
    public static String priceFormat(String price) {
        if (price != null && !price.equals("")) {
            double p = Double.parseDouble(price);
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(p);
        }
        return price;
    }

    /**
     * 获取价格格式
     *
     * @return
     */
    public static String priceFormat(double price1) {
        String price = String.valueOf(price1);
        if (price != null && !price.equals("")) {
            double p = Double.parseDouble(price);
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(p);
        }
        return price;
    }

    /**
     * 获取价格格式
     * 保留小数点后一位
     *
     * @return
     */
    public static String priceFormat1(String price) {
        if (price != null && !price.equals("")) {
            double p = Double.parseDouble(price);
            DecimalFormat df = new DecimalFormat("0.0");
            return df.format(p);
        }
        return price;
    }

    public static boolean isDouble(String string) {
        try {
            Double d = new Double(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 字符串中是否有汉字
     *
     * @return
     */
    public static boolean isThereChineseCharacter(String str) {
        int count = 0;
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count == 0 ? false : true;
    }

    /**
     * 字符串中是否有空格
     *
     * @return
     */
    public static boolean isBlank(String str) {
        if (str.indexOf(" ") >= 0) {
            return true;
        }
        return false;
    }

    /**
     * 字符串中是否只有字母或者数字
     *
     * @return
     */
    public static boolean isOnlyLettersAndNumbers(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }

    /**
     * 邮箱是否合法
     */
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isJosnFormat(String json) {
        boolean result = false;
        try {
            JSONObject object = new JSONObject(json);
            result = true;
        } catch (JSONException e) {
            result = false;
        }
        return result;
    }

    /**
     * 前几个字符显示，后面其余字符代替成自定义字符
     *
     * @param content          内容
     * @param firstFew         前几个字符显示,从1开始
     * @param replaceCharacter 自定义字符
     * @return
     */
    public static String firstFewShowOutputFormat(String content, int firstFew, char replaceCharacter) {
        if (content.length() > firstFew) {
            int count = content.length() - firstFew;
            String rChar = "";
            for (int i = 0; i < count; i++) {
                rChar += replaceCharacter;
            }
            return content.substring(0, firstFew) + rChar;
        }
        return content;
    }

    /**
     * 范围替换成自定义字符
     *
     * @param content          内容
     * @param start            开始
     * @param end              结束
     * @param replaceCharacter 自定义字符 start - end之间显示的字符
     * @return
     */
    public static String rangeShowOutputFormat(String content, int start, int end, char replaceCharacter) {
        if (content.length() > start && content.length() > end) {
            String newContent = "";
            for (int i = 0; i < content.length(); i++) {
                if (i >= start && i <= end) {
                    newContent += replaceCharacter;
                } else {
                    newContent += content.charAt(i);
                }
            }
            return newContent;
        }
        return content;
    }

    /**
     * 将内容后几个字符显示
     *
     * @param content 内容
     * @param lastFew 后几个字符显示
     * @return
     */
    public static String lastFewOutputFormat(String content, int lastFew) {
        if (content.length() > lastFew) {
            int i = content.length() - lastFew;
            return content.substring(i, content.length());
        }
        return content;
    }


}
