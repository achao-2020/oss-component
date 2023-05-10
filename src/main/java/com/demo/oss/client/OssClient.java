package com.demo.oss.client;

import com.aliyun.oss.model.CannedAccessControlList;
import com.demo.oss.info.GenerateSignUrlInfo;
import com.demo.oss.info.OssObjectInfo;
import com.demo.oss.info.OssSignInfo;
import com.demo.oss.info.ObjectAclInfo;

import java.io.InputStream;

/**
 * 文件SDK接口
 */
public interface OssClient {

    /**
     * 生成临时签名信息, 建议加入缓存。前端参考阿里云官方{@link https://help.aliyun.com/document_detail/64052.html}
     *
     * @param durationSeconds 临时访问凭证的有效时间，单位秒
     * @return
     */
    OssSignInfo signMessage(long durationSeconds);

    /**
     * 生成临时签名信息，建议加入缓存
     *
     * @param roleSessionName 用于区分不同的令牌
     * @param durationSeconds 临时访问凭证的有效时间，单位秒
     * @return
     */
    OssSignInfo signMessage(String roleSessionName, long durationSeconds);

    /**
     * 获得文件上传临时URL
     *
     * @param bucketName 存储空间
     * @param objectKey 对象名称
     * @param fileExtName 文件扩展名，如".txt"
     * @param durationSeconds 过期时间，单位秒
     * @return
     */
    GenerateSignUrlInfo uploadSignUrl(String bucketName, String objectKey, String fileExtName, Integer durationSeconds);

    /**
     * 获得文件上传临时URL
     *
     * @param bucketName 存储空间
     * @param prefix objectKey 拼接前缀
     * @param objectKey 对象名称
     * @param fileExtName 文件扩展名，如".txt"
     * @param durationSeconds 过期时间，单位秒
     * @return
     */
    GenerateSignUrlInfo uploadSignUrl(String bucketName, String prefix, String objectKey, String fileExtName, Integer durationSeconds);

    /**
     * 获取文件预览地址
     * @param bucketName
     * @param objectKey
     * @param durationSeconds
     * @return
     */
    GenerateSignUrlInfo previewSignUrl(String bucketName, String objectKey, Integer durationSeconds);

    /**
     * 通过文件流上传文件，如果是通过前端上传，请使用{@link OssClient#uploadSignUrl(String, String, String, Integer)}
     *
     * @param bucketName 存储空间
     * @param objectKey  自动拼接系统code+服务名, objectKey如果相同，文件会被覆盖
     * @param fileStream 上传的字节流
     * @param objectAcl  {@link CannedAccessControlList} 访问级别
     * @return 生成的objectKey
     */
    String uploadByStream(String bucketName, String objectKey, InputStream fileStream, CannedAccessControlList objectAcl);

    /**
     * 通过文件流上传文件，如果是通过前端上传，请使用{@link OssClient#uploadSignUrl(String, String, String, Integer)}
     *
     * @param bucketName 存储空间
     * @param prefix objectKey 拼接前缀
     * @param objectKey  对象名称
     * @param fileStream 上传的字节流
     * @param objectAcl  {@link CannedAccessControlList} 访问级别，默认继承bucket
     * @return 生成的objectKey
     */
    String uploadByStream(String bucketName, String prefix, String objectKey, InputStream fileStream, CannedAccessControlList objectAcl);

    /**
     * 通过字节数组上传文件，如果是通过前端上传，请使用{@link OssClient#uploadSignUrl(String, String, String, Integer)}
     * @param bucketName 存储空间
     * @param objectKey 对象名称
     * @param bytes 字节数组
     * @param objectAcl  {@link CannedAccessControlList} 访问级别，默认继承bucket
     * @return 生成的objectKey
     */
    String uploadByBytes(String bucketName, String objectKey, byte[] bytes, CannedAccessControlList objectAcl);

    /**
     * 通过字节数组上传文件，如果是通过前端上传，请使用{@link OssClient#uploadSignUrl(String, String, String, Integer)}
     * @param bucketName 存储空间
     * @param prefix objectKey 拼接前缀
     * @param objectKey  对象名称
     * @param bytes 字节数组
     * @param objectAcl  {@link CannedAccessControlList} 访问级别
     * @return 生成的objectKey
     */
    String uploadByBytes(String bucketName, String prefix, String objectKey, byte[] bytes, CannedAccessControlList objectAcl);

    /**
     * 获取文件预览地址-公共访问的，如果是私有访问的文件，则需要临时授权信息，用于前端授权访问{@link OssClient#signMessage(long)}
     *
     * @param bucketName 存储空间
     * @param objectKey 真实的存储对象名称
     * @return
     */
    String getObjectUrl(String bucketName, String objectKey);

    /**
     * 获取文件预览地址-公共访问的，如果是私有访问的文件，则需要临时授权信息，用于前端授权访问{@link OssClient#signMessage(long)}
     * @param endPoint 域名
     * @param bucketName 存储空间
     * @param objectKey 真实的存储对象名称
     * @return
     */
    String getObjectUrl(String endPoint, String bucketName, String objectKey);

    /**
     * 获取文件流,如果是前端访问，请使用{@link OssClient#getObjectUrl(String, String)}
     *
     * @param bucketName 存储空间
     * @param objectKey 真实的存储对象名称
     * @return
     */
    OssObjectInfo getObject(String bucketName, String objectKey);

    /**
     * 删除文件。可以直接删除，业务方自己考虑访问控制
     *
     * @param bucketName 存储空间
     * @param objectKey 真实的存储对象名称
     * @return
     */
    void deleteObject(String bucketName, String objectKey);

    /**
     * 设置文件的访问级别，可以直接设置，业务方自己考虑访问控制
     *
     * @param bucketName 存储空间
     * @param objectKey 真实的存储对象名称
     * @param objectAcl  {@link CannedAccessControlList} 访问级别
     */
    void setObjectAcl(String bucketName, String objectKey, CannedAccessControlList objectAcl);

    /**
     * 获取文件的访问控制信息
     *
     * @param bucketName 存储空间
     * @param objectKey 真实的存储对象名称
     * @return
     */
    ObjectAclInfo getObjectAcl(String bucketName, String objectKey);

    /**
     * 从sourceBucketName复制object到destinationBucketName
     *
     * @param sourceBucketName
     * @param sourceKey
     * @param destinationBucketName
     * @param destinationKey
     */
    void copyObject(String sourceBucketName, String sourceKey, String destinationBucketName,
                    String destinationKey);

}
