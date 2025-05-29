package com.shop.book.api.common.service.impl;

import com.shop.book.api.common.repository.ImageRepository;
import com.shop.book.api.common.service.ImageService;
import com.shop.book.domain.common.domain.Image;
import com.shop.book.global.util.FileUploadUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequiredArgsConstructor
public class ImageServicecImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final FileUploadUtils fileUploadUtils;

    @Override
    public Image uploadImage(MultipartFile file) {
        // S3에 파일 업로드 후 결과를 받아옵니다.
        Map<String, String> uploadResult = FileUploadUtils.uploadFileToS3(file);

        // 업로드 결과를 바탕으로 ImageEntity 객체를 생성합니다.
        Image image = new Image();
        image.setFileName(uploadResult.get("fileName"));
        // 여기서 S3 URL이 설정됩니다.
        image.setFileUrl(uploadResult.get("url"));
        image.setFileType(uploadResult.get("fileType"));
        image.setFileSize(Long.parseLong(uploadResult.get("fileSize")));

        // 생성된 ImageEntity를 데이터베이스에 저장하고 반환합니다.
        // 여기서 DB에 저장됩니다.
        return imageRepository.save(image);
    }
}
