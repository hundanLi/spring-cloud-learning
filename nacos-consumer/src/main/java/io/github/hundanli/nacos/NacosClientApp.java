package io.github.hundanli.nacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/24 14:52
 */
@SpringBootApplication
@EnableDiscoveryClient
public class NacosClientApp {
    public static void main(String[] args) {
        SpringApplication.run(NacosClientApp.class, args);
    }



    @RestController
    public static class EchoController {

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private LoadBalancerClient loadBalancerClient;

        @Qualifier("loadBalancedRestTemplate")
        @Autowired
        private RestTemplate loadBalancedRestTemplate;

        @Value("${spring.application.name}")
        private String appname;

        @GetMapping("/echo/appname")
        public String echoAppName() {
            ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-provider-sample");
            String url = String.format("http://%s:%s/echo/%s", serviceInstance.getHost(), serviceInstance.getPort(), appname);
            System.out.println("request url: " + url);
            return restTemplate.getForObject(url, String.class);
        }

        @GetMapping("/echo/appname2")
        public String echoAppName2() {
            String url = "http://nacos-provider-sample/echo/" + appname;
            System.out.println("request url: " + url);
            return loadBalancedRestTemplate.getForObject(url, String.class);
        }
    }


    @Configuration
    static class Config {

        /**
         * 注入普通的RestTemplate
         */
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        /**
         * 注入接入Ribbon的RestTemplate
         */
        @Bean
        @LoadBalanced
        public RestTemplate loadBalancedRestTemplate() {
            return new RestTemplate();
        }
    }
}
