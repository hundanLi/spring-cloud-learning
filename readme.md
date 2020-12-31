# Spring Cloud Alibaba

## 0.简介

### 0.1 功能特性

Spring Cloud Alibaba 致力于提供微服务开发的一站式解决方案。此项目包含开发分布式应用服务的必需组件，方便开发者通过 Spring Cloud 编程模型轻松使用这些组件来开发分布式应用服务。

依托 Spring Cloud Alibaba，您只需要添加一些注解和少量配置，就可以将 Spring Cloud 应用接入阿里分布式应用解决方案，通过阿里中间件来迅速搭建分布式应用系统。

目前 Spring Cloud Alibaba 提供了如下功能:

1. **服务限流降级**：支持 WebServlet、WebFlux, OpenFeign、RestTemplate、Dubbo 限流降级功能的接入，可以在运行时通过控制台实时修改限流降级规则，还支持查看限流降级 Metrics 监控。
2. **服务注册与发现**：适配 Spring Cloud 服务注册与发现标准，默认集成了 Ribbon 的支持。
3. **分布式配置管理**：支持分布式系统中的外部化配置，配置更改时自动刷新。
4. **Rpc服务**：扩展 Spring Cloud 客户端 RestTemplate 和 OpenFeign，支持调用 Dubbo RPC 服务
5. **消息驱动能力**：基于 Spring Cloud Stream 为微服务应用构建消息驱动能力。
6. **分布式事务**：使用 @GlobalTransactional 注解， 高效并且对业务零侵入地解决分布式事务问题。
7. **阿里云对象存储**：阿里云提供的海量、安全、低成本、高可靠的云存储服务。支持在任何应用、任何时间、任何地点存储和访问任意类型的数据。
8. **分布式任务调度**：提供秒级、精准、高可靠、高可用的定时（基于 Cron 表达式）任务调度服务。同时提供分布式的任务执行模型，如网格任务。网格任务支持海量子任务均匀分配到所有 Worker（schedulerx-client）上执行。
9. **阿里云短信服务**：覆盖全球的短信服务，友好、高效、智能的互联化通讯能力，帮助企业迅速搭建客户触达通道。

### 0.2 依赖管理

Spring Cloud Alibaba BOM 包含了它所使用的所有依赖的版本。

可以将这个 BOM 添加到 pom.xml 中的 \<dependencyManagement\> 部分。 这将允许你省略任何Maven依赖项的版本，而是将版本控制委派给BOM。

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.2.3.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```



## 1.Nacos服务发现

[Nacos](https://nacos.io/zh-cn/) 是 Alibaba 开源的、易于构建云原生应用的动态服务发现、配置管理和服务管理平台。

使用 Spring Cloud Alibaba Nacos Discovery，可基于 Spring Cloud 的编程模型快速接入 Nacos 服务注册功能。

Nacos的安装配置请见[Nacos官网](https://nacos.io/zh-cn/)。

Nacos Discovery 适配了 Netflix Ribbon，可以使用 RestTemplate 或 OpenFeign 进行服务间的互相调用。

### 1.1 provider服务

#### 1.1.1 添加依赖

创建maven项目，修改pom.xml如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.2.7.RELEASE</version>
        <relativePath/>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <artifactId>nacos-provider</artifactId>

    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>2.2.3.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

        </dependencies>

    </dependencyManagement>

</project>
```



#### 1.1.2 修改配置

编辑application.yml文件，配置nacos-server的地址等：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: true
        server-addr: 127.0.0.1:8848
  application:
    name: nacos-provider-sample
server:
  port: 8081
management:
  endpoints:
    web:
      exposure:
        include: '*'
    enabled-by-default: true
```

#### 1.1.3 服务发现

在springboot主启动类上添加`@EnableDiscoveryClient`注解，开启服务发现功能：

```java
@SpringBootApplication
@EnableDiscoveryClient
public class NacosProviderApp {
    public static void main(String[] args) {
        SpringApplication.run(NacosProviderApp.class);
    }

    @RestController
    public static class EchoController{
        @GetMapping("/echo/{name}")
        public String echo(@PathVariable String name) {
            return "Hello, " + name;
        }
    }
}
```

上面编写了一个简单的Controller类。

启动应用后，就可以在nacos控制台看到名字为nacos-provider-sample的服务。

### 1.2 consumer应用

#### 1.2.1 依赖配置

consumer应用的pom依赖于provider一致，application.yml配置需要修改spring.application.name属性：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: true
        server-addr: 127.0.0.1:8848
  application:
    name: nacos-consumer-sample
server:
  port: 8082
management:
  endpoints:
    web:
      exposure:
        include: '*'
    enabled-by-default: true
```

