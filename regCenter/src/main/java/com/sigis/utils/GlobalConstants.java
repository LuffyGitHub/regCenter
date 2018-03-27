package com.sigis.utils;

public class GlobalConstants {
	
	//zk服务器列表  
    public static final String ZKHOSTS = "172.26.99.126:2181,172.26.99.127:2181,172.26.99.128:2181";
    
    //连接的超时时间  
    public static final int SESSIONTIME = 2000;  
    
    //服务根目录
    public static final String SERVERS = "/servers";  
    
    //各个容器目录
    public static final String CONTAINER_ONE = "/container-one";  
    public static final String CONTAINER_TWO = "/container-two";  
    
}
