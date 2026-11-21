package com.hjay.sdp.create.factory.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 咖啡工厂类
 */
public class CoffeeFactory {

    /**
     * 1.定义容器对象存储咖啡对象
     * */
    private static Map<String, Coffee> map = new HashMap<>();

    /**
     * 2.加载配置文件
     */
    static {
        // 2.1. 创建properties对象
        Properties properties = new Properties();
        // 2.2. 加载配置文件
        InputStream inputStream = CoffeeFactory.class.getClassLoader().getResourceAsStream("bean.properties");
        try {
            properties.load(new InputStreamReader(inputStream));
            // 2.3. 获取全类名并创建对象
            Set<Object> keys = properties.keySet();
            for (Object key : keys) {
                String className = properties.getProperty((String) key);
                Class<?> clazz = Class.forName(className);
                Coffee coffee = (Coffee) clazz.newInstance();
                map.put((String)  key, coffee);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取咖啡
     * @param name
     * @return
     */
    public static Coffee createCoffee(String name){
        return map.get(name);
    }
}
