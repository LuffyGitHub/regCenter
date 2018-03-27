/**
 * 
 */
/**
 * @Title
 * @ClassName:package-info
 * @author Luffy
 * @Description:TODO(用一句话描述这个类作用)
 * @date 2018年1月4日 上午9:23:02
 */
package com.sigis.entity;

import java.util.List;
import java.util.Map;


/**
 * 返回的json对象封装
 * @Title
 * @ClassName:JsonObject
 * @author Luffy
 * @Description:TODO(用一句话描述这个类作用)
 * @date 2018年1月4日 上午9:24:00
 */
public class MethodContentInfo{
	
	/**
	 * 方法名称
	 */
	private String methodName;
	
	/**
	 * 方法响回名称
	 */
	private String response;
	
	/**
	 * 方法形参参数
	 */
	private Map<String,String> params;

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	
}