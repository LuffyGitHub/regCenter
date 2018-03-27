package com.sigis.action;

import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;


public class TestCxfUseWsdl {
	
	public static void main(String[] args) {
		
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		
		// url为调用webService的wsdl地址
        Client client = dcf.createClient("http://localhost:8080/regCenter/ws?wsdl");
        
        QName name = new QName("http://action.sigis.com/", "getConstantsList");
        
        // namespace是命名空间，methodName是方法名
        // paramvalue为参数值
        Object[] objects;
        try {
            objects = client.invoke(name);
            for(Object o : objects) {
            	System.out.println(o.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}
	
}
