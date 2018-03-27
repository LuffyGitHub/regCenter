package com.sigis.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.namespace.QName;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.sigis.entity.ContainerInfo;
import com.sigis.entity.MethodContentInfo;
import com.sigis.entity.ServiceInfo;
import com.sigis.service.ListenService;


/**
 * webservice服务发布Action
 * @Title
 * @ClassName:ServiceAction
 * @author Luffy
 * @Description:TODO(用一句话描述这个类作用)
 * @date 2017年12月29日 上午10:42:34
 */
@WebService
public class ListenAction {
	
	@Resource
	ListenService listenService;
	
	
	
	static SAXReader reader = new SAXReader();  
	
	/**
	 * 获取所有容器节点的列表
	 * @Title: getConstantsList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return    设定文件
	 * @return Map<String,String>    返回类型
	 * @throws
	 */
	public List<String> getConstantsList(){
		
			ServiceInfo serviceInfo = null;//服务信息封装
			
			ContainerInfo containerInfo = null;//容器信息封装
			
			String result = null;//容器结果集
			
			List<ServiceInfo> serviceInfos = new ArrayList<ServiceInfo>();//所有服务结果集
			
			List<String> resultList = new ArrayList<String>();//所有容器信息最终结果集
			
		try {
			
			/**
			 * 获取存在的所有容器信息
			 */
			Map<String, String> resultMap = listenService.getConstantsList();
			
			/**
			 * 容器数量wsdl循环
			 */
			for (String containerWsdl : resultMap.values()) {
				
				containerInfo = new ContainerInfo();
				
				List<String> serviceWsdls = getServiceWsdls(containerWsdl);
				
				/**
				 * 服务数量wsdl获取循环
				 */
				for (String wsdl : serviceWsdls) {
				
					Document document = reader.read(new URL(wsdl.replace("'", "")));
					
					Element rootElement = document.getRootElement();
		
					List<MethodContentInfo> listNodes = listNodes(rootElement);
					
					String portTypeName = getPortTypeName(rootElement);
					
					serviceInfo = new ServiceInfo();
					
					serviceInfo.setPortType(portTypeName);
					
					serviceInfo.setUrl(wsdl);
					
					serviceInfo.setOperations(listNodes);
					
					serviceInfos.add(serviceInfo);
				}
				
				containerInfo.setServiceInfos(serviceInfos);
				
			}
			
			result = JSONObject.fromObject(containerInfo).toString();
			
			resultList.add(result);
		
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		return resultList;
	}
	
	
	
	/**
	 * 获取所有方法以及其相关信息
	 * @Title: listNodes
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param node
	 * @param @return    设定文件
	 * @return List<MethodContentInfo>    返回类型
	 * @throws
	 */
	@WebMethod(exclude = true)
	public static  List<MethodContentInfo> listNodes(Element node){  
		
		Element nodeImport = node.element("types").element("schema").element("import");
		
		List<MethodContentInfo> listParamNodes = new ArrayList<MethodContentInfo>();
		
		if(nodeImport != null){

			Attribute attribute = nodeImport.attribute("schemaLocation");
			
			if(attribute != null){
				
				String url = attribute.getValue();
				
				if(StringUtils.isNotBlank(url)){
					
					listParamNodes = listParmaNodes(url);
				
				}else{
					
					System.out.println("访问参数URL为空");
				}
			}else{
				System.out.println("url属性不存在(schemaLocation)");
			}
		}else{
			System.out.println("url标签不存在(import)");
		}
		
		return listParamNodes;
		
	}
	
	/**
	 * 获取方法参数列表
	 * @Title: listParmaNodes
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param url
	 * @param @return    设定文件
	 * @return List<MethodContentInfo>    返回类型
	 * @throws
	 */
	@WebMethod(exclude = true)
	private static List<MethodContentInfo> listParmaNodes(String url) {
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
		List<MethodContentInfo> methods = new ArrayList<MethodContentInfo>();
		
		MethodContentInfo methodInfo = new MethodContentInfo();
		
		try {
			
				
			Document document = reader.read(new URL(url));
			
			Element rootElement = document.getRootElement();
	
			//获取complexType方法标签
			List<Element> elements = rootElement.elements("complexType");
			
			if(elements != null && elements.size() > 0){
			
				for (Element element: elements) {
					
					Attribute attribute = element.attribute("name");
					
					if(attribute != null){
						
						String methodName = attribute.getValue();
	
							//判断是否是方法名字还是Response响应名字,方法名字才需要遍历参数节点
							if(!methodName.endsWith("Response")){
								
								methodInfo.setMethodName(methodName);
								
								List<Element> paramElements = element.element("sequence").elements("element");
								
								resultMap = new HashMap<String,String>();
								
								if(paramElements != null && paramElements.size() > 0){
									//element参数标签
									for(Element paramElement : paramElements){
										if(paramElement != null){
											
											String paramName = paramElement.attribute("name").getValue();
											String paramType = paramElement.attribute("type").getValue().replace("xs:","");
											resultMap.put(paramName, paramType);
											
											methodInfo.setParams(resultMap);
											
										}else{
											System.out.println("方法参数element标签不存在");
										}
									}
								}
							}else{
								methodInfo.setResponse(methodName);
								
								methods.add(methodInfo);
								//初始化方法信息对象重新封装
								methodInfo = new MethodContentInfo();
							}
					}else{
						System.out.println("方法属性不存在(name)");
					}
					
				}
				
			}else{
				System.out.println("方法参数标签不存在(complexType)");
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return methods;  
	}   

	
	/**
	 * 获取portType名称(类名)
	 * @Title: getPortTypeName
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param node
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	@WebMethod(exclude = true)
	public static String getPortTypeName(Element node){
			
			//操作portType标签
			
			String className = null;
		
			Element portType = node.element("portType");
			
			if(portType != null){
				
				Attribute portTypeAttribute = portType.attribute("name");
				
				if(portTypeAttribute != null){
				
					className = portTypeAttribute.getValue();
				
				}else{
					System.out.println("portType中name属性不存在");
				}
				
			}else{
				System.out.println("portType标签不存在");
			}
			
		return className;
	}
	
	
	/**
	 * 获取所有服务的wsdl路径(每一个容器下面都有N个服务)
	 * @Title: getServiceWsdls
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param url
	 * @param @return    设定文件
	 * @return List<String>    返回类型
	 * @throws
	 */
	@WebMethod(exclude = true)
	public List<String> getServiceWsdls(String url){
	
		List<String> wsdls = new ArrayList<String>();
		
		Document document;
		
		try {
			
			if(StringUtils.isNotBlank(url)){
				
				document = reader.read(new URL(url.replace("'", "")));
			
				Element rootElement = document.getRootElement();
					
				String namespace = rootElement.attribute("targetNamespace").getValue();
				
				JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
				
				// url为调用webService的wsdl地址
		        Client client = dcf.createClient(url);
		        
		        String methodName = rootElement.element("types").element("element").attribute("name").getValue();
		        
		        // namespace是命名空间，methodName是方法名
		        QName name = new QName(namespace,methodName);
		        
	        	// paramvalue为参数值
	        	Object[] objects;
	            
	        	objects = client.invoke(name);
	            
	            for(Object o : objects) {
	            	wsdls.add(String.valueOf(o));
	            }
	            
			}else{
				System.out.println("容器url为空:" + url);
			}  
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
}
