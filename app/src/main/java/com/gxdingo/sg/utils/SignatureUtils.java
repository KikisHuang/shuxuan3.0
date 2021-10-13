package com.gxdingo.sg.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static com.blankj.utilcode.util.RegexUtils.isMatch;
import static com.blankj.utilcode.util.StringUtils.isEmpty;

public class SignatureUtils {

    /**
     * 设定一个基数，以免number值过小导致encode的字符过短
     */
    private final static long numberFixValue = 1000000000000001L;

    private final static String encodeKeyMap = "Ene60qBruXz1Jl9ohVLCdZwx3TgmfNb28QPKGMiHDWs4a7pjRAOkYvFtycU5S";

    public enum SignType {
        MD5, HMACSHA256
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data     待签名数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名
     */

    public static String generate(final Map<String, String> data, String key, SignType signType) {
        try {
            Set<String> keySet = data.keySet();
            String[] keyArray = keySet.toArray(new String[keySet.size()]);
            Arrays.sort(keyArray);
            StringBuilder sb = new StringBuilder();
            for (String k : keyArray) {
                if (k.equals("sign")) {
                    continue;
                }

                String value = data.get(k);

                if (value.length() > 0) // 参数值为空，则不参与签名
                    sb.append(k).append("=").append(value).append("&");
            }
            sb.append("key=").append(key);

            if (SignType.MD5.equals(signType)) {
                Log.i("signed", "generate: "+sb.toString());
                return MD5(sb.toString()).toUpperCase();
            } else if (SignType.HMACSHA256.equals(signType)) {
                return HMACSHA256(sb.toString(), key);
            }
        } catch (Exception e) {
            Log.e("生成签名错误:", e.getMessage());
//            log.error("生成签名错误： " + e.getMessage());
        }
        return "";
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 对一个加密数值字符进行解密
     */
    public static Long numberDecode(String str) {

        if (isEmpty(str))
            return 0l;

        try {
            if (!isMatch("^[0-9a-zA-Z]{6,18}$", str)) {
                LogUtils.e("字符格式不正确");
            }
        } catch (Exception e) {
            LogUtils.e("Decode Error == " + e);
            return 0l;
        }
 /*       String[] strArray = str.split("");
        List<String> strList = new LinkedList<>(Arrays.asList(strArray));
        String[] keyArray = encodeKeyMap.split("");*/


        List<String> strList = new LinkedList<>();

        List<String> keyList = new LinkedList<>();

        for (int i = 0; i < str.length(); i++) {
            if (!isEmpty(String.valueOf(str.charAt(i))))
                strList.add(String.valueOf(str.charAt(i)));
        }

        for (int i = 0; i < encodeKeyMap.length(); i++) {
            if (!isEmpty(String.valueOf(encodeKeyMap.charAt(i))))
                keyList.add(String.valueOf(encodeKeyMap.charAt(i)));
        }

        String[] keyArray = keyList.toArray(new String[keyList.size()]);


        int keyLength = encodeKeyMap.length();
        StringBuilder hexString = new StringBuilder();
        int rand = encodeKeyMap.indexOf(strList.remove(0));
        String verify = strList.remove(strList.size() - 1);
        for (String s : strList) {
            int index = encodeKeyMap.indexOf(s);
            if (index >= rand) {
                hexString.append(Integer.toHexString(index - rand));
            } else {
                hexString.append(Integer.toHexString(keyLength - rand + index));
            }
        }
        try {
            long number = Long.parseLong(hexString.toString(), 16);
            int r = (keyLength - rand + Long.toString(number).length()) % keyLength;

            if (!verify.equals(keyArray[r])) {
                LogUtils.e("给定字符校验错误，无法解析");
                return 0l;
            }
            return Math.subtractExact(number, numberFixValue);
        } catch (Exception e) {
            LogUtils.e("Decode Error == " + e);
        }
        return 0l;
    }

    /**
     * 获取活动类型
     *
     * @param type
     * @return
     */
    public static int getAcType(long type) {
        if (type > 0) {
            String code = String.valueOf(type);

            if (!isEmpty(code) && code.length() > 2)
                return Integer.parseInt(code.substring(0, 2));
        }
        return 0;
    }


    /**
     * 是否口令秘钥
     *
     * @param str
     * @return
     */
    public static boolean isShuXiangShibboleth(String str) {
        if (!isMatch("^[0-9a-zA-Z]{6,18}$", str)) {
            return false;
        }
        return true;
    }


    /**
     * 获取路径中的参数
     *
     * @param url 路径
     * @param key 参数名（传空默认获取最后一个参数）
     * @return
     */
    public static String getShibbolethParame(String url, String key) {

        if (!url.contains("?") || isEmpty(url))
            return "";

        String params = url.substring(url.indexOf("?") + 1);

        List<String> list = Arrays.asList(params.split("&"));

        String code = "";

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).contains(key))
                code = list.get(i).substring(list.get(i).indexOf("=") + 1);
        }
        return code;
    }

    /**
     * bitmap转为base64
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * base64转为bitmap
     *
     * @param base64Data
     * @return
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}