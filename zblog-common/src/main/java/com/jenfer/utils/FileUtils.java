package com.jenfer.utils;

import com.jenfer.config.AppConfig;
import com.jenfer.constants.Constants;
import com.jenfer.dto.FileUploadDto;
import com.jenfer.enums.FileUploadTypeEnum;
import com.jenfer.exception.BusinessException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class FileUtils {


    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    @Resource
    private AppConfig appConfig;

    @Resource
    private ImageUtils imageUtils;

    public FileUploadDto uploadFile2Local(MultipartFile file, String folder, FileUploadTypeEnum uploadTypeEnum){

        try {
            FileUploadDto fileUploadDto = new FileUploadDto();
            String originalFileName = file.getOriginalFilename();
            String fileSuffix = StringTools.getFileSuffix(originalFileName);
            //防止名字太长
            if(originalFileName.length()> Constants.LENGTH_200){
                originalFileName = StringTools.getFileName(originalFileName).substring(0,Constants.LENGTH_190)+fileSuffix;
            }
            if(!ArrayUtils.contains(uploadTypeEnum.getSuffixArray(),fileSuffix)){
                throw new BusinessException("文件格式不正确");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
            String month = simpleDateFormat.format(new Date());
            String baseFolder = appConfig.getProjectFolder() + Constants.FILE_FOLDER_FILE ;
            //如果是文件类型的话就加/attachment前缀
            File targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_ATTACHMENT+month+"/");
            String fileName = StringTools.getRandomString(Constants.LENGTH_15)+fileSuffix;
            File targetFile = new File(targetFileFolder.getPath()+"/"+fileName);
            String localPath = month + "/" +fileName;

            if(uploadTypeEnum==FileUploadTypeEnum.AVATAR){
                // 头像上传
                targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_AVATAR_NAME);
                targetFile = new File(targetFileFolder.getPath()+"/"+folder+Constants.AVATAR_SUFFIX);
                localPath = folder+Constants.AVATAR_SUFFIX;
            }
            if(uploadTypeEnum==FileUploadTypeEnum.ARTICLE_COVER){
                //文章封面图片上传
                targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_IMAGE+month+"/");
                 targetFile = new File(targetFileFolder.getPath()+"/"+fileName);
                 localPath = month + "/" +fileName;
            }

            if(!targetFileFolder.exists()){
                targetFileFolder.mkdirs();
            }
            file.transferTo(targetFile);
            //压缩图片
            if(uploadTypeEnum==FileUploadTypeEnum.COMMEMT_IMAGE){
                targetFileFolder = new File(baseFolder+Constants.FILE_FOLDER_IMAGE+month+"/");
                String thumbnailName  = targetFile.getName().replace(".","_.");
                File thumbnail = new File(targetFileFolder.getPath()+"/"+thumbnailName);
                Boolean thumbailCreated = imageUtils.createThumbnail(targetFile,Constants.LENGTH_200,Constants.LENGTH_200,thumbnail);
                if(!thumbailCreated){
                    org.apache.commons.io.FileUtils.copyFile(targetFile,thumbnail);
                }
            }else if(uploadTypeEnum==FileUploadTypeEnum.ARTICLE_COVER || uploadTypeEnum==FileUploadTypeEnum.AVATAR){
                imageUtils.createThumbnail(targetFile,Constants.LENGTH_200,Constants.LENGTH_200,targetFile);
            }
            fileUploadDto.setLocalPath(localPath);
            fileUploadDto.setOriginalFileName(originalFileName);
            return fileUploadDto;

        }catch (BusinessException e){
            throw e;
        }catch (Exception e){
            logger.error("文件上传失败",e);
            throw new BusinessException("文件上传失败");
        }


    }



}
