package com.example.withfile.api.image.act;

import com.example.withfile.api.image.biz.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    public final ImageService imageService;

    @RequestMapping("/ctf/upload")
    public Map<String, String> saveFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        HashMap<String, String> map = new HashMap<>();

        String fileName = imageService.saveImage(file);
        String url = "api" + File.separator + "image" + File.separator + "show" + File.separator + fileName;
        String pathUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + File.separator + url;

        map.put("fileName", fileName);
        map.put("pathUrl", pathUrl);

        return map;
    }

    @GetMapping(value = "/show/{imageName}", produces = {
            "image/bmp",
            "image/gif",
            "image/jpeg",
            "image/png",
            "image/svg+xml",
            "image/tiff",
            "image/webp"
    })
    public ResponseEntity<Resource> display(@PathVariable("imageName") String fileName) {
        ResponseEntity<Resource> responseEntity = imageService.showImage(fileName);
        return responseEntity;
    }

//    @GetMapping(value = "/image/show/{imageName}", produces = {
//            "image/bmp",
//            "image/gif",
//            "image/jpeg",
//            "image/png",
//            "image/svg+xml",
//            "image/tiff",
//            "image/webp"
//    })
//    public ResponseEntity<byte[]> userSearch(@PathVariable("imageName") String imageName) throws IOException {
//        System.out.println("image Test");
//        String photoUrl = ImageUtil.getPhotoUrl(imageName);
//        InputStream imageStream = new FileInputStream("C://" + photoUrl);
//        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
//        imageStream.close();
//
//        return new ResponseEntity<byte[]>(imageByteArray, HttpStatus.OK);
//    }


}