#### 1.2.2 服务发现

与provider服务一致，也需要在主启动类上添加``注解：

```java
@SpringBootApplication
@EnableDiscoveryClient
public class NacosClientApp {
    public static void main(String[] args) {
        SpringApplication.run(NacosClientApp.class);
    }
}
```

#### 1.2.3 服务调用

这里使用RestTemplate和LoadBalanceClient来演示服务调用，包含以下几个步骤：

1. 装配RestTemplate和LoadBalanceClient
2. 通过LoadBalanceClient获取服务实例
3. 使用RestTemplate进行服务调用

```java
@SpringBootApplication
@EnableDiscoveryClient
public class NacosClientApp {
    public static void main(String[] args) {
        SpringApplication.run(NacosClientApp.class);
    }

    /**
     * 注入RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    public static class EchoController {

        @Autowired
        private RestTemplate restTemplate;

        @Autowired
        private LoadBalancerClient loadBalancerClient;

        @Value("${spring.application.name}")
        private String appname;

        @GetMapping("/echo/appname")
        public String echoAppName() {
            ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-provider-sample");
            String url = String.format("http://%s:%s/echo/%s", serviceInstance.getHost(), serviceInstance.getPort(), appname);
            System.out.println("request url: " + url);
            return restTemplate.getForObject(url, String.class);
        }
    }
}
```

启动应用，访问http://localhost:8082/echo/appname。

### 1.3 Nacos服务发现的Endpoint

集成Nacos Discovery的应用将会提供了一个 Endpoint, 对应的 endpoint id 为 `nacos-discovery`，这是Nacos服务发现组件定义的。

可以访问http://localhost:8081/actuator/nacos-discovery查看：

```json
{
  "subscribe": [
    {
      "name": "nacos-provider-sample",
      "groupName": "DEFAULT_GROUP",
      "clusters": "DEFAULT",
      "cacheMillis": 1000,
      "hosts": [],
      "lastRefTime": 0,
      "checksum": "",
      "allIPs": false,
      "valid": true
    }
  ],
  "NacosDiscoveryProperties": {
    "serverAddr": "127.0.0.1:8848",
    "username": "",
    "password": "",
    "endpoint": "",
    "namespace": "",
    "watchDelay": 30000,
    "logName": "",
    "service": "nacos-provider-sample",
    "weight": 1,
    "clusterName": "DEFAULT",
    "group": "DEFAULT_GROUP",
    "namingLoadCacheAtStart": "false",
    "metadata": {
      "preserved.register.source": "SPRING_CLOUD"
    },
    "registerEnabled": true,
    "ip": "10.120.24.138",
    "networkInterface": "",
    "port": 8081,
    "secure": false,
    "accessKey": "",
    "secretKey": "",
    "heartBeatInterval": null,
    "heartBeatTimeout": null,
    "ipDeleteTimeout": null,
    "instanceEnabled": true,
    "ephemeral": true,
    "nacosProperties": {
      "secretKey": "",
      "namespace": "",
      "username": "",
      "namingLoadCacheAtStart": "false",
      "enabled": "true",
      "serverAddr": "127.0.0.1:8848",
      "com.alibaba.nacos.naming.log.filename": "",
      "clusterName": "DEFAULT",
      "password": "",
      "accessKey": "",
      "endpoint": ""
    }
  }
}
```

Endpoint 暴露的 json 中包含了两个属性:

1. subscribe: 显示了当前服务有哪些服务订阅者
2. NacosDiscoveryProperties: 当前应用 Nacos服务发现的基础配置信息



## 2.Nacos配置管理

