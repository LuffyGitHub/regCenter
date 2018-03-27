package com.sigis.entity;

import java.util.List;


/**
 * 获取各个信息返回值对象
 * @Title
 * @ClassName:PortType
 * @author Luffy
 * @Description:TODO(用一句话描述这个类作用)
 * @date 2018年1月4日 上午9:30:46
 */
public class ServiceInfo {
	
	/**
	 * 服务类名,也就是标签portType中name属性值
	 */
	private String portType;
	
	/**
	 * 服务wsdl地址
	 */
	private String url;

	/**
	 * 服务里面所有方法的信息集合
	 */
	private List<MethodContentInfo> Operations;

	public String getPortType() {
		return portType;
	}

	public void setPortType(String portType) {
		this.portType = portType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MethodContentInfo> getOperations() {
		return Operations;
	}

	public void setOperations(List<MethodContentInfo> operations) {
		Operations = operations;
	}

	
}
