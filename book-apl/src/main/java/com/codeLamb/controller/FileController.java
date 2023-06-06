package com.codeLamb.controller;

import com.codeLamb.config.MinIOConfig;
import com.codeLamb.exceptions.GraceException;
import com.codeLamb.grace.result.GraceJSONResult;
import com.codeLamb.utils.MinIOUtils;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileFilter;
import java.io.IOException;

/**
 * @author codeLamb
 */
@RestController
@RequestMapping("/file")
@Api(tags = "FileController Minio 文件上传测试")
public class FileController {

    @Resource
    private MinIOConfig minIOConfig;

    @PostMapping("/upload")
    public GraceJSONResult upload(MultipartFile file) throws Exception {
        // 获取上传文件的名字
        String fileName = file.getOriginalFilename();

        MinIOUtils.uploadFile(minIOConfig.getBucketName(),
                fileName, file.getInputStream());

        String uri = minIOConfig.getEndpoint() + "/" + minIOConfig.getBucketName() + "/" + fileName;

        return GraceJSONResult.ok(uri);
    }

}
