package com.demo.oss.info;

import lombok.Data;

/**
 * 通用签名URL信息
 * @author achao
 */
@Data
public class OssSignInfo {
    /**
     * 过期时间
     */
    private String expiration;
    /**
     * 临时访问key
     */
    private String accessKeyId;
    /**
     * 临时访问密钥
     */
    private String accessKeySecret;
    /**
     * 安全令牌
     */
    private String securityToken;
    /**
     * oss域名
     */
    private String endpoint;
}
