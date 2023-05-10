package com.demo.oss.util;


import static com.aliyun.oss.internal.OSSConstants.DEFAULT_OBJECT_CONTENT_TYPE;
import static com.demo.oss.common.OssConstants.*;


/**
 * oss工具类
 * @author licc3
 */
public class OssUtil {
    /**
     * 根据文件扩展名获取http content-type值
     *
     * @param fileExtName
     * @return
     */
    public static String getContentType(String fileExtName) {
        String fileExtension = fileExtName.substring(fileExtName.lastIndexOf("."));
        if (FILE_EXT_NAME_BMP.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_BMP;
        }
        if (FILE_EXT_NAME_GIF.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_GIF;
        }
        if (FILE_EXT_NAME_JPEG.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_JPEG;
        }
        if (FILE_EXT_NAME_JPG.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_JPG;
        }
        if (FILE_EXT_NAME_PNG.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_PNG;
        }
        if (FILE_EXT_NAME_HTML.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_HTML;
        }
        if (FILE_EXT_NAME_TXT.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_TXT;
        }
        if (FILE_EXT_NAME_VSD.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_VSD;
        }
        if (FILE_EXT_NAME_PPT.equalsIgnoreCase(fileExtension) || FILE_EXT_NAME_PPTX.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_PPT;
        }
        if (FILE_EXT_NAME_DOC.equalsIgnoreCase(fileExtension) || FILE_EXT_NAME_DOCX.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_DOC;
        }
        if (FILE_EXT_NAME_XML.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_XML;
        }
        if (FILE_EXT_NAME_AI.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_AI;
        }
        if (FILE_EXT_NAME_AIF.equalsIgnoreCase(fileExtension) || FILE_EXT_NAME_AIFC.equalsIgnoreCase(fileExtension) || ".aiff".equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_AIF;
        }
        if (FILE_EXT_NAME_BCPIO.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_BCPIO;
        }
        if (FILE_EXT_NAME_CSS.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_CSS;
        }
        if (FILE_EXT_NAME_EXE.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_EXE;
        }
        if (FILE_EXT_NAME_MP2.equalsIgnoreCase(fileExtension) || FILE_EXT_NAME_MP3.equalsIgnoreCase(fileExtension) || ".mpe".equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_MP2;
        }
        if (FILE_EXT_NAME_PDF.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_PDF;
        }
        if (FILE_EXT_NAME_TAR.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_TAR;
        }
        if (FILE_EXT_NAME_XLC.equalsIgnoreCase(fileExtension) || FILE_EXT_NAME_XLL.equalsIgnoreCase(fileExtension) ||
                FILE_EXT_NAME_XLM.equalsIgnoreCase(fileExtension) || FILE_EXT_NAME_XLS.equalsIgnoreCase(fileExtension) || FILE_EXT_NAME_XLw.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_XLC;
        }
        if (FILE_EXT_NAME_XPM.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_XPM;
        }
        if (FILE_EXT_NAME_XWD.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_XWD;
        }
        if (FILE_EXT_NAME_ZIP.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_ZIP;
        }
        if (FILE_EXT_NAME_XLSX.equalsIgnoreCase(fileExtension)) {
            return CONTENT_TYPE_XLSX;
        }
        return DEFAULT_OBJECT_CONTENT_TYPE;
    }

    /**
     * 获取文件预览地址
     * @param endPoint
     * @param bucketName
     * @param objectKey
     * @return
     */
    public static String combObjectUrl(String endPoint, String bucketName, String objectKey) {
        String endpoint = endPoint.replace("http://", "");
        if (!objectKey.startsWith("/")) {
            objectKey = "/" + objectKey;
        }
        String objectUrl = "https://" + bucketName + "." + endpoint + objectKey;
        return objectUrl;
    }
}
