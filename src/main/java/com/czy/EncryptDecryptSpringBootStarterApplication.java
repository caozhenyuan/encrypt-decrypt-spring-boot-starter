package com.czy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author czy
 */
@SpringBootApplication(scanBasePackages = {"com.czy"})
public class EncryptDecryptSpringBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(EncryptDecryptSpringBootStarterApplication.class, args);
    }

}
