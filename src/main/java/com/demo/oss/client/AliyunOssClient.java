package com.demo.oss.client;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.internal.OSSHeaders;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.demo.oss.auto.config.AliyunOssProperties;
import com.demo.oss.info.GenerateSignUrlInfo;
import com.demo.oss.info.OssObjectInfo;
import com.demo.oss.info.OssSignInfo;
import com.demo.oss.info.ObjectAclInfo;
import com.demo.oss.util.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 阿里云oss实现类
 *
 * @author achao
 */
@Slf4j
public class AliyunOssClient implements OssClient {

    private OSS ossClient;

    private AliyunOssProperties ossProperties;

    @Value("${spring.cloud.nacos.config.group}")
    private String prefix;

    public AliyunOssClient(OSS ossClient, AliyunOssProperties ossProperties) {
        this.ossClient = ossClient;
        this.ossProperties = ossProperties;
    }

    @Override
    public OssSignInfo signMessage(long durationSeconds) {
        return signMessage(ossProperties.getRoleName(), durationSeconds);
    }

    @Override
    public OssSignInfo signMessage(String roleSessionName, long durationSeconds) {
        DefaultProfile profile = DefaultProfile.getProfile(ossProperties.getRegionId(), ossProperties.getAccessKeyId(), ossProperties.getAccessKeySecret());
        DefaultAcsClient client = new DefaultAcsClient(profile);
        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysRegionId(ossProperties.getRegionId());
        request.setSysMethod(MethodType.POST);
        request.setRoleArn(ossProperties.getRoleArn());
        request.setRoleSessionName(ossProperties.getRoleName());
        request.setDurationSeconds(durationSeconds);
        request.setRoleSessionName(roleSessionName);
        try {
            AssumeRoleResponse acsResponse = client.getAcsResponse(request);
            OssSignInfo ossSignInfo = convertSignInfo(acsResponse);
            ossSignInfo.setEndpoint(ossProperties.getEndpoint());
            return ossSignInfo;
        } catch (Exception e) {
            log.error("获取临时访问token失败!", e);
        }
        return null;
    }

    @Override
    public GenerateSignUrlInfo uploadSignUrl(String bucketName, String objectKey, String fileExtName, Integer durationSeconds) {
        return uploadSignUrl(bucketName, prefix, objectKey, fileExtName, durationSeconds);
    }

