package com.jenfer.controller;

import com.jenfer.annotation.GloballInterceptor;
import com.jenfer.config.WebConfig;
import com.jenfer.constants.Constants;
import com.jenfer.enums.ResponseCodeEnum;
import com.jenfer.enums.UserOperFrequencyTypeEnum;
import com.jenfer.exception.BusinessException;
import com.jenfer.utils.StringTools;
import com.jenfer.vo.ResponseVo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController extends ABaseController{

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Resource
    private WebConfig webConfig;

    @RequestMapping("/uploadImage")
    @GloballInterceptor(checkLogin = true,frequencyType = UserOperFrequencyTypeEnum.IMAGE_UPLAOD)
    public ResponseVo uploadImage(MultipartFile file){
        if(file==null){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String fileName = file.getOriginalFilename();
        String fileExtName = StringTools.getFileSuffix(fileName);
        if(!ArrayUtils.contains(Constants.IMAGE_SUFFIX,fileExtName)){
            throw new BusinessException(ResponseCodeEnum.CODE_600);
        }
        String path = copyFile(file);
        Map<String,String> fileMap = new HashMap<>();
        fileMap.put("fileName",path);
        return getSuccessResponseVo(fileMap);

    }

    public String copyFile(MultipartFile file){
        try {
            String fileName = file.getOriginalFilename();
            String fileExtName = StringTools.getFileSuffix(fileName);
            String fileRealName = StringTools.getRandomString(Constants.LENGTH_30)+fileExtName;
            String folderPath = webConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_TEMP;
            File folder = new File(folderPath);
           if(!folder.exists()){
               folder.mkdirs();
           }
           File uploadFile = new File(folderPath+"/"+fileRealName);
           file.transferTo(uploadFile);
           return Constants.FILE_FOLDER_TEMP_2+"/"+fileRealName;

        }catch (Exception e){
            logger.error("上传文件失败");
            throw new BusinessException("上传文件失败");
        }
    }

    @RequestMapping("/getImage/{imageFolder}/{imageName}")
    public void getImage(HttpServletResponse response, @PathVariable("imageFolder") String imageFolder,
                          @PathVariable("imageName") String imageName){
        readImage(response,imageFolder,imageName);
    }

    @RequestMapping("/getAvatar/{userId}")
    public void getAvatar(HttpServletResponse response, @PathVariable("userId") String userId){
        String avatarFolderName = Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_AVATAR_NAME;  // 显示头像的文件夹名称
        String avatarPath = webConfig.getProjectFolder()+avatarFolderName+userId+Constants.AVATAR_SUFFIX;  // 头像文件的路径
        File avatarFolder = new File(avatarFolderName);  // 头像文件夹对象
        if(!avatarFolder.exists()){  // 如果头像文件夹不存在
            avatarFolder.mkdirs();  // 创建头像文件夹
        }
        File file = new File(avatarPath);  // 头像文件对象
        String imageName = userId+Constants.AVATAR_SUFFIX;  // 头像图片名称
        if(!file.exists()){  // 如果头像文件不存在
            imageName = Constants.AVATAR_DEFAULT_NAME;  // 使用默认头像图片名称
        }
        readImage(response,Constants.FILE_FOLDER_AVATAR_NAME,imageName);  // 读取头像图片并发送响应

    }

    public void readImage(HttpServletResponse response, String imageFolder, String imageName){
        ServletOutputStream sos =null;  // 声明并初始化sos对象，用于输出响应内容
        FileInputStream in =null;  // 声明并初始化in对象，用于读取文件
        ByteArrayOutputStream baos = null;  // 声明并初始化baos对象，用于暂存文件内容
        try {
            if(StringTools.isEmpty(imageFolder) || StringTools.isEmpty(imageName)){  // 判断imageFolder和imageName是否为空
                return;  // 如果为空，则直接返回
            }
            // 获取后缀
            String imageSuffix = StringTools.getFileSuffix(imageName);
            //
            String filePath = webConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_IMAGE+
                    imageFolder+"/"+imageName;  // 构造文件路径
            if(Constants.FILE_FOLDER_TEMP_2.equals(imageFolder)||imageFolder.contains(Constants.FILE_FOLDER_AVATAR_NAME)){
                filePath = webConfig.getProjectFolder()+Constants.FILE_FOLDER_FILE+imageFolder+"/"+imageName;  // 如果imageFolder为 Constants.FILE_FOLDER_TEMP_2或者包含 Constants.FILE_FOLDER_AVATAR_NAME，则重新构造文件路径
            }
            File file = new File(filePath);  // 创建文件对象
            if(!file.exists()){  // 判断文件是否存在
                return;  // 如果文件不存在，则直接返回
            }
            imageSuffix = imageSuffix.replace(".","");  // 去除后缀的点号
            if(!Constants.FILE_FOLDER_AVATAR_NAME.equals(imageFolder)){  // 判断imageFolder是否等于 Constants.FILE_FOLDER_AVATAR_NAME
                response.setHeader("Cache-Control","max-age=2592000");  // 如果不等于，则设置响应头的Cache-Control字段
            }
            response.setContentType("image"+imageSuffix);  // 设置响应头的Content-Type字段
            in = new FileInputStream(file);  // 创建文件输入流，用于读取文件
            sos = response.getOutputStream();  // 获取响应输出流
            baos = new ByteArrayOutputStream();  // 创建字节数组输出流，用于暂存文件内容
            int ch = 0;
            while (-1 != (ch = in.read())){  // 循环读取文件内容
                baos.write(ch);  // 将读取的内容写入字节数组输出流
            }
            sos.write(baos.toByteArray());  // 将字节数组转换为字节流并写入响应输出流
        }catch (Exception e){
            logger.error("读取图片失败",e);  // 捕获异常并记录日志
        }finally {
            if(baos!=null){
                try {
                    baos.close();  // 关闭字节数组输出流
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(sos!=null){
                try {
                    baos.close();  // 关闭响应输出流
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            if(in!=null){
                try {
                    baos.close();  // 关闭文件输入流
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

    }


}
