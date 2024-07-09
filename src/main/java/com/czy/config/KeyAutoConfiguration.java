package com.czy.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: czy
 * @date: 2024/7/9 上午10:27
 */
@ConditionalOnClass(KeyProperties.class)
@EnableConfigurationProperties(KeyProperties.class)
@Configuration
@ConditionalOnProperty(prefix = "spring.encrypt", name = "enabled", havingValue = "true", matchIfMissing = true)
public class KeyAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(KeyAutoConfiguration.class);

    @Autowired
    private KeyProperties keyProperties;

    @Bean
    public KeyService keyService() {
        logger.info("RSA加密已经启动");
        return new KeyService(keyProperties.getPrivateKey(), keyProperties.getContentJsonKey(), keyProperties.getAesJsonKey());
    }
}