    @Override
    public GenerateSignUrlInfo uploadSignUrl(String bucketName, String prefix, String objectKey, String fileExtName, Integer durationSeconds) {
        log.debug("method: uploadSignUrl, objectKey:" + objectKey);
        // 获取临时授权token
        OssSignInfo ossSignInfo = signMessage(durationSeconds);
        OSS signClient = null;
        try {
            signClient = new OSSClientBuilder().build(ossSignInfo.getEndpoint(), ossSignInfo.getAccessKeyId(), ossSignInfo.getAccessKeySecret(), ossSignInfo.getSecurityToken());
            // 设置请求头。
            Map<String, String> headers = new HashMap<String, String>();
            String contentType = null;
            if (StringUtils.isNotEmpty(fileExtName)) {
                contentType = OssUtil.getContentType(fileExtName);
                headers.put(OSSHeaders.CONTENT_TYPE, contentType);
            }
            URL signedUrl = null;
            // 指定生成的签名URL过期时间，单位为毫秒。
            Date expiration = new Date(System.currentTimeMillis() + durationSeconds * 1000);
            // 生成签名URL。
            GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, combKey(prefix, objectKey), HttpMethod.PUT);
            // 设置过期时间。
            request.setExpiration(expiration);
            // 将请求头加入到request中。
            request.setHeaders(headers);
            // 通过HTTP PUT请求生成签名URL。
            signedUrl = ossClient.generatePresignedUrl(request);
            // 拼装返回信息
            GenerateSignUrlInfo generateSignUrlInfo = new GenerateSignUrlInfo();
            generateSignUrlInfo.setSignUrl(signedUrl.toString());
            generateSignUrlInfo.setContentType(contentType);
            generateSignUrlInfo.setExpiration(ossSignInfo.getExpiration());
            generateSignUrlInfo.setObjectKey(combKey(prefix, objectKey));
            generateSignUrlInfo.setAccessKeyId(ossSignInfo.getAccessKeyId());
            generateSignUrlInfo.setAccessKeySecret(ossSignInfo.getAccessKeySecret());
            generateSignUrlInfo.setSecurityToken(ossSignInfo.getSecurityToken());
            generateSignUrlInfo.setEndpoint(ossSignInfo.getEndpoint());
            log.debug("生成授权url信息：" + generateSignUrlInfo.toString());
            return generateSignUrlInfo;
        } catch (Exception e) {
            log.error("获取临时授权url失败", e);
        } finally {
            signClient.shutdown();
        }
        return null;
    }

    @Override
    public GenerateSignUrlInfo previewSignUrl(String bucketName, String objectKey, Integer durationSeconds) {
        // 获取临时授权token
        OssSignInfo ossSignInfo = signMessage(durationSeconds);
        OSS ossClient = new OSSClientBuilder().build(ossSignInfo.getEndpoint(), ossSignInfo.getAccessKeyId(), ossSignInfo.getAccessKeySecret(), ossSignInfo.getSecurityToken());

        try {
            // 设置签名URL过期时间，单位为毫秒。
            Date expiration = new Date(System.currentTimeMillis() + durationSeconds * 1000);
            // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
            URL url = ossClient.generatePresignedUrl(bucketName, objectKey, expiration);
            // 拼装返回信息
            GenerateSignUrlInfo generateSignUrlInfo = new GenerateSignUrlInfo();
            generateSignUrlInfo.setSignUrl(url.toString());
            generateSignUrlInfo.setExpiration(ossSignInfo.getExpiration());
            generateSignUrlInfo.setContentType(OssUtil.getContentType(objectKey));
            generateSignUrlInfo.setObjectKey(objectKey);
            generateSignUrlInfo.setAccessKeyId(ossSignInfo.getAccessKeyId());
            generateSignUrlInfo.setAccessKeySecret(ossSignInfo.getAccessKeySecret());
            generateSignUrlInfo.setSecurityToken(ossSignInfo.getSecurityToken());
            generateSignUrlInfo.setEndpoint(ossSignInfo.getEndpoint());
            log.debug("生成授权url信息：" + generateSignUrlInfo.toString());
            return generateSignUrlInfo;
        } catch (Exception e) {
            log.error("获取临时授权url失败", e);
        } finally {
            ossClient.shutdown();
        }
        return null;
    }

    @Override
    public String uploadByStream(String bucketName, String objectKey, InputStream fileStream, CannedAccessControlList objectAcl) {
        return uploadByStream(bucketName, prefix, objectKey, fileStream, objectAcl);
    }

    @Override
    public String uploadByStream(String bucketName, String prefix, String objectKey, InputStream fileStream, CannedAccessControlList objectAcl) {
        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, combKey(prefix, objectKey), fileStream);
        if (Objects.nonNull(objectAcl)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setObjectAcl(objectAcl);
            objectRequest.setMetadata(metadata);
        }
        ossClient.putObject(objectRequest);
        return combKey(prefix, objectKey);
    }

    @Override
    public String uploadByBytes(String bucketName, String objectKey, byte[] bytes, CannedAccessControlList objectAcl) {
        return uploadByBytes(bucketName, prefix, objectKey, bytes, objectAcl);
    }

    @Override
    public String uploadByBytes(String bucketName, String prefix, String objectKey, byte[] bytes, CannedAccessControlList objectAcl) {
        PutObjectRequest objectRequest = new PutObjectRequest(bucketName, combKey(prefix, objectKey), new ByteArrayInputStream(bytes));
        if (Objects.nonNull(objectAcl)) {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setObjectAcl(objectAcl);
            objectRequest.setMetadata(metadata);
        }
        ossClient.putObject(objectRequest);
        return combKey(prefix, objectKey);
    }

    @Override
    public String getObjectUrl(String bucketName, String objectKey) {
        return getObjectUrl(ossProperties.getEndpoint(), bucketName, objectKey);
    }

    @Override
    public String getObjectUrl(String endPoint, String bucketName, String objectKey) {
        return OssUtil.combObjectUrl(endPoint, bucketName, objectKey);
    }

    @Override
    public OssObjectInfo getObject(String bucketName, String objectKey) {
        OSSObject ossObject = ossClient.getObject(bucketName, objectKey);
        return convertObjectInfo(ossObject);
    }

    private OssObjectInfo convertObjectInfo(OSSObject ossObject) {
        OssObjectInfo ossObjectInfo = new OssObjectInfo();
        ossObjectInfo.setObjectKey(ossObject.getKey());
        ossObjectInfo.setBucketName(ossObject.getBucketName());
        ossObjectInfo.setMetadata(ossObject.getObjectMetadata());
        ossObjectInfo.setObjectContent(ossObject.getObjectContent());
        return ossObjectInfo;
    }

    @Override
    public void deleteObject(String bucketName, String objectKey) {
        log.debug("method: deleteObject, objectKey:" + objectKey);
        ossClient.deleteObject(bucketName, objectKey);
    }

    @Override
    public void setObjectAcl(String bucketName, String objectKey, CannedAccessControlList objectAcl) {
        log.debug("method: setObjectAcl, objectKey:" + objectKey);
        ossClient.setObjectAcl(bucketName, objectKey, objectAcl);
    }

    @Override
    public ObjectAclInfo getObjectAcl(String bucketName, String objectKey) {
        log.debug("method: getObjectAcl, objectKey:" + objectKey);
        ObjectAcl objectAcl = ossClient.getObjectAcl(bucketName, objectKey);
        return convertObjectAclInfo(objectAcl);
    }

    @Override
    public void copyObject(String sourceBucketName, String sourceKey, String destinationBucketName, String destinationKey) {
        ossClient.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
    }


    public String combKey(String prefix, String objectKey) {
        if (!objectKey.startsWith("/")) {
            objectKey = "/" + objectKey;
        }
        return prefix + "/" + LocalDate.now().toString() + objectKey;
    }

    private OssSignInfo convertSignInfo(AssumeRoleResponse acsResponse) {
        OssSignInfo ossSignInfo = new OssSignInfo();
        if (acsResponse.getCredentials() != null) {
            ossSignInfo.setAccessKeyId(acsResponse.getCredentials().getAccessKeyId());
            ossSignInfo.setAccessKeySecret(acsResponse.getCredentials().getAccessKeySecret());
            ossSignInfo.setSecurityToken(acsResponse.getCredentials().getSecurityToken());
            ossSignInfo.setExpiration(acsResponse.getCredentials().getExpiration());
        }

        return ossSignInfo;
    }

    private ObjectAclInfo convertObjectAclInfo(ObjectAcl objectAcl) {
        ObjectAclInfo objectAclInfo = new ObjectAclInfo();
        objectAclInfo.setPermission(objectAcl.getPermission().toString());
        objectAclInfo.setVersionId(objectAcl.getVersionId());
        return objectAclInfo;
    }

}
