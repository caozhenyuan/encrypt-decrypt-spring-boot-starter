package com.czy.threadLocal;

/**
 * @author: czy
 * @date: 2024/7/9 下午1:26
 */
public class AesKeyThreadLocal {

    private AesKeyThreadLocal() {

    }

    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();


    public static void put(String key) {
        LOCAL.set(key);
    }

    public static String get() {
        return LOCAL.get();
    }

    public static void remove() {
        LOCAL.remove();
    }
}
