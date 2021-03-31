package io.github.hundanli.gatewayservice.controller;

import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/1/5 14:48
 */
@RestController
public class RouteController {

    @GetMapping("/")
    public String hello() {
        return "Hello, Gateway";
    }

    @GetMapping("/shortcut")
    public String shortcut() {
        return "Hello, shortcut!";
    }

    @GetMapping("/full")
    public String full() {
        return "Hello, full expanded!";
    }

    @GetMapping("/after")
    public String after() {
        return "After Route: " + ZonedDateTime.now();
    }

    @GetMapping("/before")
    public String before() {
        return "Before Route: " + ZonedDateTime.now();
    }

    @GetMapping("/between")
    public String between() {
        return "Between Route: " + ZonedDateTime.now();
    }

    @GetMapping("/cookie")
    public String cookie(@CookieValue(name = "cookie", defaultValue = "none") String cookieValue) {
        System.out.println(cookieValue);
        return cookieValue;
    }

    @GetMapping("/header")
    public String header(@RequestHeader(name = "X-Request-Id", defaultValue = "1") String reqId) {
        System.out.println(reqId);
        return "reqId = " + reqId;
    }

    @GetMapping("/red/{segment}")
    public String path(@PathVariable String segment) {
        return "path: red/" + segment;
    }
}
