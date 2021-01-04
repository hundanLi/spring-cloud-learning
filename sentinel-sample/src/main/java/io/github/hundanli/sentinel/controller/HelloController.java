package io.github.hundanli.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/1/4 14:26
 */
@RestController
public class HelloController {

    @GetMapping("/hello/{name}")
    @SentinelResource(value = "hello")
    public String hello(@PathVariable String name) {
        System.out.println("Hello, " + name);
        return "Hello, " + name;
    }

}
