package com.ey.tax.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Created by zhuji on 2/6/2018.
 *
 * 整合 org.springframework.util.StringUtils 和 org.apache.commons.lang3.StringUtils
 */
public class StringUtil extends StringUtils{
    public static boolean hasText(CharSequence str) {
        return org.springframework.util.StringUtils.hasText(str);
    }

    public static boolean hasText(String str){
        return org.springframework.util.StringUtils.hasText(str);
    }

    public static boolean hasLength(final String str) {
        return org.springframework.util.StringUtils.hasLength((CharSequence) str);
    }

    /**
     * 去除所有字符串中的空白字符
     * <pre>
     * StringUtils.trimAllWhitespace(null)          = null
     * StringUtils.trimAllWhitespace("")            = ""
     * StringUtils.trimAllWhitespace("     ")       = ""
     * StringUtils.trimAllWhitespace(" ab c ")         = "abc"
     * StringUtils.trimAllWhitespace("    abc    ") = "abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String trimAllWhitespace(final String str){
        return org.springframework.util.StringUtils.trimAllWhitespace(str);
    }

    /**
     * 去除字符串开头指定的字符
     * <pre>
     * StringUtils.trimLeadingCharacter("_a_bc_", '_') = a_bc_
     * StringUtils.trimLeadingCharacter("a_bc_", '_') = a_bc_
     * </pre>
     * @param str
     * @param leadingCharacter
     * @return
     */
    public static String trimLeadingCharacter(String str, char leadingCharacter){
        return org.springframework.util.StringUtils.trimLeadingCharacter(str,leadingCharacter);
    }

    /**
     * 去除字符串末尾指定的字符
     * @param str
     * @param trailingCharacter
     * @return
     */
    public static String trimTrailingCharacter(String str, char trailingCharacter) {
        return org.springframework.util.StringUtils.trimTrailingCharacter(str,trailingCharacter);
    }

    /**
     * locale字符串转换成Locale对象
     * @param localeString
     * @return
     */
    public static Locale parseLocaleString(String localeString){
        return org.springframework.util.StringUtils.parseLocaleString(localeString);
    }

    /**
     * 获取文件名后缀
     * @param path
     * @return
     */
    public static String getFilenameExtension(String path) {
        return org.springframework.util.StringUtils.getFilenameExtension(path);
    }

    /**
     *  获取文件名称
     * @param path
     * @return
     */
    public static String getFilename(String path) {
        return org.springframework.util.StringUtils.getFilename(path);
    }

    /**
     * 在数组上追加元素
     * @param srcArray
     * @param separator
     * @param extraArray
     * @return
     */
    public static String join(String[] srcArray,final String separator,String... extraArray){
        int srclength = srcArray.length;
        int extralength = extraArray.length;
        String[] destArray = new String[srclength+extralength];
        System.arraycopy(srcArray, 0, destArray, 0, srclength);
        System.arraycopy(extraArray, 0, destArray, srclength, extralength);
        return join(destArray,separator);
    }
}