Nacos还可以用来作为[配置管理](https://github.com/alibaba/spring-cloud-alibaba/blob/master/spring-cloud-alibaba-docs/src/main/asciidoc-zh/nacos-config.adoc)中心。

如果要在项目中使用 Nacos 来实现应用的外部化配置，只需要在上述pom.xml文件的基础上添加以下starter依赖：

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

### 2.1 快速开始

#### 2.1.1 配置nacos地址

在/src/main/resources目录下创建bootstrap.yml文件，配置nacos服务器地址：

```properties
# DataId 默认使用 spring.application.name 配置跟文件扩展名结合(配置格式默认使用 properties),
# GROUP 不配置默认使用 DEFAULT_GROUP。
# 因此该配置文件对应的 Nacos Config 配置的 DataId 为 nacos-config-sample.properties, GROUP 为 DEFAULT_GROUP

spring:
  application:
    name: nacos-config-sample

  cloud:
    nacos:
      config:
        server-addr: 127.0.0.1:8848
```

#### 2.1.2 获取外部配置

```java
@SpringBootApplication
public class NacosConfigApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NacosConfigApp.class, args);
        String name = context.getEnvironment().getProperty("user.name");
        String age = context.getEnvironment().getProperty("user.dir");
        System.out.printf("user.name = %s, user.dir = %s\n", name, age);
    }
}
```

#### 2.1.3 Nacos服务端配置

```properties
Data ID:    nacos-config-sample.properties

Group  :    DEFAULT_GROUP

配置格式:    Properties

配置内容：
		user.name=li
		user.dir=/home/li
```



### 2.2 高级用法

#### 2.2.1 DataId扩展名

Nacos Config 除了支持 properties 格式以外，也支持 yaml 格式，只需两步即可使用该特性：

1. 在bootstrap.yml配置以下属性值：

   ```yaml
   spring:
     cloud:
       nacos:
         config:
           file-extension: yaml
   ```

   

2. 在Nacos配置中心添加DataId为yaml扩展名的配置项，并添加配置：

   ```properties
   Data ID:    nacos-config-sample.properties
   
   Group  :    DEFAULT_GROUP
   
   配置格式:    Yaml
   
   配置内容：
   		user:
   			name: li
   		    dir: /home/li
   ```



#### 2.2.2 动态更新配置

Nacos Config 默认支持配置的动态更新，测试代码如下：

```java
@SpringBootApplication
public class NacosConfigApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NacosConfigApp.class, args);
        while (true){
            String name = context.getEnvironment().getProperty("user.name");
            String age = context.getEnvironment().getProperty("user.dir");
            System.out.printf("user.name = %s, user.dir = %s\n", name, age);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                break;
            }

        }
    }
}
```

可以通过配置`spring.cloud.nacos.config.refresh.enabled=false`来关闭动态刷新。

#### 2.2.3 profile配置

Nacos Config 在加载配置的时候，不仅仅加载了以 DataId 为 `${spring.application.name}.${file-extension:properties}` 为前缀的基础配置，还加载了DataId为 `${spring.application.name}-${profile}.${file-extension:properties}` 的基础配置。在日常开发中如果遇到多套环境下的不同配置，可以通过Spring 提供的 `${spring.profiles.active}` 这个配置项来配置。

```yaml
spring:
  profiles:
    active: dev
```

在Nacos配置中心添加DataId为“nacos-config-sample-dev.yaml”的配置：

```yaml
user:
    name: hundanli
    dir: home/hundanli

env:
    active: dev-env
```

读取配置：

```java
@SpringBootApplication
public class NacosConfigApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(NacosConfigApp.class, args);
        while (true){
            String name = context.getEnvironment().getProperty("user.name");
            String age = context.getEnvironment().getProperty("user.dir");
            // 获取env配置
            String env = context.getEnvironment().getProperty("env.active");
            System.out.printf("user.name = %s, user.dir = %s, env = %s\n", name, age, env);

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                break;
            }

        }
    }
}
```



#### 2.2.4 自定义namespace配置

Namespace概念：

>用于进行租户粒度的配置隔离。不同的命名空间下，可以存在相同的 Group 或 Data ID 的配置。Namespace 的常用场景之一是不同环境的配置的区分隔离，例如开发测试环境和生产环境的资源（如配置、服务）隔离等。

Nacos内置 Public 命名空间，默认使用的也是Public。可以在Nacos命名空间管理模块新增自定义命名空间，应用可以配置以下属性来指定命名空间：

```properties
spring.cloud.nacos.config.namespace=<namespace-id>
```

> 该配置必须放在 bootstrap.properties或者bootstrap.yml 文件中。此外 `spring.cloud.nacos.config.namespace` 的值是 namespace 对应的 id，id 值可以在 Nacos 的控制台获取。并且在添加配置时注意不要选择其他的 namespae，否则将会导致读取不到正确的配置。

#### 2.2.5 自定义group配置

在没有明确指定 `${spring.cloud.nacos.config.group}` 配置的情况下， 默认使用的是 DEFAULT_GROUP 。如果需要自定义自己的 Group，可以在bootstrap.properties中通过以下配置来实现：

```properties
spring.cloud.nacos.config.group=DEVELOP_GROUP
```

#### 2.2.6 Endpoint

Nacos Config 内部提供了一个 Endpoint, 对应的 endpoint id 为 `nacos-config`。

Endpoint 暴露的 json 中包含了三种属性:

1. Sources: 当前应用配置的数据信息
2. RefreshHistory: 配置刷新的历史记录
3. NacosConfigProperties: 当前应用 Nacos 的基础配置信息

这是 Endpoint 暴露的 json 示例:

```json
{
  "NacosConfigProperties": {
    "serverAddr": "127.0.0.1:8848",
    "username": "",
    "password": "",
    "encode": null,
    "group": "DEFAULT_GROUP",
    "prefix": null,
    "fileExtension": "yaml",
    "timeout": 3000,
    "maxRetry": null,
    "configLongPollTimeout": null,
    "configRetryTime": null,
    "enableRemoteSyncConfig": false,
    "endpoint": null,
    "namespace": "product",
    "accessKey": null,
    "secretKey": null,
    "contextPath": null,
    "clusterName": null,
    "name": null,
    "sharedConfigs": null,
    "extensionConfigs": null,
    "refreshEnabled": true,
    "refreshableDataids": null,
    "configServiceProperties": {
      "secretKey": "",
      "namespace": "product",
      "fileExtension": "yaml",
      "username": "",
      "enableRemoteSyncConfig": "false",
      "configLongPollTimeout": "",
      "configRetryTime": "",
      "encode": "",
      "serverAddr": "127.0.0.1:8848",
      "maxRetry": "",
      "clusterName": "",
      "password": "",
      "accessKey": "",
      "endpoint": ""
    },
    "extConfig": null,
    "sharedDataids": null
  },
  "RefreshHistory": [],
  "Sources": [
    {
      "lastSynced": "2020-12-25 15:04:40",
      "dataId": "nacos-config-sample.yaml"
    },
    {
      "lastSynced": "2020-12-25 15:04:40",
      "dataId": "nacos-config-sample-dev.yaml"
    },
    {
      "lastSynced": "2020-12-25 15:04:40",
      "dataId": "nacos-config-sample"
    }
  ]
}
```



## 3.OpenFeign

Feign是一个声明式的Web服务客户端。通过它结合服务注册发现可以很方便地实现服务-服务之间的互相调用。

### 3.1  服务定义

服务端与常规的微服务应用几乎没有区别，只需注册到注册中心即可。

#### 3.1.1  添加依赖

创建SpringBoot应用，添加以下依赖：

```xml
    <dependencies>
        <!--Nacos discovery-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>2.2.3.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

        </dependencies>

    </dependencyManagement>
```

#### 3.1.2 配置yml

编辑application.yml文件：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        enabled: true

  application:
  	# 服务名称，feign需要使用
    name: openfeign-service

server:
  port: 8090
management:
  endpoints:
    web:
      exposure:
        include: '*'
    enabled-by-default: true
```

#### 3.1.3 编写简单接口

```java
@RestController
public class FeignController {

    @GetMapping("/{name}")
    public String feignHello(@PathVariable(value = "name") String name) {
        return "Hello, " + name;
    }

    @PostMapping("/{name}")
    public String feignPostHello(@PathVariable(value = "name") String name) {
        return "Hello, Post, " + name;
    }
}

```



### 3.2 FeignClient服务调用

#### 3.2.1 添加依赖

创建SpringBoot应用，添加以下依赖：

```xml
    <dependencies>
        <!--Nacos discovery-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <!--openfeign-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <version>2.2.3.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>Hoxton.RELEASE</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

        </dependencies>

    </dependencyManagement>
```



#### 3.2.2 配置yml

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        enabled: true

  application:
    name: openfeign-client

server:
  port: 8091
management:
  endpoints:
    web:
      exposure:
        include: '*'
    enabled-by-default: true
```



#### 3.2.3 启用FeignClient

在主启动类上添加`@EnableFeignClients`注解，开启FeignClient调用：

```java
@SpringBootApplication
@EnableFeignClients
public class FeignClientApp {

    public static void main(String[] args) {
        SpringApplication.run(FeignClientApp.class, args);
    }
}
```



#### 3.2.4 定义FeignClient接口

定义FeignClient接口，标注`@FeignClient`注解：

```java
@FeignClient(name = "openfeign-service") // name就是在注册中心上的服务名称
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
```

#### 3.2.5 使用FeignClient接口

定义以后，便可以在其他Bean中注入改接口并调用：

```java
@RestController
public class HelloController {

    @Autowired
    HelloFeignClient feignClient;

    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        return feignClient.getHello(name);
    }

    @PostMapping("/{name}")
    public String postHello(@PathVariable String name) {
        return feignClient.postHello(name);
    }
}
```





## 4.Spring Cloud Gateway





## 5.Sentinel

随着微服务的流行，服务和服务之间的稳定性变得越来越重要。 [Sentinel](https://github.com/alibaba/Sentinel) 以流量为切入点，从流量控制、熔断降级、系统负载保护等多个维度保护服务的稳定性。

[Sentinel](https://github.com/alibaba/Sentinel) 具有以下特性:

- **丰富的应用场景**： Sentinel 承接了阿里巴巴近 10 年的双十一大促流量的核心场景，例如秒杀（即突发流量控制在系统容量可以承受的范围）、消息削峰填谷、实时熔断下游不可用应用等。
- **完备的实时监控**： Sentinel 同时提供实时的监控功能。您可以在控制台中看到接入应用的单台机器秒级数据，甚至 500 台以下规模的集群的汇总运行情况。
- **广泛的开源生态**： Sentinel 提供开箱即用的与其它开源框架/库的整合模块，例如与 Spring Cloud、Dubbo、gRPC 的整合。您只需要引入相应的依赖并进行简单的配置即可快速地接入 Sentinel。
- **完善的 SPI 扩展点**： Sentinel 提供简单易用、完善的 SPI 扩展点。您可以通过实现扩展点，快速的定制逻辑。例如定制规则管理、适配数据源等。

### 5.1 快速入门

#### 5.1.1 添加依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

#### 5.1.2 定义资源

```java
@SpringBootApplication
public class SentinelApp {

    public static void main(String[] args) {
        SpringApplication.run(SentinelApp.class, args);

    }


    @RestController
    public static class HelloController{

        @GetMapping("/hello/{name}")
        @SentinelResource(value = "hello")
        public String hello(@PathVariable String name) {
            System.out.println("Hello, " + name);
            return "Hello, " + name;
        }

    }

}

```

@SentinelResource 注解用来标识资源是否被限流、降级。上述例子上该注解的属性 'hello' 表示资源名。

@SentinelResource 还提供了其它额外的属性如 `blockHandler`，`blockHandlerClass`，`fallback` 用于表示限流或降级的操作，更多内容可以参考 [Sentinel注解支持](https://github.com/alibaba/Sentinel/wiki/注解支持)。

#### 5.1.3 启动sentinel控制台

Sentinel 控制台提供一个轻量级的控制台，它提供机器发现、单机资源实时监控、集群资源汇总，以及规则管理的功能。您只需要对应用进行简单的配置，就可以使用这些功能。

使用sentinel仅需三步：

1. 从 [release 页面](https://github.com/alibaba/Sentinel/releases) 下载最新版本的控制台 jar 包

2. 启动控制台：

   ```bash
   java -Dserver.port=7080 -Dcsp.sentinel.dashboard.server=localhost:7080 -Dproject.name=sentinel-dashboard -jar sentinel-dashboard.jar
   ```

#### 5.1.4 yml配置

在application.yml配置sentinel控制台信息：

```yml
spring:
  application:
    name: sentinel-sample
  cloud:
    sentinel:
      transport:
        port: 8719
        dashboard: localhost:7080
```

这里的 `spring.cloud.sentinel.transport.port` 端口配置会在应用对应的机器上启动一个 Http Server，该 Server 会与 Sentinel 控制台做交互。比如 Sentinel 控制台添加了1个限流规则，会把规则数据 push 给这个 Http Server 接收，Http Server 再将规则注册到 Sentinel 中。



### 5.2 OpenFeign支持



### 5.3 RestTemplate支持



### 5.4 动态数据源配置



### 5.5 网关支持









