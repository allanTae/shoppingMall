package com.allan.shoppingMall.domains.item.presentation;

import com.allan.shoppingMall.common.exception.ErrorCode;
import com.allan.shoppingMall.common.exception.ItemImageNotFoundException;
import com.allan.shoppingMall.domains.item.domain.ItemImage;
import com.allan.shoppingMall.domains.item.domain.ItemImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemImageController {

    private final ItemImageRepository itemImageRepository;

    /**
     * ItemImage 반환하는 메소드.
     */
    @GetMapping(
            value = "/image/{imageId}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
    )
    public ResponseEntity<byte[]> getImage(@PathVariable("imageId") Long imageId) {
        ItemImage itemImage = itemImageRepository.findById(imageId)
                .orElseThrow(() -> new ItemImageNotFoundException("이미지가 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String imagePath = itemImage.getItemImagePath();

        InputStream imageStream =null;
        try{
            imageStream = new FileInputStream(absolutePath + imagePath);
            byte[] imageByteArray = IOUtils.toByteArray(imageStream);

            return new ResponseEntity<>(imageByteArray, HttpStatus.OK);
        }catch(IOException exception){
            log.error("ItemImageController's getImage() cause error");
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }finally {
            try{
                if(imageStream != null)
                    imageStream.close();
            }catch (IOException exception){
                log.error(exception.getMessage());
                exception.printStackTrace();
            }
        }

    }

    /**
     * 이미지를 반환하는 것을 stream 으로 출력하는 메소드.
     * 나중에 성능 테스트를 해보자.
     */
    @GetMapping(
            value = "/image/stream/{imageId}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE}
    )
    public void getImageStream(@PathVariable("imageId") Long imageId, HttpServletResponse response) {
        ItemImage itemImage = itemImageRepository.findById(imageId)
                .orElseThrow(() -> new ItemImageNotFoundException("이미지가 존재하지 않습니다.", ErrorCode.ENTITY_NOT_FOUND));

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;
        String imagePath = itemImage.getItemImagePath();

        InputStream imageStream = null;
        OutputStream out = null;
        try{
            imageStream = new FileInputStream(absolutePath + imagePath);
            out = response.getOutputStream();
            FileCopyUtils.copy(imageStream, out);

            Path filePath = new File(absolutePath + imagePath).toPath();
            String mimeType = Files.probeContentType(filePath);
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(mimeType);
        }catch(IOException exception){
            log.error("ItemImageController's getImage() cause error");
        }finally {
            if(imageStream != null){
                try{
                    imageStream.close();
                }catch (IOException exception){
                    log.error(exception.getMessage());
                    exception.printStackTrace();
                }
            }
            try{
                out.close();
            }catch (IOException exception){
                log.error(exception.getMessage());
                exception.printStackTrace();
            }
        }

    }
}
