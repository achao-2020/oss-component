package com.demo.oss.auto.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 配置文件父类
 * @author achao
 */
@Getter
@Setter
public abstract class OssProperties {

    /**
     * oss类型, 默认aliyun
     */
    @Value("${demo.oss.type:aliyun}")
    private String ossType;
}
