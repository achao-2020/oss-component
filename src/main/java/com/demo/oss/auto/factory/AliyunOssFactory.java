package com.demo.oss.auto.factory;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.demo.oss.auto.config.AliyunOssProperties;
import com.demo.oss.client.AliyunOssClient;
import com.demo.oss.client.OssClient;

/**
 * 阿里云oss创建工厂
 * @author achao
 */
public class AliyunOssFactory extends OssFactory{

    private AliyunOssProperties aliyunOssProperties;

    protected AliyunOssFactory(AliyunOssProperties ossProperties) {
        aliyunOssProperties = ossProperties;
    }

    @Override
    public OssClient createMqiOss() {
        OSS aliyunOSS = initAliyunOss(this.aliyunOssProperties);
        return new AliyunOssClient(aliyunOSS, aliyunOssProperties);
    }

    private OSS initAliyunOss(AliyunOssProperties aliyunOssProperties) {
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(aliyunOssProperties.getMaxConnections());
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(aliyunOssProperties.getSocketTimeout());
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(aliyunOssProperties.getConnectionTimeout());
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(aliyunOssProperties.getConnectionRequestTimeout());
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(aliyunOssProperties.getIdleConnectionTime());
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(5);
        return new OSSClientBuilder().build(aliyunOssProperties.getEndpoint(), aliyunOssProperties.getAccessKeyId(), aliyunOssProperties.getAccessKeySecret(), conf);
    }
}
