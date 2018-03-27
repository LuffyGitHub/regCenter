package com.sigis.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.sigis.utils.GlobalConstants;


/**
 * 监听zookeeper节点变化
 * @Title
 * @ClassName:ListenService
 * @author Luffy
 * @Description:TODO(用一句话描述这个类作用)
 * @date 2017年12月29日 上午10:03:31
 */


@Service
public class ListenService implements ApplicationListener<ContextRefreshedEvent>{

	
	private  ZooKeeper zookeeper;
    
	/**
	 * 封装成结果集返回
	 */
	private StringBuffer result = new StringBuffer(); 
	
	private int i = 0 ;
	
	/**
	 * @Title: getConstantsList
	 * @Description: TODO(获取所有容器节点的列表)key:代表容器节点目录,values:容器节点里面的值
	 * @param @return
	 * @param @throws KeeperException
	 * @param @throws InterruptedException
	 * @param @throws UnsupportedEncodingException    设定文件
	 * @return Map<String,String>    返回类型
	 * @throws
	 */
	public  Map<String,String> getConstantsList(){
		
			Map<String,String> mapServer = new HashMap<String,String>();
	        
	        List<String> subList;
			try {
					subList = zookeeper.getChildren(GlobalConstants.SERVERS, true);
				
			        for (String subNode : subList) {
			            //获取节点数据
			            byte[] data = zookeeper.getData(GlobalConstants.SERVERS + "/" + subNode, false, null);
						
			            mapServer.put(GlobalConstants.SERVERS + "/" + subNode, new String(data,"utf-8"));
			        }
		        
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		 
		 return mapServer;
	        
	}

	/**
	 * spring容器启动的时候执行监听,实时返回容器的状态
	 * @Title: onApplicationEvent
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param event    设定文件
	 * @return void    返回类型
	 * @throws
	 */
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		if(event.getApplicationContext().getParent() == null){  
			
			try {
				// 注册全局默认watcher
				zookeeper = new ZooKeeper(GlobalConstants.ZKHOSTS, 1000, new Watcher(){
					
				    public void process(WatchedEvent event) {
				    	
				    	if (event.getType() == EventType.NodeChildrenChanged && (GlobalConstants.SERVERS).equals(event.getPath())) {
				    		/**
				    		 * 每次节点有变化就刷新列表	
				    		 */
							getConstantsList();
							
				        }
				    }
				});
					// watcher注册后，只能监听事件一次，参数true表示继续使用默认watcher监听事件
					zookeeper.getChildren(GlobalConstants.SERVERS, true);
				
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("开启zookeeper服务注册中心监听.......");
        }  
	}
	
}
