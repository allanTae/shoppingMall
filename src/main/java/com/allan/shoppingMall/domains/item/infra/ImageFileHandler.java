package com.allan.shoppingMall.domains.item.infra;

import com.allan.shoppingMall.domains.item.domain.ImageType;
import com.allan.shoppingMall.domains.item.domain.ItemImage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * MultipartFile 로 전달 된 파일 정보들로 이미지 파일을 처리하는 핸들러.
 * 현재, 2가지 기능을 수행한다.
 * 1. MultirpartFile 정보를 통해 애플리케이션 내 저장 하는 기능.
 * 2. itemImage 엔티티로 변환 후 반환하는 기능.(item 엔티티를 통해 itemImage 를 저장하기 위해 반환.)
 */

@Component
@Slf4j
public class ImageFileHandler {

    public List<ItemImage> parseImageInfo(List<MultipartFile> multipartFiles, ImageType imageType) throws IOException {

        // 반환 할 ItemImage Entity list.
        List<ItemImage> itemImages = new ArrayList<>();

        // 전달 된 파일 리스트가 비어 있는 경우.
        if(!CollectionUtils.isEmpty(multipartFiles)){

            // 파일명으로 사용 할 날짜 정보.
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            String currentDate = now.format(dateTimeFormatter);

            // 이미지를 저장 할 웹 애플리케이션 내 절대경로.
            // 운영체제 영향 받지 않도록 File.separator 사용.
            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            // 이미지를 저장 할 세부경로.
            String detailPath = "images" + File.separator + "item" + File.separator + currentDate;
            File imageFile = new File(detailPath);

            // 폴더 없으면 생성시도, 시도시 에러발생하면 에러로그.
            if(!imageFile.exists()){
                boolean isSuccessful = imageFile.mkdirs();
                if(!isSuccessful){
                    log.error("IamgeFileHandler's parseImageInfo() cause error!!");
                    log.error("file mkdir create is not successful");
                }
            }

            for(MultipartFile multipartFile : multipartFiles){
                String fileExtension =""; // 파일 확장자명.
                String contentType = multipartFile.getContentType(); // MEME TYPE.

                // 확장자명이 존재하지 않는 경우.
                if(ObjectUtils.isEmpty(contentType)){
                    break;
                }else{
                    if(contentType.contains("image/jpeg"))
                        fileExtension = ".jpeg";
                    else if(contentType.contains("image/png"))
                        fileExtension = ".png";
                    else{
                        log.error("IamgeFileHandler's parseImageInfo() cause error!!");
                        log.error("처리 할 수 없는 이미지 파일 형식입니다.");
                    }
                }

                // 저장 할 이미지 파일명.
                // 피일명 중복을 피하기 위해 시간정보 + 확장자명 형태로 이름을 정의.
                String newFileName = System.nanoTime() + fileExtension;

                ItemImage itemImage = ItemImage.builder()
                        .originalItemImageName(multipartFile.getOriginalFilename())
                        .itemImagePath(detailPath + File.separator + newFileName)
                        .imageSize(multipartFile.getSize())
                        .imageType(imageType)
                        .build();

                itemImages.add(itemImage);

                // 애플리케이션에 파일 저장.
                imageFile = new File(absolutePath + detailPath + File.separator + newFileName);
                multipartFile.transferTo(imageFile);

                // 이미지 파일 접근권한 설정.
                imageFile.setReadable(true);
                imageFile.setWritable(true);
            }
        }
        return itemImages;
    }
}
