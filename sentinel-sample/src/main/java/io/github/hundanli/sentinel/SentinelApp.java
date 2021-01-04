package io.github.hundanli.sentinel;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/25 17:30
 */
@SpringBootApplication
@EnableFeignClients
public class SentinelApp {

    public static void main(String[] args) {
        SpringApplication.run(SentinelApp.class, args);

    }

    @SentinelRestTemplate
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
