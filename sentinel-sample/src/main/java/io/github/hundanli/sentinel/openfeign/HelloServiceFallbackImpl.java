package io.github.hundanli.sentinel.openfeign;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/1/4 15:06
 */
public class HelloServiceFallbackImpl implements HelloService {
    @Override
    public String getHello(String str) {
        return "hello fallback";
    }
}
