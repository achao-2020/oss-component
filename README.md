# 文件SDK组件设计

## 1.设计背景##

好用的文件SDK能提供开箱即用的功能，让使用者不用关系系统配置，按照封装好的方法便可以直接使用。或者有其他OSS提供方，也能够快速重写相关类，达到快速接入的目的。

该文章，基于阿里云oss的基础，设计文件SDK。



## 2.功能清单

| 功能   | 描述                  |
|:----:|:-------------------:|
| 文件上传 | 通过文件流上传文件           |
|      | 通过字节数组上传文件          |
| 文件下载 | 通过objectKey获得文件下载地址 |
|      | 通过objectKey获得文件流    |
| 授权   | 获得临时授权信息            |
|      | 获得临时上次授权URL         |
| 访问控制 | 更改文件访问控制权限          |
|      | 获得文件访问控制            |
|      | 文件删除                |



## 3.功能设计

### 3.1 整体设计

![](E:\review-project\oss-component\png\Snipaste_2023-05-10_16-59-07.png)







### 3.2 类图

![](E:\review-project\oss-component\png\Snipaste_2023-05-10_18-01-11.png)



### 3.3 接口设计

```java
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

```
