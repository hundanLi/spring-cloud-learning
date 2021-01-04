package io.github.hundanli.sentinel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/1/4 14:27
 */
@RestController
public class RestTemplateController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @GetMapping("/rest/{name}")
    public String restHello(@PathVariable String name) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("openfeign-service");
        String url = String.format("http://%s:%s/%s", serviceInstance.getHost(), serviceInstance.getPort(), name);
        return restTemplate.getForObject(url, String.class);
    }
}
