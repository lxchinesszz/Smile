package org.smileframework.tool.io;

import org.smileframework.tool.json.JsonUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Package: safety.bankpay.util
 * @Description: 流转换成String
 * @author: liuxin
 * @date: 2017/11/14 下午6:12
 */
public class StreamTools {



    public static <T> T convertStringToObject(BufferedReader reader, Class<T> cls) {
        String var1 = convertStreamToString(reader);
        return JsonUtils.fromJson(var1, cls);
    }

    public static String convertStreamToString(BufferedReader reader) {
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


    public static <T> T convertStringToObject(InputStream inputStream, Class<T> cls) {
        String var1 = convertStreamToString(inputStream);
        return JsonUtils.fromJson(var1, cls);
    }

    public static String convertStreamToString(InputStream is) {
        /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}