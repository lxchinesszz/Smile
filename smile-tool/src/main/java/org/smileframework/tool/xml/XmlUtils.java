package org.smileframework.tool.xml;

import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Package: dove.hnnx.util
 * @Description:
 * @author: liuxin
 * @date: 2017/11/10 上午11:57
 */
public class XmlUtils {

    private static final Logger logger = LoggerFactory.getLogger(XmlUtils.class);

    /**
     * map转XMl
     * @param map
     * @return
     */
    public static String mapToXML(Map map) {
        logger.debug("将Map转成Xml, Map:{}", map.toString());
        StringBuffer sb = new StringBuffer();
        mapToXMLParse(map, sb);
        logger.debug("将Map转成Xml, Xml:{}" , sb.toString());
        try {
            return sb.toString();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    private static void mapToXMLParse(Map map, StringBuffer sb) {
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value) {
                value = "";
            }
            if (value.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++) {
                    HashMap hm = (HashMap) list.get(i);
                    mapToXMLParse(hm, sb);
                }
                sb.append("</" + key + ">");

            } else {
                if (value instanceof HashMap) {
                    sb.append("<" + key + ">");
                    mapToXMLParse((HashMap) value, sb);
                    sb.append("</" + key + ">");
                } else {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }

            }

        }
    }


    /**
     * xml 转换为Map对象
     * @param xml
     * @return
     */
    public static Map xmlToMap(String xml) {
        logger.debug("xml参数:{}",xml);
        return XML.toJSONObject(xml).toMap();
    }


}
