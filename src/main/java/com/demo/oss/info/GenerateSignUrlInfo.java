package com.demo.oss.info;

import lombok.Data;

/**
 * 通用授权url信息类
 * @author achao
 */
@Data
public class GenerateSignUrlInfo {
    /**
     * 授权url
     */
    private String signUrl;
    /**
     * http请求头类型
     */
    private String contentType;
    /**
     * 过期时间
     */
    private String expiration;
    /**
     * objectKey
     */
    private String objectKey;
    /**
     * 临时accessKeyId
     */
    private String accessKeyId;
    /**
     * 临时 accessKeySecret
     */
    private String accessKeySecret;
    /**
     * 授权token
     */
    private String securityToken;
    /**
     *  访问域名
     */
    private String endpoint;
}
