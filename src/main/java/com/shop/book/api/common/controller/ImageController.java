package com.shop.book.api.common.controller;

import com.shop.book.api.common.service.ImageService;
import com.shop.book.domain.common.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {

        // 파일이 비어있는지 확인
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }
        // 파일 타입이 PNG인지 확인
        if (!file.getContentType().equals("image/png")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Only PNG files are allowed"));
        }
        // 이미지 서비스를 통해 s3 파일 업로드 및 저장
        Image savedImage = imageService.uploadImage(file);

        return ResponseEntity.ok(Map.of("id", savedImage.getId().toString(), "url", savedImage.getFileUrl()));
    }
}
