package com.demo.oss.info;

import lombok.Data;

import java.io.InputStream;

/**
 * oss对象信息
 * @author achao
 */
@Data
public class OssObjectInfo {
    /**
     * 存储对象名称
     */
    private String objectKey;
    /**
     * 存储空间
     */
    private String bucketName;
    /**
     * 其他信息
     */
    private Object metadata;
    /**
     * 数据流
     */
    private InputStream objectContent;
}
