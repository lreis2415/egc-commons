package org.egc.commons.util;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Description:
 * <pre>
 *
 * </pre>
 *
 * @author houzhiwei
 * @date 2018 /10/8 22:21
 */
public class Formatter {
    private Formatter() {}

    /**
     * Format double.
     *
     * @param d      the double to format
     * @param digits the digits MaximumFractionDigits, 保留位数
     * @param mode   {@link RoundingMode}， 如四舍五入 {@link RoundingMode#HALF_UP}
     * @return the double formatted value
     */
    public static double formatDouble(double d, int digits, RoundingMode mode) {
        return Double.parseDouble(formatDoubleStr(d, digits, mode));
    }

    /**
     * format double with mode {@link RoundingMode#HALF_UP} 四舍五入
     *
     * @param d      the double to format
     * @param digits the digits MaximumFractionDigits, 保留位数
     * @return
     */
    public static String formatDoubleStr(double d, int digits) {
        return formatDoubleStr(d, digits, RoundingMode.HALF_UP);
    }

    /**
     * format double with mode {@link RoundingMode#HALF_UP} 四舍五入
     *
     * @param d      the double to format
     * @param digits the digits MaximumFractionDigits, 保留位数
     * @return
     */
    public static double formatDouble(double d, int digits) {
        return Double.parseDouble(formatDoubleStr(d, digits, RoundingMode.HALF_UP));
    }

    /**
     * Format double.
     *
     * @param d      the double to format
     * @param digits the digits MaximumFractionDigits, 保留位数
     * @param mode   {@link RoundingMode}， 如四舍五入 {@link RoundingMode#HALF_UP}
     * @return the double formatted value
     */
    public static String formatDoubleStr(double d, int digits, RoundingMode mode) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        // 5 位小数
        nf.setMaximumFractionDigits(digits);
        // 四舍五入
        nf.setRoundingMode(mode);
        // 是否分组，即每三位加逗号分隔
        nf.setGroupingUsed(false);
        return nf.format(d);
    }


}
