package io.github.hundanli.sentinel.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/1/4 14:05
 */
@FeignClient(name = "openfeign-service", fallback = HelloServiceFallbackImpl.class, configuration = FeignConfiguration.class)
public interface HelloService {
    /**
     * 注解的path属性是远程服务controller类中的请求路径
     *
     * @param name 路径参数
     * @return string
     */
    @GetMapping(path = "/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    String getHello(@PathVariable(name = "name") String name);

}
