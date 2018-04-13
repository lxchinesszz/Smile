package org.smileframework.tool.string;


import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

/**
 * 编码与解码操作工具类
 *
 * @author huangyong
 * @since 1.0
 */
public class CodecUtils {

    private static final Logger logger = LoggerFactory.getLogger(CodecUtils.class);
    private static final String UTF_8="utf-8";

    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;


    /**
     * 将字符串 MD5 加密
     */
    public static String encryptMD5(String str) {
        return DigestUtils.md5Hex(str);
    }


    /**
     * 将字符串 MD5 加密
     */
    public static String decodeMD5(String str) {
        return "";
    }

    /**
     * 创建随机数
     */
    public static int createRandom(int count) {
        Random random=new Random();
        return random.nextInt(count);
    }

    /**
     * 获取 UUID（32位）
     */
    public static String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
