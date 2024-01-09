package com.jenfer.utils;

import com.jenfer.config.AppConfig;
import com.jenfer.constants.Constants;
import jakarta.annotation.Resource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component("imageUtils")
public class ImageUtils {

    private static final Logger logger = LoggerFactory.getLogger(ImageUtils.class);
    @Resource
    private AppConfig appConfig;

    public static Boolean createThumbnail(File file, int thumbnailWidth, Integer thumbnailHeight,File targetFile) {
        try{
            BufferedImage src  = ImageIO.read(file);
            int sorceW = src.getWidth();
            int sorceH = src.getHeight();
            if(sorceW<=thumbnailWidth){
                return false;
            }
            int height = sorceH;
            if(sorceW>thumbnailWidth){
                height = thumbnailHeight * sorceH / sorceW;
            }else {
                thumbnailWidth = sorceW;
                height = sorceH;
            }
            BufferedImage dst = new BufferedImage(thumbnailWidth, height, BufferedImage.TYPE_INT_RGB);
            Image scaleImage = src.getScaledInstance(thumbnailWidth, height, Image.SCALE_SMOOTH);
            Graphics2D g = dst.createGraphics();
            g.drawImage(scaleImage, 0, 0, thumbnailWidth,height, null);
            g.dispose();
            int resultH = dst.getHeight();
            if(resultH > thumbnailHeight){
                resultH = thumbnailHeight;
                dst = dst.getSubimage(0, 0, thumbnailWidth, resultH);
            }
            ImageIO.write(dst, "JPEG", targetFile);
            return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public String restImageHtml(String html){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        Date date = new Date();
        String format = simpleDateFormat.format(date);
        List<String> imageList = getImageList(html);
        for (String img :imageList){
            resetImage(img,format);
        }
        return format;
    }


    private String resetImage(String imagePath,String month){
        if(StringTools.isEmpty(imagePath)||!imagePath.contains(Constants.FILE_FOLDER_TEMP_2)){
            return imagePath;
        }
        imagePath = imagePath.replace(Constants.READ_IMAGE_PATH,"");
        if(StringTools.isEmpty(month)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
            Date date = new Date();
            month = simpleDateFormat.format(date);
        }
        String imageFileName = imagePath.replace(Constants.FILE_FOLDER_TEMP_2,month+"/");
        File targetFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + Constants.FILE_FOLDER_IMAGE + imageFileName);
        try {
            File tempFile = new File(appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE + imagePath);
            FileUtils.copyFile(tempFile,targetFile);
//            tempFile.delete();
        } catch (IOException e) {
            logger.error("复制图片失败",e);
            return imagePath;
        }
        return imageFileName;
    }
    public List<String> getImageList(String html){
        List<String> imageList = new ArrayList<>();
        String regEx_img = "(<img.*src\\s*=\\s*(.*?)[^>]*?>)";
        Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        Matcher m_image = p_image.matcher(html);
        while (m_image.find()) {
            String img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()){
                String imageUrl = m.group(1);
                imageList.add(imageUrl);
            }
        }

        return imageList;

    }

}
