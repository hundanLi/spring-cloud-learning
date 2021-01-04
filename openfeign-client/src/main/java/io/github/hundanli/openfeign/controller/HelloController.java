package io.github.hundanli.openfeign.controller;

import io.github.hundanli.openfeign.feignclient.HelloFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/31 18:49
 */
@RestController
public class HelloController {

    @Autowired
    HelloFeignClient feignClient;

    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return feignClient.getHello(name);
    }

    @PostMapping("/{name}")
    public String postHello(@PathVariable String name) {
        return feignClient.postHello(name);
    }

    @GetMapping
    public String hello() {
        return "Hello, feign client!";
    }
}
