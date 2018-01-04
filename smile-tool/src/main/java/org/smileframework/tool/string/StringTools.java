package org.smileframework.tool.string;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.smileframework.tool.common.Default;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Package: smile.string
 * @Description: 处理String校验
 * @author: liuxin
 * @date: 2017/11/30 上午11:02
 */
public final class StringTools {

    private static String ISNUMBER = "[0-9]*";



    public static boolean hasText(CharSequence str) {
        if(!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasText(String str) {
        return hasText((CharSequence)str);
    }
    public static String simpleClassName(Class cls) {
        return cls.getSimpleName();
    }

    /**
     * 等于null 长度等于=0 都是true
     *
     * @param cs
     * @return
     */
    public static Boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * 判读是否是数字
     *
     * @param cs
     * @return
     */
    public static Boolean isNumber(CharSequence cs) {
        Pattern pattern = Pattern.compile(ISNUMBER);
        Matcher isNum = pattern.matcher(cs);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 当cs不为空返回true
     *
     * @param cs
     * @return
     */
    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 不为空白
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    /**
     * 任意一个参数为空就true
     *
     * @param css A null B
     * @return false
     */
    public static boolean isAnyBlank(CharSequence... css) {
        CharSequence[] arr$ = css;
        int len$ = css.length;
        for (int i$ = 0; i$ < len$; ++i$) {
            CharSequence cs = arr$[i$];
            if (isBlank(cs)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 强调大小写
     *
     * @param cs1 A
     * @param cs2 a
     * @return false
     */
    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        return cs1 != null && cs2 != null && cs1.toString().equals(cs2.toString());
    }

    /**
     * 忽略大小写
     *
     * @param str1 a
     * @param str2 A
     * @return true
     */
    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return str1 != null && str2 != null && str1.toString().toLowerCase().equalsIgnoreCase(str2.toString());
    }

    /**
     * seq 中是否包含 searchSeq
     *
     * @param seq       abc
     * @param searchSeq a
     * @return true
     */
    public static boolean contains(CharSequence seq, CharSequence searchSeq) {
        return seq != null && searchSeq != null && seq.toString().contains(searchSeq.toString());
    }

    /**
     * src 中分隔符
     *
     * @param str        1,2|3;4
     * @param delimiters ,?;
     * @return 1 2 3 4
     */
    public static String[] tokenizeToStringArray(String str, String delimiters) {
        return tokenizeToStringArray(str, delimiters, true, true);
    }

    public static String[] tokenizeToStringArray(String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
        if (str == null) {
            return null;
        } else {
            StringTokenizer st = new StringTokenizer(str, delimiters);
            ArrayList tokens = new ArrayList();

            while (true) {
                String token;
                do {
                    if (!st.hasMoreTokens()) {
                        return toStringArray((Collection) tokens);
                    }

                    token = st.nextToken();
                    if (trimTokens) {
                        token = token.trim();
                    }
                } while (ignoreEmptyTokens && token.length() <= 0);

                tokens.add(token);
            }
        }
    }

    public static String[] toStringArray(Collection<String> collection) {
        return collection == null ? null : (String[]) collection.toArray(new String[collection.size()]);
    }


    public static String removeStart(String str, String remove) {
        return !isEmpty(str) && !isEmpty(remove) ? (str.startsWith(remove) ? str.substring(remove.length()) : str) : str;
    }

    public static String removeStartIgnoreCase(String str, String remove) {
        return !isEmpty(str) && !isEmpty(remove) ? (startsWithIgnoreCase(str, remove) ? str.substring(remove.length()) : str) : str;
    }

    public static boolean startsWithIgnoreCase(CharSequence str, CharSequence prefix) {
        return startsWith(str, prefix, true);
    }

    private static boolean startsWith(CharSequence str, CharSequence prefix, boolean ignoreCase) {
        return str != null && prefix != null ? (prefix.length() > str.length() ? false : regionMatches(str, ignoreCase, 0, prefix, 0, prefix.length())) : str == null && prefix == null;
    }

    public static String removeEnd(String str, String remove) {
        return !isEmpty(str) && !isEmpty(remove) ? (str.endsWith(remove) ? str.substring(0, str.length() - remove.length()) : str) : str;
    }

    public static String removeEndIgnoreCase(String str, String remove) {
        return !isEmpty(str) && !isEmpty(remove) ? (endsWithIgnoreCase(str, remove) ? str.substring(0, str.length() - remove.length()) : str) : str;
    }

    public static String remove(String str, String remove) {
        return !isEmpty(str) && !isEmpty(remove) ? replace(str, remove, "", -1) : str;
    }

    public static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        if (!isEmpty(text) && !isEmpty(searchString) && replacement != null && max != 0) {
            int start = 0;
            int end = text.indexOf(searchString, start);
            if (end == -1) {
                return text;
            } else {
                int replLength = searchString.length();
                int increase = replacement.length() - replLength;
                increase = increase < 0 ? 0 : increase;
                increase *= max < 0 ? 16 : (max > 64 ? 64 : max);

                StringBuilder buf;
                for (buf = new StringBuilder(text.length() + increase); end != -1; end = text.indexOf(searchString, start)) {
                    buf.append(text.substring(start, end)).append(replacement);
                    start = end + replLength;
                    --max;
                    if (max == 0) {
                        break;
                    }
                }

                buf.append(text.substring(start));
                return buf.toString();
            }
        } else {
            return text;
        }
    }


    public static String remove(String str, char remove) {
        if (!isEmpty(str) && str.indexOf(remove) != -1) {
            char[] chars = str.toCharArray();
            int pos = 0;

            for (int i = 0; i < chars.length; ++i) {
                if (chars[i] != remove) {
                    chars[pos++] = chars[i];
                }
            }

            return new String(chars, 0, pos);
        } else {
            return str;
        }
    }

    public static boolean endsWithIgnoreCase(CharSequence str, CharSequence suffix) {
        return endsWith(str, suffix, true);
    }

    private static boolean endsWith(CharSequence str, CharSequence suffix, boolean ignoreCase) {
        if (str != null && suffix != null) {
            if (suffix.length() > str.length()) {
                return false;
            } else {
                int strOffset = str.length() - suffix.length();
                return regionMatches(str, ignoreCase, strOffset, suffix, 0, suffix.length());
            }
        } else {
            return str == null && suffix == null;
        }
    }

    public static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        } else {
            int index1 = thisStart;
            int index2 = start;
            int var8 = length;

            while (var8-- > 0) {
                char c1 = cs.charAt(index1++);
                char c2 = substring.charAt(index2++);
                if (c1 != c2) {
                    if (!ignoreCase) {
                        return false;
                    }

                    if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    /**
     * 如果cs1为空,就返回默认值
     *
     * @param cs1 null
     * @param cs2 test
     * @return test
     */
    public static String defaultString(CharSequence cs1, CharSequence cs2) {
        return Default.defaultValue(cs1, cs2, new Default.DefaultIF<String>() {
            @Override
            public boolean defaultIf() {
                return isNotEmpty(cs1);
            }
        });
    }

    /**
     * 字符串翻转
     *
     * @param str
     * @return
     */
    public static String reverse(String str) {
        return str == null ? null : (new StringBuilder(str)).reverse().toString();
    }

    /**
     * 最多显示maxWidth长度,其他为省略
     *
     * @param str
     * @param maxWidth
     * @return
     */
    public static String abbreviate(String str, int maxWidth) {
        return abbreviate(str, 0, maxWidth);
    }

    public static String abbreviate(String str, int offset, int maxWidth) {
        if (str == null) {
            return null;
        } else if (maxWidth < 4) {
            throw new IllegalArgumentException("Minimum abbreviation width is 4");
        } else if (str.length() <= maxWidth) {
            return str;
        } else {
            if (offset > str.length()) {
                offset = str.length();
            }

            if (str.length() - offset < maxWidth - 3) {
                offset = str.length() - (maxWidth - 3);
            }

            String abrevMarker = "...";
            if (offset <= 4) {
                return str.substring(0, maxWidth - 3) + "...";
            } else if (maxWidth < 7) {
                throw new IllegalArgumentException("Minimum abbreviation width with offset is 7");
            } else {
                return offset + maxWidth - 3 < str.length() ? "..." + abbreviate(str.substring(offset), maxWidth - 3) : "..." + str.substring(str.length() - (maxWidth - 3));
            }
        }
    }

    /**
     * 包装
     *
     * @param str
     * @param wrapWith
     * @return
     */
    public static String wrap(String str, char wrapWith) {
        return !isEmpty(str) && wrapWith != 0 ? wrapWith + str + wrapWith : str;
    }

    public static String wrap(String str, String wrapWith) {
        return !isEmpty(str) && !isEmpty(wrapWith) ? wrapWith.concat(str).concat(wrapWith) : str;
    }

    /**
     * 行
     *
     * @param str
     * @param width
     * @return
     */
    public static String drawColumn(String str, int width) {
        int currWidth = str.toCharArray().length;
        if (currWidth < width) {
            str += " ";
            return drawColumn(str, width);
        }
        return str;
    }

    /**
     * @param commandPrint 是否在命令行打印
     * @param columnWidth  宽度
     * @param str          列数据
     * @return
     */
    public static String drawColumns(boolean commandPrint, int columnWidth, String... str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|");
        String columnEnd = "|";
        Arrays.asList(str).forEach(x -> {
            stringBuilder.append(drawColumn(Pinyin(x), columnWidth) + columnEnd);
        });
        if (commandPrint) {
            System.out.println(stringBuilder.toString());
        }
        return stringBuilder.toString();
    }

    public static void drawHeader(int columnWidth, String... str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|");
        String columnEnd = "|";
        Arrays.asList(str).forEach(x -> {
            stringBuilder.append(drawColumn(Pinyin(x), columnWidth) + columnEnd);
        });
        drawLine(true, "-", stringBuilder.toString().length());
        System.out.println(stringBuilder.toString());
        drawLine(true, "-", stringBuilder.toString().length());
    }

    /**
     * @param commandPrint true 在命令行打印
     * @param cs           线段格式
     * @param width        宽度
     * @return
     */
    public static String drawLine(boolean commandPrint, CharSequence cs, int width) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < width; i++) {
            stringBuffer.append(cs.toString());
        }
        if (commandPrint) {
            System.out.println(stringBuffer.toString());
        }
        return stringBuffer.toString();
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return
     */
    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (!hasLength(str)) {
            return str;
        } else {
            char baseChar = str.charAt(0);
            char updatedChar;
            if (capitalize) {
                updatedChar = Character.toUpperCase(baseChar);
            } else {
                updatedChar = Character.toLowerCase(baseChar);
            }

            if (baseChar == updatedChar) {
                return str;
            } else {
                char[] chars = str.toCharArray();
                chars[0] = updatedChar;
                return new String(chars, 0, chars.length);
            }
        }
    }

    public static boolean hasLength(CharSequence str) {
        return str != null && str.length() > 0;
    }


    public static String Pinyin(String src) {
        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        for (int i = 0; i < t0; i++) {
            // 判断是否为汉字字符
            if (Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
                try {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
                t4 += capitalize(t2[0]);
            } else {
                t4 += Character.toString(t1[i]);
            }

        }
        return t4;
    }

    /**
     * 移除换行符
     *
     * @param s
     * @return
     */
    public static String removeRN(String s) {
        return s.replaceAll("\r|\n", "");
    }


    /**
     * 验证邮箱
     *
     * @return 如果是符合的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean isEmail(String str) {
        String regex = "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        return match(regex, str);
    }

    /**
     * 验证IP地址
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean isIP(String str) {
        String num = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";
        String regex = "^" + num + "\\." + num + "\\." + num + "\\." + num + "$";
        return match(regex, str);
    }

    /**
     * 验证网址Url
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsUrl(String str) {
        String regex = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
        return match(regex, str);
    }

    /**
     * 验证电话号码
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsTelephone(String str) {
        String regex = "^(\\d{3,4}-)?\\d{6,8}$";
        return match(regex, str);
    }

    /**
     * 验证输入密码条件(字符与数据同时出现)
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsPassword(String str) {
        String regex = "[A-Za-z]+[0-9]";
        return match(regex, str);
    }

    /**
     * 验证输入密码长度 (6-18位)
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsPasswLength(String str) {
        String regex = "^\\d{6,18}$";
        return match(regex, str);
    }

    /**
     * 验证输入邮政编号
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsPostalcode(String str) {
        String regex = "^\\d{6}$";
        return match(regex, str);
    }

    /**
     * 验证输入手机号码
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsHandset(String str) {
        String regex = "^[1]+[3,5]+\\d{9}$";
        return match(regex, str);
    }

    /**
     * 验证输入身份证号
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsIDcard(String str) {
        String regex = "(^\\d{18}$)|(^\\d{15}$)";
        return match(regex, str);
    }

    /**
     * 验证输入两位小数
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsDecimal(String str) {
        String regex = "^[0-9]+(.[0-9]{2})?$";
        return match(regex, str);
    }

    /**
     * 验证输入一年的12个月
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsMonth(String str) {
        String regex = "^(0?[[1-9]|1[0-2])$";
        return match(regex, str);
    }

    /**
     * 验证输入一个月的31天
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsDay(String str) {
        String regex = "^((0?[1-9])|((1|2)[0-9])|30|31)$";
        return match(regex, str);
    }

    /**
     * 验证日期时间
     *
     * @return 如果是符合网址格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean isDate(String str) {
        String regex = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";
        return match(regex, str);
    }

    /**
     * 验证数字输入
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsNumber(String str) {
        String regex = "^[0-9]*$";
        return match(regex, str);
    }

    /**
     * 验证非零的正整数
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsIntNumber(String str) {
        String regex = "^\\+?[1-9][0-9]*$";
        return match(regex, str);
    }

    /**
     * 验证大写字母
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsUpChar(String str) {
        String regex = "^[A-Z]+$";
        return match(regex, str);
    }

    /**
     * 验证小写字母
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsLowChar(String str) {
        String regex = "^[a-z]+$";
        return match(regex, str);
    }

    /**
     * 验证验证输入字母
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsLetter(String str) {
        String regex = "^[A-Za-z]+$";
        return match(regex, str);
    }

    /**
     * 验证验证输入汉字
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsChinese(String str) {
        String regex = "^[\u4e00-\u9fa5],{0,}$";
        return match(regex, str);
    }

    /**
     * 验证验证输入字符串
     *
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     * @str 待验证的字符串
     */
    public static boolean IsLength(String str) {
        String regex = "^.{8,}$";
        return match(regex, str);
    }

    /**
     * @param regex 正则表达式字符串
     * @param str   要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }

    public static String arrayToDelimitedString(Object[] arr, String delim) {
        if(ObjectTools.isEmpty(arr)) {
            return "";
        } else if(arr.length == 1) {
            return ObjectTools.nullSafeToString(arr[0]);
        } else {
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < arr.length; ++i) {
                if(i > 0) {
                    sb.append(delim);
                }

                sb.append(arr[i]);
            }

            return sb.toString();
        }
    }

    public static String arrayToCommaDelimitedString(Object[] arr) {
        return arrayToDelimitedString(arr, ",");
    }


    public static void main(String[] args) throws Exception{
        int i=0;
     while (true){
         System.out.println(i++);
     }
/**
 *
 drawColumns(false, 10, "lilei", "14", "beijing");
 drawColumns(false, 10, "xiaohong", "12", "shanghai");
 drawColumns(false, 15, " 名字", " 年龄", " 学校");
 drawColumns(false, 15, " 李磊", " 14", " 北京大学");
 drawColumns(false, 15, " 熊爱红", " 12", " 上海交大");

 System.out.println("哈".getBytes().length);//汉字3个字符
 System.out.println("A".getBytes().length);//字母一个字符
 System.out.println(" ".getBytes().length);//空格也是一个字符

 System.out.println("哈".toCharArray().length);//汉字一个字符
 System.out.println("A".toCharArray().length);//字母一个字符
 System.out.println(" ".toCharArray().length);//空格也是一个字符
 */

    }
}
