package com.example.withfile.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {
    public static BufferedImage resize(InputStream inputStream, int width, int height)
            throws IOException {
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage = ImageIO.read(inputStream);

        BufferedImage outputImage =
                new BufferedImage(width, height, bufferedImage.getType());

        Graphics2D graphics2D = outputImage.createGraphics();
        graphics2D.drawImage(bufferedImage, 0, 0, width, height, null);
        graphics2D.dispose();

        return outputImage;
    }

    public static String getPhotoUrl(String fileUrl) {
        if (fileUrl != null) {
            String imageName = fileUrl;
            if (imageName.split("_")[0].length() == 8) {
                String s = imageName.split("_")[0];
                String path = File.separator + s.substring(0, 4) + File.separator + s.substring(4, 6) + File.separator + s.substring(6, 8);
                String url = "image" + path + File.separator + imageName;
//                listVO.setMainImageName("/image"+path + File.separator + mainImageName);
                return url;
            }
        }
        return null;
    }
//
//    private static void getPhotoUrl()
}
