package io.github.hundanli.sentinel.openfeign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/1/4 15:06
 */
@Configuration
public class FeignConfiguration {
    @Bean
    public HelloServiceFallbackImpl helloServiceFallback() {
        return new HelloServiceFallbackImpl();
    }
}
