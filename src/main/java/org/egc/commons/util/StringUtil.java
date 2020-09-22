package org.egc.commons.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
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
    private StringUtil() {
    }

    private static final char SEPARATOR = '_';

    /**
     * 移除第一个下划线
     *
     * @param src the src
     * @return string
     * @date 2016年2月1日下午5:38:54
     */
    public static String removeFirstUnderline(String src) {
        if (src.indexOf('_') == 0) {
            src = src.substring(1);
        }
        return src;
    }

    public static String[] stringSplit(String targetString, String splitString) {
        return targetString.split(splitString);
    }

    public static String toHexString(byte[] byteArray) {
        StringBuilder hexString = new StringBuilder("");
        if (byteArray == null || byteArray.length <= 0) {
            return null;
        }
        for (byte b : byteArray) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hexString.append(0);
            }
            hexString.append(hv);
        }
        return hexString.toString().toLowerCase();
    }

    public static List<Integer> findCharIndex(String src, String key) {
        List<Integer> indexList = new ArrayList<>();
        int index = src.indexOf(key);
        indexList.add(index);
        while (index != -1) {
            index = src.indexOf(key, index + 1);
            if (index != -1) {
                indexList.add(index);
            }

        }
        return indexList;
    }

    /**
     * 是否为有效的 URI（统一资源标识符）
     *
     * @param uri uri
     * @return boolean
     */
    public static boolean isUriValid(String uri) {
        Pattern pattern = RegexPatterns.WEB_URL;
        return pattern.matcher(uri).matches();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCapitalizeCamelCase(String s) {
        if (s == null) {
            return null;
        }
        s = toCamelCase(s);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase(" hello_world ") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            boolean nextUpperCase = true;

            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }

            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }

            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    public static void isNullOrEmptyPrecondition(String s, String msg) {
        s = Strings.emptyToNull(s);
        Preconditions.checkNotNull(s, msg);
    }


    static final Pattern p = Pattern.compile("\\s*|\t|\r|\n");

    /**
     * 去除字符串中的空格、回车、换行符、制表符等
     *
     * @param str the str
     * @return string
     */
    public static String replaceSpecialStr(String str) {
        String repl = "";
        if (str != null) {
            Matcher m = p.matcher(str);
            repl = m.replaceAll("");
        }
        return repl;
    }
}
