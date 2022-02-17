package com.lag.io;


import java.io.InputStream;

/*自定义持久层框架第一步*/
public class Resources {

    //根据配置文件的路径，将配置文件加载成字节输入流，存储在内存中
    public static InputStream getResourceAsSteam(String path){
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);

        return resourceAsStream;
    }
}
