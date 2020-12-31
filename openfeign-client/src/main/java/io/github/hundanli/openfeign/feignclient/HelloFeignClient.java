package io.github.hundanli.openfeign.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2020/12/31 18:42
 */
@FeignClient(name = "openfeign-service")
public interface HelloFeignClient {

    /**
     * 注解的path属性是远程服务controller类中的请求路径
     * @param name 路径参数
     * @return string
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getHello(@PathVariable(name = "name") String name);

    /**
     * 注解的path属性是远程服务controller类中的请求路径
     * @param name 路径参数
     * @return string
     */
    @RequestMapping(method = RequestMethod.GET, path = "/{name}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String postHello(@PathVariable(name = "name") String name);

}
