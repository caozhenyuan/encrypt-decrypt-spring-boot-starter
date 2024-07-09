package com.czy.config;

/**
 * @author: czy
 * @date: 2024/7/9 上午10:23
 */
public class KeyService {

    private final String rsaPrivateKey;

    private final String contentJsonKey;

    private final String aesJsonKey;

    public KeyService(String rsaPrivateKey, String contentJsonKey, String aesJsonKey) {
        this.rsaPrivateKey = rsaPrivateKey;
        this.contentJsonKey = contentJsonKey;
        this.aesJsonKey = aesJsonKey;
    }

    public String getRsaPrivateKey() {
        return rsaPrivateKey;
    }

    public String getContentJsonKey() {
        return contentJsonKey;
    }

    public String getAesJsonKey() {
        return aesJsonKey;
    }
}
