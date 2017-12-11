package org.smileframework.tool.string;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smileframework.tool.asserts.Assert;


/**
 * @Package: dove.hnnx.util
 * @Description: 配置解析器根据
 * @author: liuxin
 * @date: 2017/11/10 上午10:45
 */
public class ConfigParser {
    private static final Logger logger = LoggerFactory.getLogger(ConfigParser.class);

    /**
     * @param srcData     要解析的数据改数据一定要包含 {DATA} 大括号包括的
     * @param replaceData 被替换的内容
     * @param appCode     要替换的内容
     * @return
     */
    public static String parser(String srcData, String replaceData, String appCode) {
        Assert.notNull(srcData, "srcData参数不能为空");
        if (!srcData.contains("{") || !srcData.contains("}")) {
            logger.error("参数:{},一定要包含\\{  \\}表达式", srcData);
        }
        return srcData.replaceAll("\\{" + replaceData.toUpperCase() + "\\}", appCode);
    }

    /**
     * 默认替换{APPCODE}
     *
     * @param srcData    原始串
     * @param targetData 将{APPCODE}替换成目标
     * @return
     */
    public static String parser(String srcData, String targetData) {
        Assert.notNull(srcData, "srcData参数不能为空");
        if (!srcData.contains("{") || !srcData.contains("}")) {
            logger.error("参数:{},一定要包含\\{  \\}表达式", srcData);
        }
        return srcData.replaceAll("\\{APPCODE\\}", targetData);
    }

}
