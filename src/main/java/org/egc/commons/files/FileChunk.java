package org.egc.commons.files;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * Description:
 * <pre>
 * 文件分块上传/断点续传中的文件块信息
 * 参考：https://github.com/simple-uploader/Uploader#configuration
 * </pre>
 *
 * @author houzhiwei
 * @date 2018/9/15 10:09
 */
@Data
public class FileChunk implements Serializable {

    private static final long serialVersionUID = 8995091133249093124L;

    private Long id;
    /**
     * 当前文件块，从1开始
     */
    private Integer chunkNumber;
    /**
     * 分块大小
     */
    private Long chunkSize;
    /**
     * 当前分块大小
     */
    private Long currentChunkSize;
    /**
     * 总的文件大小
     */
    private Long totalSize;
    /**
     * 文件标识
     */
    private String identifier;
    /**
     * original file name
     */
    private String filename;
    /**
     * 总分块数
     */
    private Integer totalChunks;

    private String relativePath;
    /**
     * 文件
     */
    private MultipartFile file;

}
