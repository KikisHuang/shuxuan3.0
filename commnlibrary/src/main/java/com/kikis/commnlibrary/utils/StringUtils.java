package com.kikis.commnlibrary.utils;

/**
 * 字符串工具类
 * Created by pjw on 2019/2/23.
 */

public class StringUtils {

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串是否字母
     *
     * @param str
     * @return
     */
    public static boolean isAlphabet(String str) {
        return str.matches("[a-zA-Z]+");
    }

    /**
     * 截取字符
     * @param firstFew 返回的前几个字符数,从1开始
     * @return
     */
    public static String truncateCharacter(String content, int firstFew){
        if (content.length() > firstFew) {
            return content.substring(0, firstFew);
        }
        return content;
    }
}
