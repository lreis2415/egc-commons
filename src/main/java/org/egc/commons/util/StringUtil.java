package org.egc.commons.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <pre/>字符串操作工具类
 * 一般的判空等字符串操作使用
 * {@link com.google.common.base.Strings} 或 {@link org.apache.commons.lang3.StringUtils}
 *
 * @author houzhiwei
 * @date 2017/6/29 16:31
 */
public class StringUtil {
    /**
     * 移除第一个下划线
     *
     * @param src the src
     * @return string
     * @date 2016年2月1日下午5:38:54
     */
    public static String removeFirstUnderline(String src)
    {
        if (src.indexOf('_') == 0)
            src = src.substring(1);
        return src;
    }

    public static String[] stringSplit(String targetString, String splitString){
        String[] sourceStrArray = targetString.split(splitString);
        return sourceStrArray;
    }

    public static List findCharIndex(String src, String key){
        List indexList = new ArrayList();
        int index = src.indexOf(key);
        indexList.add(index);
        while(index != -1){
            index = src.indexOf(key, index + 1);
            if (index != -1){
                indexList.add(index);
            }

        }
        return indexList;
    }

    /**
     * 是否为有效的 URI（统一资源标识符）
     *
     * @param uri
     * @return
     */
    public static boolean isUriValid(String uri) {
        Pattern pattern = RegexPatterns.WEB_URL;
        return pattern.matcher(uri).matches();
    }}
