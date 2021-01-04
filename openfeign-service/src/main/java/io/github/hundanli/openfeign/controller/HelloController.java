package io.github.hundanli.openfeign.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/1/4 11:37
 */
@RestController
public class HelloController {
    @GetMapping
    public String hello() {
        return "Hello, feign service!";
    }
}
