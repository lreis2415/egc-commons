package org.egc.commons.files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by lp on 2017/5/26.
 */
public class FilesOperation {

    private static final Logger logger = LoggerFactory.getLogger(FilesOperation.class);

    public static long copyFile(String srcFilePath, String destDirPath, String destFileName){
        long copySizes = 0;
        File srcFile = new File(srcFilePath);
        File destDir = new File(destDirPath);
        if (!srcFile.exists()) {
            copySizes = -1;
        } else if (!destDir.exists()) {
            copySizes = -1;
        } else if (destFileName == null) {
            copySizes = -1;
        } else {
            try {
                FileChannel fcin = new FileInputStream(srcFile).getChannel();
                FileChannel fcout = new FileOutputStream(new File(destDir, destFileName)).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, fcin.size(), fcout);
                fcin.close();
                fcout.close();
                copySizes = size;
            } catch (FileNotFoundException e) {
                logger.error("Copy File Operation fail, because the file to copy can not be found", e);
            } catch (IOException e) {
                logger.error("Copy File Operation fail, because can not write", e);
            }
        }
        return copySizes;
    }

    public static void deleteDiskFile(String path) {
        File deleteFile = new File(path);
        if(deleteFile.isFile() && deleteFile.exists())
        {
            deleteFile.delete();
        }
    }

    public static void deleteDiskFolder(File file)
    {
        if (file.exists())
        {
            if (file.isFile())
            {
                file.delete();
            }
            else if (file.isDirectory())
            {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++)
                {
                    deleteDiskFolder(files[i]);
                }
            }
            file.delete();
        }
    }

}
