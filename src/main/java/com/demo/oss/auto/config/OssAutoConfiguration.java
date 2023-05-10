package com.demo.oss.auto.config;

import com.demo.oss.auto.factory.OssFactory;
import com.demo.oss.client.OssClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置开启入口
 * @author achao
 */
@Configuration(proxyBeanMethods = false)
@ConfigurationPropertiesScan(value = {"com.demo.oss.auto.config"})
public class OssAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(OssFactory.class)
    public OssClient mqiOssClient(OssFactory ossFactory) {
        return ossFactory.createMqiOss();
    }

    @Bean
    @ConditionalOnMissingBean(OssProperties.class)
    public OssFactory ossFactory(OssProperties ossProperties) {
        return OssFactory.createFactory(ossProperties);
    }
}
