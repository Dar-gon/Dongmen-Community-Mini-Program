package com.community.controller;

import com.community.util.MinioUtil;
import com.community.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
@Tag(name = "文件管理", description = "文件上传相关接口")
public class FileController {

    private final MinioUtil minioUtil;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    public Result<String> upload(@RequestParam MultipartFile file,
                                 @RequestParam(defaultValue = "common") String directory) {
        String url = minioUtil.uploadFile(file, directory);
        return Result.success(url);
    }
}
