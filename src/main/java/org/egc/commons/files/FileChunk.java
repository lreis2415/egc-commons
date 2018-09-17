package org.egc.commons.files;


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

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for property 'chunkNumber'.
     *
     * @return Value for property 'chunkNumber'.
     */
    public Integer getChunkNumber() {
        return chunkNumber;
    }

    /**
     * Setter for property 'chunkNumber'.
     *
     * @param chunkNumber Value to set for property 'chunkNumber'.
     */
    public void setChunkNumber(Integer chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    /**
     * Getter for property 'chunkSize'.
     *
     * @return Value for property 'chunkSize'.
     */
    public Long getChunkSize() {
        return chunkSize;
    }

    /**
     * Setter for property 'chunkSize'.
     *
     * @param chunkSize Value to set for property 'chunkSize'.
     */
    public void setChunkSize(Long chunkSize) {
        this.chunkSize = chunkSize;
    }

    /**
     * Getter for property 'currentChunkSize'.
     *
     * @return Value for property 'currentChunkSize'.
     */
    public Long getCurrentChunkSize() {
        return currentChunkSize;
    }

    /**
     * Setter for property 'currentChunkSize'.
     *
     * @param currentChunkSize Value to set for property 'currentChunkSize'.
     */
    public void setCurrentChunkSize(Long currentChunkSize) {
        this.currentChunkSize = currentChunkSize;
    }

    /**
     * Getter for property 'totalSize'.
     *
     * @return Value for property 'totalSize'.
     */
    public Long getTotalSize() {
        return totalSize;
    }

    /**
     * Setter for property 'totalSize'.
     *
     * @param totalSize Value to set for property 'totalSize'.
     */
    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * Getter for property 'identifier'.
     *
     * @return Value for property 'identifier'.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Setter for property 'identifier'.
     *
     * @param identifier Value to set for property 'identifier'.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Getter for property 'filename'.
     *
     * @return Value for property 'filename'.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Setter for property 'filename'.
     *
     * @param filename Value to set for property 'filename'.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Getter for property 'totalChunks'.
     *
     * @return Value for property 'totalChunks'.
     */
    public Integer getTotalChunks() {
        return totalChunks;
    }

    /**
     * Setter for property 'totalChunks'.
     *
     * @param totalChunks Value to set for property 'totalChunks'.
     */
    public void setTotalChunks(Integer totalChunks) {
        this.totalChunks = totalChunks;
    }

    /**
     * Getter for property 'file'.
     *
     * @return Value for property 'file'.
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * Setter for property 'file'.
     *
     * @param file Value to set for property 'file'.
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    /**
     * Getter for property 'relativePath'.
     *
     * @return Value for property 'relativePath'.
     */
    public String getRelativePath() {
        return relativePath;
    }

    /**
     * Setter for property 'relativePath'.
     *
     * @param relativePath Value to set for property 'relativePath'.
     */
    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

}
