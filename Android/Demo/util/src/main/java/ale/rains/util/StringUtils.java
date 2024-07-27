package ale.rains.util;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringUtils {
    private final static String BASE = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 获取非空字符串
     *
     * @param origin
     * @return
     */
    public static String nonNullString(CharSequence origin) {
        if (origin == null) {
            return "";
        }

        if (origin instanceof String) {
            return (String) origin;
        }

        return origin.toString();
    }

    /**
     * 查找是否包含
     *
     * @param origin
     * @param subString
     * @return
     */
    public static boolean contains(CharSequence origin, CharSequence subString) {
        if (origin == null || origin.length() == 0) {
            return false;
        }
        return origin.toString().contains(subString);
    }

    /**
     * 强制toString
     */
    public static String toString(Object item) {
        if (item == null) {
            return null;
        }
        if (item.getClass().isArray()) {
            return Arrays.toString((Object[]) item);
        }
        if (item instanceof String) {
            return (String) item;
        }
        return item.toString();
    }

    /**
     * 去除前后不可见符号
     */
    public static String trim(CharSequence origin) {
        return origin == null ? null : origin.toString().trim();
    }

    /**
     * 判断origin是否以sub开始
     *
     * @param origin 目标字段
     * @param sub    查找字段
     */
    public static boolean startWith(CharSequence origin, CharSequence sub) {
        if (TextUtils.isEmpty(origin) || TextUtils.isEmpty(sub) || origin.length() < sub.length()) {
            return false;
        }

        /**
         * 比较前n个字符
         */
        for (int i = 0; i < sub.length(); i++) {
            if (origin.charAt(i) != sub.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 比较字符串是否相等
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equals(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return s1.equals(s2);
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 比较字符串是否相等
     *
     * @param s1 The first string.
     * @param s2 The second string.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean equalsIgnoreCase(final CharSequence s1, final CharSequence s2) {
        if (s1 == s2) return true;
        int length;
        if (s1 != null && s2 != null && (length = s1.length()) == s2.length()) {
            if (s1 instanceof String && s2 instanceof String) {
                return ((String) s1).equalsIgnoreCase(((String) s2));
            } else {
                for (int i = 0; i < length; i++) {
                    if (s1.charAt(i) != s2.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 比较字符串是否相等或者左侧为空
     */
    public static boolean equalsOrLeftBlank(CharSequence a, CharSequence b) {
        // 两者都为空，相等
        if (a == null) {
            return true;
        }

        // 都不为空，直接比较
        return toString(a).equals(toString(b));
    }

    /**
     * tanslate charSequence to String
     *
     * @param cs charSquence
     * @return string类型
     */
    public static String safeCharSeqToString(CharSequence cs) {
        if (cs == null) {
            return "";
        } else {
            return cs.toString();
        }
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 字符串转换为UTF-8格式
     *
     * @param str 字符串
     * @return String
     */
    public static String utf8Encode(String str) {
        try {
            if (!TextUtils.isEmpty(str) && str.getBytes("UTF-8").length != str.length()) {
                return URLEncoder.encode(str, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            Logger.e(e);
        }
        return str;
    }

    /**
     * 字符串转换为UTF-8格式（有默认返回）
     *
     * @param str           字符串
     * @param defaultReturn 默认String类型字符
     * @return String
     */
    public static String utf8Encode(String str, String defaultReturn) {
        try {
            if (!TextUtils.isEmpty(str) && str.getBytes("UTF-8").length != str.length()) {

                return URLEncoder.encode(str, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            Logger.e(e);
            return defaultReturn;
        }
        return defaultReturn;
    }

    /**
     * 判断字符串a是否包含字符串b
     *
     * @param strA 字符串a
     * @param strB 字符串b
     * @return boolean
     */
    public static boolean includeStr(String strA, String strB) {
        return strA.contains(strB);
    }

    /**
     * 判断字符串数组a是否包含指定字符串b
     *
     * @param strA 字符串数组a
     * @param strB 字符串b
     * @return boolean
     */
    public static boolean allInclude(String[] strA, String strB) {
        boolean exist = false;
        for (String s : strA) {
            if (includeStr(strB, s)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    /**
     * 判断字符串数组是否包含指定字符串元素
     *
     * @param strAll 字符串数组
     * @param str    指定字符串
     * @return boolean
     */
    public static boolean strContains(String[] strAll, String str) {
        boolean exist = false;
        for (String s : strAll) {
            if (str.equals(s)) {
                exist = true;
                break;
            }
        }
        return exist;
    }


    /**
     * 格式化string
     *
     * @param str 字符串
     * @return String
     */
    public static String formatText(CharSequence str) {
        if (null == str) {
            return null;
        }
        String text = str.toString();
        text = text.replaceAll("−", "-");
        text = text.replaceAll("• ", "");
        text = text.replaceAll(" ", "");
        text = text.replaceAll("\\n", "");
        text = text.replaceAll("	", "");
        return text;
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return String   反转字符串
     */
    public static String reverseString(String s) {
        int len = s.length();
        if (len <= 1) {
            return s;
        }
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 把给定的字符串用给定的字符分割
     *
     * @param string 给定的字符串
     * @param ch     给定的字符
     * @return String[]分割后的字符串数组
     */
    public static String[] splitString(String string, char ch) {
        ArrayList<String> stringList = new ArrayList<>();
        char[] charArray = string.toCharArray();
        int nextStart = 0;
        for (int w = 0; w < charArray.length; w++) {
            if (ch == charArray[w]) {
                stringList.add(new String(charArray, nextStart, w - nextStart));
                nextStart = w + 1;
                if (nextStart == charArray.length) {
                    //当最后一位是分割符的话，就再添加一个空的字符串到分割数组中去
                    stringList.add("");
                }
            }
        }
        if (nextStart < charArray.length) {
            //如果最后一位不是分隔符的话，就将最后一个分割符到最后一个字符中间的左右字符串作为一个字符串添加到分割数组中去
            stringList.add(new String(charArray, nextStart,
                    charArray.length - 1 - nextStart + 1));
        }
        return stringList.toArray(new String[stringList.size()]);
    }

    /**
     * 删除给定字符串中给定位置处的字符
     *
     * @param string 给定字符串
     * @param index  给定位置
     * @return 删除给定位置字符后的字符串
     */
    public static String removeChar(String string, int index) {
        String result;
        char[] chars = string.toCharArray();
        if (index == 0) {
            result = new String(chars, 1, chars.length - 1);
        } else if (index == chars.length - 1) {
            result = new String(chars, 0, chars.length - 1);
        } else {
            result = new String(chars, 0, index) +
                    new String(chars, index + 1, chars.length - index);
        }
        return result;
    }

    /**
     * 将给定的字符串MD5加密
     *
     * @param string 给定的字符串
     * @return String  MD5加密后生成的字符串
     */
    public static String toMD5(String string) {
        String result = null;
        try {
            char[] charArray = string.toCharArray();
            byte[] byteArray = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }

            StringBuilder hexValue = new StringBuilder();
            byte[] md5Bytes = MessageDigest.getInstance("MD5")
                    .digest(byteArray);
            for (byte md5Byte : md5Bytes) {
                int val = ((int) md5Byte) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }

            result = hexValue.toString();
        } catch (Exception e) {
            Logger.e(e);
        }
        return result;
    }

    /**
     * 检查字符串长度，如果字符串的长度超过maxLength，就截取前maxLength个字符串并在末尾拼上appendString
     *
     * @param string       给定的字符串
     * @param maxLength    字符串长度
     * @param appendString 追加的字符串
     * @return String  指定长度字符串
     */
    public static String checkLength(String string, int maxLength, String appendString) {
        if (string.length() > maxLength) {
            string = string.substring(0, maxLength);
            if (appendString != null) {
                string += appendString;
            }
        }
        return string;
    }


    /**
     * 检查字符串长度，如果字符串的长度超过maxLength，就截取前maxLength个字符串并在末尾拼上…
     *
     * @param string    给定的字符串
     * @param maxLength 字符串长度
     * @return String  指定长度字符串
     */
    public static String checkLength(String string, int maxLength) {
        return checkLength(string, maxLength, "…");
    }

    /**
     * 连接字符串
     *
     * @param joiner
     * @param contents
     * @return
     */
    public static String join(CharSequence joiner, List<String> contents) {
        if (contents == null || contents.size() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contents.size() - 1; i++) {
            sb.append(contents.get(i)).append(joiner);
        }
        return sb.append(contents.get(contents.size() - 1)).toString();
    }

    /**
     * 正则替换
     *
     * @param origin
     * @param reg
     * @param to
     * @return
     */
    public static String patternReplace(CharSequence origin, CharSequence reg, CharSequence to) {
        if (origin == null) {
            return null;
        }

        if (reg == null) {
            return origin.toString();
        }

        if (to == null) {
            return origin.toString().replaceAll(reg.toString(), "null");
        } else {
            return origin.toString().replaceAll(reg.toString(), to.toString());
        }
    }

    /**
     * 数字格式
     */
    private static final Pattern NUMBER_PATTERN = Pattern.compile("[-+]?\\d+(\\.\\d+)?");

    /**
     * 是否为数字字符串
     */
    public static boolean isNumeric(CharSequence origin) {
        if (origin == null || origin.length() == 0) {
            return false;
        }
        return NUMBER_PATTERN.matcher(origin).matches();
    }

    /**
     * 正则替换
     *
     * @param origin  原始字段
     * @param pattern 正则模板
     * @param replace 替换方法
     */
    public static String patternReplace(CharSequence origin, Pattern pattern, PatternReplace replace) {
        if (origin == null || replace == null || pattern == null) {
            return null;
        }

        // 正则匹配下
        Matcher matcher = pattern.matcher(origin);
        StringBuilder sb = new StringBuilder();

        int currentIdx = 0;
        // 替换所有匹配到的字段
        while (matcher.find()) {
            // 添加之前的字段
            int start = matcher.start();
            sb.append(origin.subSequence(currentIdx, start));

            // 替换match字段
            String content = replace.replacePattern(matcher.group());
            sb.append(content);

            // 重置偏移量
            currentIdx = start + matcher.group().length();
        }

        // 如果还有其他字段
        if (currentIdx < origin.length()) {
            sb.append(origin.subSequence(currentIdx, origin.length()));
        }

        return sb.toString();
    }

    /**
     * 字符替换接口
     */
    public interface PatternReplace {
        String replacePattern(String origin);
    }

    /**
     * 生成长度为<tt>length</tt>的随机字符串
     *
     * @param length 生成长度
     * @return 随机字符串
     */
    public static String generateRandomString(int length) {
        StringBuilder builder = new StringBuilder(length);
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            int currentValue = random.nextInt(35);
            if (currentValue < 10) {
                builder.append(currentValue);
            } else {
                builder.append((char) ('a' + (currentValue - 10)));
            }
        }
        return builder.toString();
    }

    /**
     * 判断是否包含中文
     *
     * @param checkStr
     * @return
     */
    public static boolean containsChinese(CharSequence checkStr) {
        if (!TextUtils.isEmpty(checkStr)) {
            String checkChars = checkStr.toString();
            for (int i = 0; i < checkChars.length(); i++) {
                char checkChar = checkChars.charAt(i);
                if (checkCharContainChinese(checkChar)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkCharContainChinese(char checkChar) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(checkChar);
        return Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS == ub ||
                Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS == ub ||
                Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS == ub ||
                Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT == ub ||
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub ||
                Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B == ub;
    }

    /**
     * 取hash
     */
    public static String hash(Object content) {
        if (content == null) {
            return "FFFFFFFF##-1";
        } else {
            int length;
            if (content instanceof Collection) {
                length = ((Collection) content).size();
            } else if (content.getClass().isArray()) {
                length = Array.getLength(content);
            } else {
                String strVal = toString(content);
                length = strVal == null ? 0 : strVal.length();
            }

            return content.getClass().getSimpleName() + '@' + Integer.toHexString(content.hashCode()) + "##" + length;
        }
    }


    /**
     * 获取随机的字符串
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(BASE.charAt(random.nextInt(BASE.length())));
        }
        return sb.toString();
    }

    /**
     * 正则表达式匹配
     *
     * @param regex   正则表达式
     * @param content 匹配的字符串
     * @return 是否匹配成功
     */
    public static boolean patternMatch(String regex, String content) {
        if (TextUtils.isEmpty(regex)) {
            return true;
        }
        try {
            return Pattern.compile(regex).matcher(content).find();
        } catch (PatternSyntaxException e) {
            Logger.e("match pattern error: " + regex + ", content: " + content);
            return content.contains(regex);
        }
    }
}