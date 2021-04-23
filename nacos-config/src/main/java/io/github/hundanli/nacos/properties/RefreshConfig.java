package io.github.hundanli.nacos.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2021/4/6 20:25
 */
@Configuration
@EnableConfigurationProperties(RefreshConfig.RefreshProperties.class)
public class RefreshConfig {

    @Autowired
    private RefreshProperties properties;

    public RefreshProperties getProperties() {
        return properties;
    }

    @ConfigurationProperties(prefix = "scope.refresh")
    public static class RefreshProperties {
        private String appId;
        private String port;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }
    }


}
