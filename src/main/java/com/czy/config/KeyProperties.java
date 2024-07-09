package com.czy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

/**
 * @author: czy
 * @date: 2024/7/9 上午10:20
 */
@ConfigurationProperties(prefix = "spring.encrypt")
public class KeyProperties {

    private boolean enabled = true;
    /**
     * rsa私钥
     */
    private String privateKey = "";

    /**
     * 前端内容json key
     */
    private String contentJsonKey = "content";

    /**
     * 前端加密过后的aesKey key
     */
    private String aesJsonKey = "aesKey";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getContentJsonKey() {
        return contentJsonKey;
    }

    public void setContentJsonKey(String contentJsonKey) {
        this.contentJsonKey = contentJsonKey;
    }

    public String getAesJsonKey() {
        return aesJsonKey;
    }

    public void setAesJsonKey(String aesJsonKey) {
        this.aesJsonKey = aesJsonKey;
    }

    @PostConstruct
    public void validate() {
        if (enabled && (privateKey == null || privateKey.isEmpty())) {
            throw new IllegalArgumentException("The private key must be configured when encryption is enabled.");
        }
    }
}
