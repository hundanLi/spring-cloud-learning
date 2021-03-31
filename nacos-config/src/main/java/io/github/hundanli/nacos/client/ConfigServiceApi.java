package io.github.hundanli.nacos.client;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;

import java.util.Properties;
import java.util.concurrent.Executor;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/3/11 10:53
 */
public class ConfigServiceApi {

    public static void main(String[] args) throws NacosException {
        getConfig();
        listenConfig();
    }


    public static void getConfig() {
        try {
            String serverAddr = "127.0.0.1:8848";
            String dataId = "application.yml";
            String group = "ump-web";
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            ConfigService configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println("first config:\n" + content);
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }

    public static void listenConfig() throws NacosException {
        String serverAddr = "127.0.0.1:8848";
        String dataId = "application.yml";
        String group = "ump-web";
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String content = configService.getConfig(dataId, group, 5000);
        System.out.println(content);
        configService.addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                System.out.println("listener received config:\n" + configInfo);
            }
            @Override
            public Executor getExecutor() {
                return null;
            }
        });

        // 测试让主线程不退出，因为订阅配置是守护线程，
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        removeListener();

    }


    public static void removeListener() throws NacosException {
        String serverAddr = "127.0.0.1:8848";
        String dataId = "application.yml";
        String group = "ump-web";
        Properties properties = new Properties();
        properties.put("serverAddr", serverAddr);
        ConfigService configService = NacosFactory.createConfigService(properties);
        configService.removeListener(dataId, group, null);
    }
}
