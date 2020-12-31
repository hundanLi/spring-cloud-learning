package io.github.hundanli.sentinel;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/25 17:30
 */
@SpringBootApplication
public class SentinelApp {

    public static void main(String[] args) {
        SpringApplication.run(SentinelApp.class, args);

    }


    @RestController
    public static class HelloController{

        @GetMapping("/hello/{name}")
        @SentinelResource(value = "hello")
        public String hello(@PathVariable String name) {
            System.out.println("Hello, " + name);
            return "Hello, " + name;
        }

    }

}
