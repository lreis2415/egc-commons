package org.egc.commons.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.egc.commons.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

/**
 * TODO
 * 路径获取工具类
 *
 * @author houzhiwei
 * @date 2016 /11/23 16:06
 */
@Slf4j
public class PathUtil {
    /**
     * 项目根路径（绝对）<br/>
     * new File("").getCanonicalPath();
     *
     * @return project root
     */
    public static String getProjectRoot() {
        File file = new File("");
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new BusinessException(e, "Get project path failed");
        }
    }

    /**
     * （编译后）类根路径
     * <pre/>classes或test-classes
     *
     * @param clazz the clazz
     * @return class path
     */
    public static <T> String getClassPath(T clazz) {
        String path = null;
        try {
            path = clazz.getClass().getResource("").getPath();
        } catch (Exception e) {
            log.error(e.getMessage());
            path = getClassPath();
        }
        return path;
    }


    /**
     * file path in classes or test-classes (junit)
     *
     * @param file file name
     * @return string
     */
    public static String classFilePath(String file) {
        file = fileNormalize(file);
        URL resource = Thread.currentThread().getContextClassLoader().getResource(file);
        assert resource != null;
        return resource.getPath();
    }

    /**
     * Gets class path.
     * <pre/>classes or test-classes
     *
     * @return the class path
     */
    public static String getClassPath() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        assert resource != null;
        return resource.getPath();
    }

    /**
     * Gets package path.
     *
     * @param clazz the clazz
     * @return the package path
     */
    public static <T> String getPackagePath(T clazz) {
        return clazz.getClass().getResource("").getPath();
    }

    /**
     * Gets WEB-INF path.
     *
     * @param request the request
     * @return the web inf root
     */
    public static String getWebInfRoot(HttpServletRequest request) {
        return request.getSession().getServletContext().getRealPath("/");
    }

    /**
     * <pre>Gets current relative path
     * {@link #getCWD()}
     *
     * @return the relative path
     */
    public static String getRelativePath() {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    /**
     * <pre>current working directory
     * a complete absolute path from where the application is initialized.
     * System.getProperty("user.dir"))
     * {@link #getRelativePath()}
     *
     * @return cwd
     */
    public static String getCWD() {
        return System.getProperty("user.dir");
    }

    /**
     * Gets context path.
     *
     * @param request the request
     * @return the context path
     */
    public static String getContextPath(HttpServletRequest request) {
        return request.getContextPath();
    }

    /**
     * Gets the absolute path of file in "src/main/resources/".<br/>
     * the path of target/classes
     *
     * @return the path string
     */
    public static <T> String resourcesPath(T clazz) {
        ClassLoader classLoader = clazz.getClass().getClassLoader();
        return classLoader.getResource("").getFile();
    }

    /**
     * path of file in target/classes <br/>
     * original locates in src/main/resources in maven project <br/>
     *
     * @param <T>      type of the class where you call this function
     * @param clazz    the clazz
     * @param filename the filename, should not start with "/"!
     * @return string string
     */
    public static <T> String resourcesFilePath(T clazz, String filename) {
        ClassLoader classLoader = clazz.getClass().getClassLoader();
        filename = fileNormalize(filename);
        try {
            return Paths.get(classLoader.getResource(filename).toURI()).toString();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * Gets multiple level parent files.
     * 获取往上一定级别的目录
     *
     * @param filepath the filepath
     * @param levels   the levels, relative to the current directory
     * @return the multi level parent files
     */
    public static File getMultiLevelParentFiles(String filepath, int levels) {
        File parentFile = new File(filepath);
        for (int i = 0; i < levels; i++) {
            // if out of the range
            if (parentFile.getParentFile() == null) {
                return parentFile;
            }
            parentFile = parentFile.getParentFile();
        }
        return parentFile;
    }

    private static String fileNormalize(String file) {
        if (StringUtils.isBlank(file)) {
            log.warn("file is blank");
            return "";
        }
        file = FilenameUtils.normalize(file);
        if (file.startsWith(File.separator)) {
            file = file.substring(1);
        }
        return file;
    }
}
