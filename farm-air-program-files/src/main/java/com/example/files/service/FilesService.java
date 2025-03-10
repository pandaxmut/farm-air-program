package com.example.files.service;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.example.files.entity.Files;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class FilesService {
    public Files uploadFile(MultipartFile file) throws IOException {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region2());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
//...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
        String accessKey = "wcX5fhAyi3sZpAE4-nrshjLslZccuMKVtUKSzOmc";
        String secretKey = "lkla5xhjTuY2Zma_D18Gecbnn_HE5B7E2dnwJklR";
        String bucket = "farmairprogram1";

//默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        String path = getPathByTime();
        System.out.println(path);
        int onlyName = file.getBytes().hashCode();
        key = path+onlyName;
        try {

            byte[] uploadBytes = file.getBytes();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);

            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);
                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (UnsupportedEncodingException ex) {
            //ignore
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //TODO 把文件信息保存到数据库中，方便查询
        Files files = new Files();
        files.setFileName(String.valueOf(onlyName));
        files.setFilePath(key);
        files.setFileUrl(downloadFile(key));
        files.setCreateTime(LocalDateTime.now());
        files.setUpdateTime(LocalDateTime.now());
        System.out.println(files);
        //返回图片信息（id,url）
        return files;

    }

    public String downloadFile(String key) throws UnsupportedEncodingException {
        String fileName = key;
        String domainOfBucket = "http://ssqsismum.hn-bkt.clouddn.com";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
        String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
        String accessKey = "wcX5fhAyi3sZpAE4-nrshjLslZccuMKVtUKSzOmc";
        String secretKey = "lkla5xhjTuY2Zma_D18Gecbnn_HE5B7E2dnwJklR";
        Auth auth = Auth.create(accessKey, secretKey);
        long expireInSeconds = 3600;//1小时，可以自定义链接过期时间
        String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
        System.out.println(finalUrl);
        return finalUrl;
    }

    public String getPathByTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = simpleDateFormat.format(date);
        String path = "./"+dateString.replace("-","/")+"/";
        return path;
    }
}
