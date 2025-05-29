package com.shop.book.global.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FileUploadUtils {


    public static Map<String, String> uploadFileToS3(MultipartFile file) {
        return null;
    }
}
