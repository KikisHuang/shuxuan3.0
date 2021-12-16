package com.gxdingo.sg;


import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.blankj.utilcode.util.RegexUtils.isMobileExact;
import static com.blankj.utilcode.util.TimeUtils.string2Millis;
import static com.kikis.commnlibrary.utils.DateUtils.getCustomDate;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);

//        String json = "{\"msg\":\"请求成功\",\"code\":0,\"data\":{}}";
//        NormalBean normalBean = GsonUtil.GsonToBean(json, NormalBean.class);


        System.out.println(getCustomDate(string2Millis("2021-11-10 20:45:45"), string2Millis("2021-11-16 20:45:45")));


        int a = 25;
        int b = 10;

        String aa = "123456";

        String bb[] = aa.split("");
        System.out.println("" + a % b);
    }

    private static final String SEP1 = "#";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";

    /**
     * String转换List
     *
     * @param listText :需要转换的文本
     * @return List<?>
     */
    public static List<Object> StringToList(String listText) {
        if (listText == null || listText.equals("")) {
            return null;
        }
        listText = listText.substring(1);

        listText = listText;

        List<Object> list = new ArrayList<Object>();
        String[] text = listText.split(SEP1);
        for (String str : text) {
            if (str.charAt(0) == 'M') {
                Map<?, ?> map = StringToMap(str);
                list.add(map);
            } else if (str.charAt(0) == 'L') {
                List<?> lists = StringToList(str);
                list.add(lists);
            } else {
                list.add(str);
            }
        }
        return list;
    }

    /**
     * String转换Map
     *
     * @param mapText          :需要转换的字符串
     * @param KeySeparator     :字符串中的分隔符每一个key与value中的分割
     * @param ElementSeparator :字符串中每个元素的分割
     * @return Map<?, ?>
     */
    public static Map<String, Object> StringToMap(String mapText) {

        if (mapText == null || mapText.equals("")) {
            return null;
        }
        mapText = mapText.substring(1);

        mapText = mapText;

        Map<String, Object> map = new HashMap<String, Object>();
        String[] text = mapText.split("\\" + SEP2); // 转换为数组
        for (String str : text) {
            String[] keyText = str.split(SEP3); // 转换key与value的数组
            if (keyText.length < 1) {
                continue;
            }
            String key = keyText[0]; // key
            String value = keyText[1]; // value
            if (value.charAt(0) == 'M') {
                Map<?, ?> map1 = StringToMap(value);
                map.put(key, map1);
            } else if (value.charAt(0) == 'L') {
                List<?> list = StringToList(value);
                map.put(key, list);
            } else {
                map.put(key, value);
            }
        }
        return map;
    }
}