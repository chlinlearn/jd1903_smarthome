package com.briup.smarthome.util;

import java.util.*;

import com.briup.environment.util.WossModuleInit;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.Log;
import com.briup.smarthome.client.ClientImpl;
import com.briup.smarthome.client.GatherImpl;
import com.briup.smarthome.server.DBStoreImpl;
import com.briup.smarthome.server.ServerImpl;

/**
*@Author: xuchunlin
*@CreateDate: 2019年7月25日 下午4:21:33
*@Description: null
*/

//将自己的对象注入到其他模块
public class ConfigurationImpl implements Configuration{
    private Map<String, WossModuleInit> map;//存放实例对象
    public ConfigurationImpl() {
		this("src/com/briup/smarthome/util/config.xml");
	}

	private ConfigurationImpl(String path) {
	    map = new HashMap<>();
        Properties p = new Properties();
		//解析配置文件
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(path);
			Element root = document.getRootElement();
			List list = root.elements();
			for (Object temp:list){
			    Element em = (Element)temp;
			    String key = em.getName();
			    String val = em.attributeValue("class");
			    WossModuleInit woss = (WossModuleInit) Class.forName(val).newInstance();
			    map.put(key,woss);
			    List pros = em.elements();
			    for (Object pro:pros){
                    Element tag = (Element)pro;        
                    p.setProperty(tag.getName(),tag.getText());
                }
			    woss.init(p);
			    //把配置模块的对象传给其他模块
			    ((ConfigurationAware)woss).setConfiguration(this);
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Client getClient() throws Exception {
		return (Client)map.get("client");
	}

	@Override
	public DBStore getDbStore() throws Exception {
		return (DBStore) map.get("dbstore");
	}

	@Override
	public Gather getGather() throws Exception {
		return (Gather) map.get("gather");
	}

	@Override
	public Log getLogger() throws Exception {
		return (Log)map.get("logger");
	}

	@Override
	public Server getServer() throws Exception {
		return (Server)map.get("server");
	}

}

