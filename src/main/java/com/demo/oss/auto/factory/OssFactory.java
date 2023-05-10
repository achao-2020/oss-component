package com.demo.oss.auto.factory;


import com.demo.oss.auto.config.AliyunOssProperties;
import com.demo.oss.auto.config.OssProperties;
import com.demo.oss.client.OssClient;

/**
 * oss创建工厂抽象类
 * @author achao
 */
public abstract class OssFactory {

    public static OssFactory createFactory(OssProperties ossProperties) {
        if (ossProperties.getOssType().equals("aliyun")) {
            return new AliyunOssFactory((AliyunOssProperties) ossProperties);
        }
        return null;
    }


    /**
     * 生成mqiOss客户端
     * @return
     */
    public abstract OssClient createMqiOss();
}
