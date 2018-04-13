package org.smileframework.tool.string;

import com.google.common.collect.Maps;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.Word;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smileframework.tool.http.BlmHttpsClients;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @Package: org.smileframework.tool.string
 * @Description: 翻译英文
 * @author: liuxin
 * @date: 2018/4/13 上午10:49
 */
public class VarNameTools {
    private final static String PreUrl = "http://www.baidu.com/s?wd=";                        //百度搜索URL
    private final static String TransResultStartFlag = "<span class=\"op_dict_text2\">";      //翻译开始标签
    private final static String TransResultEndFlag = "</span>";                               //翻译结束标签
    private final static Map<String, String> replaceMap = Maps.newHashMap();

    public static void setWord(String srcWord,String targetWrod){
        replaceMap.put(srcWord.toLowerCase(),targetWrod);
    }

    public static void main(String[] args) throws Exception {
        setWord("establish","check");
        setWord("son","sub");
        List<Word> words = WordSegmenter.segWithStopWords("生成报表");
        System.out.println();
        StringBuilder varName = new StringBuilder();
        for (int i = 0, len = words.size(); i < len; i++) {
            String text = words.get(i).getText();
            varName.append(createSubName(fanyi(text)));
        }
        String uncapitalize = StringTools.uncapitalize(varName.toString());
        System.out.println(uncapitalize);
    }

    private static String getTranslateResult(String urlString) throws Exception {    //传入要搜索的单词
        URL url = new URL(PreUrl + urlString);            //生成完整的URL
        // 打开URL
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        // 得到输入流，即获得了网页的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String preLine = "";
        String line;
        int flag = 1;
        // 读取输入流的数据，并显示
        String content = "";          //翻译结果
        while ((line = reader.readLine()) != null) {            //获取翻译结果的算法
            if (preLine.indexOf(TransResultStartFlag) != -1 && line.indexOf(TransResultEndFlag) == -1) {
                content += line.replaceAll("　| ", "") + "\n";   //去电源代码上面的半角以及全角字符
                flag = 0;
            }
            if (line.indexOf(TransResultEndFlag) != -1) {
                flag = 1;
            }
            if (flag == 1) {
                preLine = line;
            }
        }
        return content;//返回翻译结果}
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
    private static List<JSONObject> fanyi(String query) {
        List<JSONObject> results = new ArrayList<>();
        String q = query;
        String from = "zh";
        String to = "en";
        String appid = "20180413000145829";
        String salt = String.valueOf(System.currentTimeMillis()).substring(0, 6);
        String miyao = "DQcaZDdYj7IHrSpHRfe9";
        String sign = CodecUtils.encryptMD5(appid + q + salt + miyao);
        String url = String.format("https://fanyi-api.baidu.com/api/trans/vip/translate?q=%s&from=%s&to=%s&appid=%s&salt=%s&sign=%s"
                , q, from, to, appid, salt, sign);
        String s = BlmHttpsClients.sendByGet(url,false);
        JSONObject jsonObject = new JSONObject(s);
        JSONArray trans_result = jsonObject.getJSONArray("trans_result");
        for (int i = 0, len = trans_result.length(); i < len; i++) {
            JSONObject jsonObject1 = trans_result.getJSONObject(i);
            results.add(jsonObject1);
        }
        return results;
    }

    private static String createSubName(List<JSONObject> words) {
        StringBuilder varName = new StringBuilder();
        for (int i = 0, len = words.size(); i < len; i++) {
            JSONObject jsonObject = words.get(i);
            String[] dsts = jsonObject.getString("dst").split(" ");
            for (int j = 0, slent = dsts.length; j < slent; j++) {
                String replace = isReplace(dsts[j]);
                if (replace != null) {
                    dsts[j] = replace;
                }
                String capitalize = StringTools.capitalize(dsts[j]);
                varName.append(capitalize);
            }
        }
        return varName.toString();
    }

    private static String isReplace(String word) {
        String value = replaceMap.get(word.toLowerCase());
        return word=!StringTools.isBlank(value)?value:null;
    }
}