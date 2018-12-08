package org.smileframework.tool.string;

import com.google.common.collect.Maps;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smileframework.tool.http.BlmHttpsClients;
import java.util.*;

/**
 * @Package: org.smileframework.tool.string
 * @Description: 翻译英文
 * @author: liuxin
 * @date: 2018/4/13 上午10:49
 */
public class VarNameTools {
    private final static Map<String, String> matchEnglish = Maps.newHashMap();
    private final static Map<String, String> matchChinese = Maps.newHashMap();
    private final static Map<String, String> matchAllReplace = Maps.newHashMap();
    private final static Map<String, String> matchResultReplace = Maps.newHashMap();


    private enum Match {
        //英文匹配
        EN,
        //中文匹配
        ZH,
        //结果
        RESULT,
        //全部匹配
        ALL;
    }

    public enum Type {
        //自动翻译
        AUTO("auto"),
        //英文翻译
        EN("en"),
        //中文翻译
        ZH("zh");
        String type;

        Type(String type) {
            this.type = type;
        }
    }

    static {
        setWord("amountOfMoney","Amt",Match.RESULT);
        setWord("establish", "check");
        setWord("son", "sub");
        setWord("describe", "desc");
        setWord("number", "no");
        setWord("cell-phone", "phone");
        setWord("Information", "info");
        setWord("initialization", "init");
        setWord("accordingTo", "by");
        setWord("adopt", "by");
        setWord("transaction", "trade");
        setWord("passageway", "channel");
        setWord("obtain", "get");
        setWord("reach", "to");
        setWord("guide", "boot");
        setWord("packing", "wrapper");
        setWord("change", "onChanged");
        setWord("replica", "duplicate");
        setWord("decision", "resolve");
        setWord("whether", "is");
        setWord("close","closed");
        setWord("appoint", "assignable");
        setWord("injection", "inject");
        setWord("automatic", "auto");
        setWord("activation", "activate");
        setWord("jurisdiction", "access");
        setWord("autograph", "sign");
        setWord("available","usable");
        setWord("unavailable","unusable");
        setWord("blend","mix");
        setWord("maximum","Max");
        setWord("minimum","min");
        //替换单词前缀为$标识该单词不用驼峰处理
        setWord("Assembling", "$wired");
        setWord("theServer", "server");
        setWord("reflex", "reflect");
        setWord("agent", "proxy");
        setWord("inherit", "extends");
        setWord("payment","pay");
        setWord("whether","is",Match.ALL);
        setWord("realization", "implements");
        setWord("example", "example", Match.EN);
        setWord("根据", "通过", Match.ZH);
        setWord("添加", "添", Match.ZH);
        setWord("自带", "自定义", Match.ZH);
        setWord("检查", "核对", Match.ZH);
        setWord("副本", "复制品", Match.ZH);
        setWord("转换", "转换器", Match.ZH);
        setWord("是否是", "是否", Match.ZH);
        setWord("是否", "是否", Match.ZH);
        setWord("为", "-", Match.ZH);
        setWord("执行", "援引", Match.ZH);
        setWord("实例", "实例", Match.ZH);
        setWord("使用权", "权限", Match.ZH);
        setWord("运行", "运行", Match.ZH);
        setWord("路径", "路径", Match.ZH);
        setWord("域", "域", Match.ZH);
        setWord("配置", "config", Match.ZH);
        setWord("field", "field");
        setWord("field", "scope", Match.ALL);
        setWord("检查用户","checkUser",Match.ZH);
        setWord("route", "route");
        setWord("routine", "regular");
        setWord("routineA", "regular");
        setWord("to-configure", "config");
        setWord("function", "function");
        setWord("abnormal", "exception");
        setWord("synchronization ", "synchronized");
        setWord("example", "instance", Match.ALL);
        setWord("function", "run", Match.ALL);
        setWord("route", "path", Match.ALL);
        setWord("adopt","by",Match.ALL);
        setWord("replica", "duplicate",Match.ALL);
        setWord("数量","计数",Match.ZH);
        setWord("金额","amt",Match.ZH);
        setWord("关机","shutdown",Match.ZH);
        setWord("不支持","Unsupport",Match.ZH);
        setWord("设置","set",Match.ZH);
        setWord("创建","创造",Match.ZH);
        setWord("引用","reference",Match.ZH);
        setWord("表","桌子",Match.ZH);
    }

    public static void setWord(String srcWord, String targetWrod) {
       setWord(srcWord.toLowerCase(), targetWrod,Match.EN);
    }

    public static void setWord(String srcWord, String targetWrod, Match matchingType) {
        if (matchingType == Match.EN) {
            matchEnglish.put(srcWord.toLowerCase(), targetWrod);
        } else if (matchingType == Match.ALL) {
            matchAllReplace.put(srcWord, targetWrod);
        }else if (matchingType==Match.RESULT){
            matchResultReplace.put(srcWord,targetWrod);
        }
        else {
            matchChinese.put(srcWord, targetWrod);
        }
    }

