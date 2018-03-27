package com.sigis.entity;

import java.util.List;


/**
 * 容器实体类
 * @Title
 * @ClassName:ContainerInfo
 * @author Luffy
 * @Description:TODO(用一句话描述这个类作用)
 * @date 2018年1月12日 下午1:57:00
 */
public class ContainerInfo {
	
	/**
	 * 容器名称
	 */
	private String containerName;
	
	/**
	 * 容器里面所有服务结果集
	 */
	private List<ServiceInfo> serviceInfos;

	public List<ServiceInfo> getServiceInfos() {
		return serviceInfos;
	}

	public void setServiceInfos(List<ServiceInfo> serviceInfos) {
		this.serviceInfos = serviceInfos;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

}
