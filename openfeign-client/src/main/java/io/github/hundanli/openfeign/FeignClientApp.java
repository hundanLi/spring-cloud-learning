package io.github.hundanli.openfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/31 18:33
 */
@SpringBootApplication
@EnableFeignClients
public class FeignClientApp {

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApp.class, args);
    }
}