    public static void main(String[] args) throws Exception {
        VarNameTools varNameTools = new VarNameTools();
        System.out.println(">>>>>>执行中文分词初始化操作>>>>>>>");
        String init = varNameTools.getName("执行初始化操作");
        System.out.println(">>>>>>执行初始化完成>>>>>>>");
        System.out.println("执行翻译请在单词前加 [-] eg: -age");
        Scanner scanner = new Scanner(System.in);
        System.out.println("---------------------------");
        System.out.println("      输入要查询的变量名称");
        System.out.println("---------------------------");
        System.out.print("输入: ");
        StringBuffer sb = new StringBuffer();
        while (true) {
            String name = "";
            if (scanner.hasNext()) {
                name = scanner.next();
            }
            name = varNameTools.getName(name);
            String format = String.format("输出: [%s]", name);
            sb.append(format).append("\n").
                    append("---------------------------").append("\n").append("输入: ");
            System.out.print(sb.toString());
            sb.delete(0, sb.toString().length());
        }
    }

    public String getName(String name) {
        Type type = Type.EN;
        if (name.contains("-")) {
            name = name.replace("-", "").trim();
            type = Type.AUTO;
        }
        List<Word> words = WordSegmenter.segWithStopWords(name);
        System.err.println("分词信息:" + words);
        StringBuilder varName = new StringBuilder();
        for (int i = 0, len = words.size(); i < len; i++) {
            String text = words.get(i).getText();
            boolean chineseMatching = matchChinese.containsKey(text);
            text = matchingChinese(text);
            varName.append(createSubName(translate(text, type), chineseMatching));
        }
        String uncapitalize = StringTools.uncapitalize(varName.toString());
        return formatOutput(uncapitalize);
    }



    private static List<JSONObject> translate(String translateText) {
        return translate(translateText, Type.EN);
    }

    /**
     * https://fanyi-api.baidu.com/api/trans/vip/translate
     * <p>
     * q
     * from zh
     * to  en
     * appid 20180413000145829
     * salt DQcaZDdYj7IHrSpHRfe9
     * sign
     *
     * @return
     */
    private static List<JSONObject> translate(String translateText, Type translateType) {
        List<JSONObject> results = new ArrayList<>();
        String q = translateText;
        String from = "auto";
        String to = translateType.type;
        String appid = "20180413000145829";
        String salt = String.valueOf(System.currentTimeMillis()).substring(0, 6);
        String miyao = "DQcaZDdYj7IHrSpHRfe9";
        String sign = CodecUtils.encryptMD5(appid + q + salt + miyao);
        String url = String.format("https://fanyi-api.baidu.com/api/trans/vip/translate?q=%s&from=%s&to=%s&appid=%s&salt=%s&sign=%s"
                , q, from, to, appid, salt, sign);
        String s = BlmHttpsClients.sendByGet(url, false);
        JSONObject jsonObject = new JSONObject(s);
        JSONArray trans_result = jsonObject.getJSONArray("trans_result");
        for (int i = 0, len = trans_result.length(); i < len; i++) {
            JSONObject resultJson = trans_result.getJSONObject(i);
            results.add(resultJson);
        }
        return results;
    }

    private static String createSubName(List<JSONObject> words, boolean chineseMatching) {
        StringBuilder varName = new StringBuilder();
        for (int i = 0, len = words.size(); i < len; i++) {
            JSONObject jsonObject = words.get(i);
            String[] dsts = jsonObject.getString("dst").split(" ");
            for (int j = 0, slent = dsts.length; j < slent; j++) {
                String replace = matchingEnglish(dsts[j].replace(",",""));
                if (replace != null) {
                    //说明中文和英文都匹配上则全部替换
                    if (chineseMatching) {
                        dsts[j] = matchingAll(dsts[j]);
                    } else {
                        dsts[j] = replace;
                    }
                }
                if (!dsts[j].contains("$")) {
                    dsts[j] = StringTools.capitalize(dsts[j]);
                } else {
                    dsts[j] = dsts[j].replace("$", "");
                }
                String capitalize = dsts[j];
                varName.append(capitalize);
            }
        }
        return varName.toString();
    }

    private static String matchingEnglish(String word) {
        String value = matchEnglish.get(word.toLowerCase());
        return !StringTools.isBlank(value) ? value : word;
    }


    private static String matchingChinese(String word) {
        String value = matchChinese.get(word.trim());
        return !StringTools.isBlank(value) ? value : word;
    }

    private static String matchingAll(String word) {
        String value = matchAllReplace.get(word.toLowerCase());
        return !StringTools.isBlank(value) ? value : word;
    }


    private static String formatOutput(String variableName) {
        return removeInUnderline(variableName);
    }

    public static String removeInUnderline(String variableName) {
        String[] split = variableName.split("-");
        if (split.length == 1) {
            return variableName;
        } else {
            StringBuilder varName = new StringBuilder();
            for (int i = 0; i < split.length; i++) {
                String capitalize = StringTools.capitalize(split[i]);
                varName.append(capitalize);
            }
            return StringTools.uncapitalize(varName.toString());
        }
    }
}
