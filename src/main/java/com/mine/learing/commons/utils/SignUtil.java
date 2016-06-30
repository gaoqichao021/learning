package com.mine.learing.commons.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by gaoqichao on 16-4-22.
 */
public class SignUtil {

    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f'};

    /**
     * 获取签名字符串
     *
     * @param sourceParams 原始参数信息
     * @return 签名字符串
     */
    public static String sign(String sourceParams, String appSecret) {
        sourceParams = sourceParams.startsWith("?") ? sourceParams.substring(1) : sourceParams;

        String[] paramArr = sourceParams.split("&");
        Map<String, String> paramsMap = new TreeMap<>();

        String key = StringUtils.EMPTY;
        int index = 0;
        for (String param : paramArr) {
            index = param.indexOf("=");
            key = param.substring(0, index);
            if (key.equals("sign")) {
                continue;
            }
            paramsMap.put(key, param.substring(index + 1));
        }

        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            buffer.append(entry.getKey())
                    .append(entry.getValue());
        }

        String sourceStr = appSecret + buffer.toString() + appSecret;

        return SignUtil.getMD5String(sourceStr).toUpperCase();
    }

    /**
     * 对参数进行签名
     *
     * @param paramsMap 参数组
     * @param appSecret 加密字符串
     * @return 签名字符串
     */
    public static String sign(Map<String, String> paramsMap, String appSecret) {
        boolean isTreeMap = paramsMap instanceof TreeMap;
        StringBuffer buffer = new StringBuffer();
        if (isTreeMap) {
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                buffer.append(entry.getKey())
                        .append(entry.getValue());
            }
        } else {
            TreeMap<String, String> treeMap = new TreeMap<>();
            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                treeMap.put(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<String, String> entry : treeMap.entrySet()) {
                buffer.append(entry.getKey())
                        .append(entry.getValue());
            }
        }

        String sourceStr = appSecret + buffer.toString() + appSecret;

        return SignUtil.getMD5String(sourceStr).toUpperCase();

    }


    /**
     * 对字符串进行md5加密
     *
     * @param s 字符串
     * @return 加密后的字符串
     */
    public static String getMD5String(String s) {
        try {
            return getMD5String(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    /**
     * 对字节数组进行md5加密
     *
     * @param bytes 字符串
     * @return 加密后的字符串
     */
    public static String getMD5String(byte[] bytes) {
        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update(bytes);
            return bufferToHex(messagedigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
