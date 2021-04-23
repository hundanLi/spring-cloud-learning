package io.github.hundanli.nacos;

import io.github.hundanli.nacos.properties.RefreshConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/24 16:57
 */
@SpringBootApplication
public class NacosConfigApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NacosConfigApp.class, args);
        while (true) {
            String name = context.getEnvironment().getProperty("user.name");
            String age = context.getEnvironment().getProperty("user.dir");
            String namespace = context.getEnvironment().getProperty("nacos.namespace");
            String env = context.getEnvironment().getProperty("env.active");

//            System.out.printf("user.name = %s, user.dir = %s, nacos.namespace = %s, env.active = %s\n", name, age, namespace, env);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                break;
            }

        }
    }

    @RestController
    static class Controller {

        @Autowired
        RefreshConfig refreshConfig;

        @GetMapping("/prop")
        public RefreshConfig.RefreshProperties properties() {
            return refreshConfig.getProperties();
        }
    }
}
