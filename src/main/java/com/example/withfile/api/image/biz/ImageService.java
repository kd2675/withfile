package com.example.withfile.api.image.biz;

import com.example.withfile.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

@Service
@Slf4j
public class ImageService {

    final int[] PHOTO_HEIGHT = {200, 300, 600, 900};
    final int[] PHOTO_WIDTH = {200, 400, 800, 1200};

    @Value("${rootPath}")
    private String ROOT_PATH;
    @Value("${rootImagePath}")
    private String ROOT_IMAGE_PATH;

    public String saveImage(MultipartFile file){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c1 = Calendar.getInstance();

        String strToday = sdf.format(c1.getTime());
        String fileRealName = file.getOriginalFilename();
        String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."), fileRealName.length());
        String path = File.separator + strToday.substring(0,4) + File.separator + strToday.substring(4, 6) + File.separator + strToday.substring(6, 8);

        String folderPath = ROOT_IMAGE_PATH+path+File.separator;

        String uuid = UUID.randomUUID().toString();

        String uploadFileName = strToday + "_" + "_" + uuid.split("-")[0];

        log.error("파일 폴더 : {}", folderPath);
        log.error("업로드 파일 이름 : {}", uploadFileName);
        log.error("확장자명 : {}", fileExtension);

        File folder = new File(folderPath);

        if(!folder.exists()){
            try{
                log.info("폴더생성됨 : {}", folder.getPath());
                folder.mkdirs();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        File saveFile = new File(folderPath+uploadFileName+fileExtension);  // 적용 후

        try {
            for (int i = 0; i < 4; i++) {
                InputStream is = file.getInputStream();
                BufferedImage image200 = ImageUtils.resize(is,PHOTO_WIDTH[i],PHOTO_HEIGHT[i]);
                File file200 = new File(folderPath+uploadFileName+PHOTO_WIDTH[i]+PHOTO_HEIGHT[i]+fileExtension);
                boolean write = ImageIO.write(image200, fileExtension.substring(1), file200);
            }

            file.transferTo(saveFile);

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return uploadFileName+fileExtension;
    }

    public String updatePhoto(MultipartFile file, String deleteFileName){
        String substring = deleteFileName.substring(0, 8);
        String deletePath = File.separator + substring.substring(0,4) + File.separator + substring.substring(4, 6) + File.separator + substring.substring(6, 8);

        File deleteFile = new File(ROOT_IMAGE_PATH + deletePath + File.separator + deleteFileName);
        log.error("deleteFile = {}", deleteFile);
        deleteFile.delete();
        String path = saveImage(file);

        return path;
    }

    public ResponseEntity<Resource> showImage(String fileName) {
        System.out.println("image Test");

        Resource resource = new FileSystemResource(ROOT_PATH + ImageUtils.getPhotoUrl(fileName));

        if (!resource.exists())
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND);
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(ROOT_PATH + fileName);
            header.add("Content-type", Files.probeContentType(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }

}
