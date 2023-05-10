package com.demo.oss.info;

import lombok.Data;

/**
 * 访问控制信息
 * @author achao
 */
@Data
public class ObjectAclInfo {
    /**
     * 访问权限
     */
    private String permission;
    /**
     * 版本
     */
    private String versionId;
}
