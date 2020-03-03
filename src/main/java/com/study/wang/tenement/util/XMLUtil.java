package com.study.wang.tenement.util;
 
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
 
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
/**
 *  XML解析工具类
 * @author 秋枫艳梦
 * @date 2019-06-02
 * */
public class XMLUtil {
 
    /**
     *  将XML转换成Map
     * @param inputStream 请求体中的输入流
     * @return Map
     * */
    public static Map<String,String> getMap(InputStream inputStream){
        //返回的Map
        Map<String,String> map = new HashMap<>();
        //初始化解析器
        SAXReader reader = new SAXReader();
 
        //读取XML文档
        Document document = null;
        try {
            document = reader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
 
        //获取XML文档的根节点
        Element element = document.getRootElement();
 
        //遍历所有节点，把键和值存入Map
        List<Element> elementList = element.elements();
        for (Element e : elementList) {
            map.put(e.getName(),e.getText());
        }
 
        return map;
    }
}