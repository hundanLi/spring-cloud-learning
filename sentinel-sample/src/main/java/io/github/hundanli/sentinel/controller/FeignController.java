package io.github.hundanli.sentinel.controller;

import io.github.hundanli.sentinel.openfeign.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/1/4 14:27
 */
@RestController
public class FeignController {
    @Autowired
    HelloService service;

    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return service.getHello(name);
    }
}
