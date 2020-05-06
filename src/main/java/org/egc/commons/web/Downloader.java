package org.egc.commons.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.egc.commons.exception.BusinessException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

/**
 * download data from url
 * https://www.baeldung.com/java-download-file
 * https://www.stackabuse.com/how-to-download-a-file-from-a-url-in-java/
 *
 * @author houzhiwei
 * @date 2020 /5/5 0:21
 */
@Slf4j
public class Downloader {
    static int CONNECTION_TIMEOUT = 60000;
    static int READ_TIMEOUT = 60000;
    String error = "Download file from %s failed";

    public void download(String fileUrl, String saveDir) {
        download(fileUrl, saveDir, null);
    }

    public void download(String fileUrl, String saveDir, String filename) {
        filename = getSaveFilename(saveDir, fileUrl, filename);
        try {
            FileUtils.copyURLToFile(new URL(fileUrl), new File(filename), CONNECTION_TIMEOUT, READ_TIMEOUT);
        } catch (IOException e) {
            log.error(String.format(error, fileUrl), e);
            throw new BusinessException(e, String.format(error, fileUrl));
        }
    }

    public void downloadBigData(String fileUrl, String saveDir) {
        downloadBigData(fileUrl, saveDir, null);
    }

    public void downloadBigData(String fileUrl, String saveDir, String filename) {
        URL url = null;
        filename = getSaveFilename(saveDir, fileUrl, filename);
        try {
            url = new URL(fileUrl);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(filename);
            FileChannel fileChannel = fileOutputStream.getChannel();
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            log.error(String.format(error, fileUrl), e);
            throw new BusinessException(e, String.format(error, fileUrl));
        }
    }

    /**
     * Download restful data.
     *
     * @param restUrl restful service url
     * @param saveDir e.g., PathUtil.getProjectRoot()
     * @throws IOException the io exception
     */
    public void downloadRestfulData(URI restUrl, String saveDir) throws IOException {
        downloadRestfulData(restUrl, saveDir, null);
    }

    /**
     * Download restful data.
     *
     * @param restUrl  restful service url
     * @param saveDir  PathUtil.getProjectRoot() + name
     * @param filename the optional filename. Extension is unnecessary
     * @throws IOException the io exception
     */
    public void downloadRestfulData(URI restUrl, String saveDir, String filename) throws IOException {
        long start = System.currentTimeMillis();

        HttpPost httpPost = new HttpPost(restUrl);

        CloseableHttpClient closeableHttpClient = HttpClients.custom()
                .setConnectionManager(HttpUtils.getPoolingConnMgr()).build();
        int timeout = 5 * 60 * 1000;
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout)
                .setConnectTimeout(timeout).setConnectionRequestTimeout(30000).build();
        httpPost.setConfig(requestConfig);

        CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpPost);
        byte[] bytes = EntityUtils.toByteArray(httpResponse.getEntity());

        String tempFilename = getFilename(httpResponse);
        if (StringUtils.isBlank(filename)) {
            filename = tempFilename;
        } else {
            filename = filename + FilenameUtils.getExtension(tempFilename);
        }
        String saveFile = FilenameUtils.normalize(saveDir + File.separator + filename);
        FileUtils.writeByteArrayToFile(new File(saveFile), bytes);

        long time = System.currentTimeMillis() - start;
        log.debug("Time used: {}", time);
        closeableHttpClient.close();
        httpResponse.close();
    }

    //----------------------------------------------------------------------

    private String getFilename(CloseableHttpResponse response) {
        String filename = "";
        String dispositionValue = response.getFirstHeader("Content-Disposition").getValue();
        System.out.println(dispositionValue);
        int index = dispositionValue.indexOf("filename=");
        if (index > 0) {
            filename = dispositionValue.substring(index + 10, dispositionValue.length() - 1);
        }
        return filename;
    }

    /**
     * extracts file name from URL <br/>
     * 适用于直接的文件网址，如
     * https://dds.cr.usgs.gov/srtm/version2_1/SRTM3/Australia/S11E121.hgt.zip
     *
     * @param fileUrl file url
     * @return filename
     */
    private String getFilename(String fileUrl) {
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1, fileUrl.length());
    }
    private String getSaveFilename(String saveDir, String fileUrl, String filename) {
        String urlFileName = getFilename(fileUrl);
        if (StringUtils.isBlank(filename)) {
            filename = FilenameUtils.normalize(saveDir + File.separator + urlFileName);
        } else {
            filename = FilenameUtils.normalize(saveDir + File.separator
                    + FilenameUtils.getBaseName(filename) + "." + FilenameUtils.getExtension(urlFileName));
        }
        return filename;
    }

}
