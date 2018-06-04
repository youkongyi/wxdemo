package com.gee.blog.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.gee.blog.entity.TextMessage;
import com.thoughtworks.xstream.XStream;

/**
 * XML转Map工具类
 * 
 * @author "GeHanBiao"
 */
public class XmlParseMapUtils {

    private XmlParseMapUtils() {
        throw new RuntimeException("Error");
    }

    /**
     * @description： 输入流读取XML文件内容转换Map
     * @param inputStream  输入流
     * @param map  转换Map,可选重复集合IdentityHashMap
     * @return
     * @author："GeHanBiao"
     * @crateDate：2018年6月4日下午2:49:39
     */
    public static Map xmlToMap(InputStream inputStream,Map map) {
        try {
            // 获取文件
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(inputStream);
            // 获取根节点
            Element element = document.getRootElement();
            map = getNodes(element,map);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 从指定节点开始,递归遍历所有子节点 @description： 添加方法功能描述
     * 
     * @param node
     *            @author："GeHanBiao"
     * @crateDate：2018年6月4日上午9:57:07
     */
    private static Map getNodes(Element node,Map map) {
        if(map == null) {
            map = new IdentityHashMap<String,String>();
        }
        //是否有子节点
        if (node.getTextTrim() != null && !"".equals(node.getTextTrim())) {
            map.put(new String(node.getName()), node.getTextTrim());
        }
        // 当前节点的所有属性的list
        List<Attribute> listAttr = node.attributes();
        // 遍历当前节点的所有属性
        for (Attribute attr : listAttr) {
            String name = attr.getName();
            String value = attr.getValue();
            map.put(new String(name), value);
        }

        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();
        // 遍历所有一级子节点
        for (Element e : listElement) {
            getNodes(e,map);
        }
        return map;
    }

    /**
     * 文本消息转化为xml
     * 
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(TextMessage textMessage) {
        XStream xstream = new XStream();
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

//    public static void main(String[] args) throws FileNotFoundException {
//        Map map = XmlParseMapUtils.xmlToMap(new FileInputStream(new File("F:\\123.xml")),new HashMap<>());
//        System.out.println(map);
//        map = XmlParseMapUtils.xmlToMap(new FileInputStream(new File("F:\\456.xml")),null);
//        System.out.println(map);
//        Iterator iter = map.entrySet().iterator();
//        while(iter.hasNext()) {
//            Map.Entry entry = (Entry) iter.next();
//            System.out.println(entry.getKey()+" : "+entry.getValue());
//        }
//    }
}
