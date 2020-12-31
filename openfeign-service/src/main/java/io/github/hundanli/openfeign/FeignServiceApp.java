package io.github.hundanli.openfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/31 18:20
 */
@SpringBootApplication
@EnableDiscoveryClient
public class FeignServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(FeignServiceApp.class, args);
    }
}
