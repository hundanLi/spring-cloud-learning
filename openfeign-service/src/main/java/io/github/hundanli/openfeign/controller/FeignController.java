package io.github.hundanli.openfeign.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * openfeign 服务示例
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/31 18:14
 */
@RestController
public class FeignController {

    @GetMapping("/{name}")
    public String feignHello(@PathVariable(value = "name") String name) {
        return "Hello, " + name;
    }

    @PostMapping("/{name}")
    public String feignPostHello(@PathVariable(value = "name") String name) {
        return "Hello, Post, " + name;
    }
}
