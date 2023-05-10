package com.demo.oss.auto.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云配置文件
 * @author achao
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "demo.oss")
public class AliyunOssProperties extends OssProperties{

    private String accessKeyId;

    private String accessKeySecret;
    /**
     * 存储空间（存储继承bucket或者私有文件）
     */
    private String bucket;
    /**
     * 公共存储空间（存储公共读或者公共读写文件）
     */
    private String publicBucket;
    /**
     * 域名地址
     */
    private String endpoint;
    /**
     * 单个ossClient的最大连接数
     */
    private Integer maxConnections;
    /**
     * 设置Socket层传输数据的超时时间，默认为50000毫秒。
     */
    private Integer socketTimeout;
    /**
     * 设置建立连接的超时时间，默认为50000毫秒。
     */
    private Integer connectionTimeout;
    /**
     * 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
     */
    private Integer connectionRequestTimeout;
    /**
     * 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
     */
    private Integer idleConnectionTime;
    /**
     * c端公共读的地址
     */
    private String publicDownLoadUrl;

    private String roleArn;

    private String roleName;

    private String regionId;
}
