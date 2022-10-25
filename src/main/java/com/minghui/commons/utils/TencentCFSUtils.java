package com.minghui.commons.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 腾讯云CFS的工具类
 * @Author minghui
 * @Date 2021-07-20
 */
public class TencentCFSUtils {

    private static final String secretId = "";
    private static final String secretKey = "";
    private static final String localHost = "";
    private static final String address = "ap-shanghai";
    private static final String bucketName = "";

    /**
     * 上传文件到存储桶，base64加密
     * @param base64
     * @return String 图片的存储地址
     * @throws IOException
     */
    public static String uploadFileParseBase64(String folder,String base64){
        MultipartFile multipartFile = null;
        File file = null;
        try {
            //将base64转换为multipartFile
            multipartFile = base64ToMultipartFile(base64);
            //将multipartFile转换为file
            file = multipartFileToFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //CFS的基本配置
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(address);
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);
        //确定CFS的存储路径及名称
        String key = "";
        if (file != null) {
            System.out.println(multipartFile.getOriginalFilename());
            key = folder + "/" + UUID.randomUUID() + ".png";
        }
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        return localHost + key;
    }


    /**
     * 将multipartFile转换为file
     * @param multipartFile
     * @return File
     * @throws IOException
     */
    public static File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = File.createTempFile("tmp",null);
        multipartFile.transferTo(file);
        file.deleteOnExit();
        return file;
    }

        /**
         * base64转multipartFile
         * @param base64
         * @return MultipartFile
         */
        public static MultipartFile base64ToMultipartFile(String base64) throws IOException {
            String[] baseStrs = base64.split(",");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            try {
                b = decoder.decodeBuffer(baseStrs[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new Base64DecodeMultipartFile(b, baseStrs[0]);
        }
}
