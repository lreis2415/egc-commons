package org.egc.commons.files;

import org.egc.commons.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileUpload {

  private static final Logger logger = LoggerFactory.getLogger(FileUpload.class);

  public String uploadFile(String rootFilePath, Integer userId, Integer datasetId,String fileName, MultipartFile file) throws IOException {
    String filePath = rootFilePath + userId.toString() + "/" + datasetId + "/";
    String storeName = getFilePath(fileName, file);

    //filePath = filePath + storeName;
    if (!file.isEmpty()) {
      File tempFile = new File(filePath, storeName);
      if (!tempFile.getParentFile().exists()) {
        if(tempFile.getParentFile().mkdirs()){
          logger.info(tempFile.getParentFile().toString() + " do not exist, and create a folder");
        }else{
          logger.info(tempFile.getParentFile().toString() + "  create folder error");
        }
      }
      if (!tempFile.exists()) {
        tempFile.createNewFile();
        logger.info(tempFile + " do not exist, crate a new one");
      }
      file.transferTo(tempFile);
    }
    String relativePath =  userId.toString() + "/" + datasetId + "/" + storeName;
    return relativePath;

  }

  public String getFilePath(String fileName, MultipartFile file){
    String originalFilename = file.getOriginalFilename();
    String[] splitArray = StringUtil.stringSplit(originalFilename, "\\.");
    String filePostfix = splitArray[splitArray.length - 1];
    String storeName = fileName + "." + filePostfix;
    return storeName;
  }


}
