package com.briup.smarthome.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.Log;
import com.briup.smarthome.util.ConfigurationAware;
import com.briup.smarthome.util.ConfigurationImpl;
import com.briup.smarthome.util.LogImpl;

//网络模块--服务端
public class ServerImpl implements Server,ConfigurationAware{
    private ServerSocket ss = null;
    private int port;
    private Configuration conf;
    private Log log;

    @Override
	public void init(Properties properties) throws Exception {
		//获取端口号
    	port = Integer.parseInt(properties.get("port").toString());
	}

	@Override
	public Collection<Environment> reciver() throws Exception {
		log = conf.getLogger();
	    //启动服务器
		ss = new ServerSocket(port);
        log.info("服务器已启动...");
	    //接收客户端传来的数据，并将数据保存到数据库（根据时间来保存到不同的表）
        log.info("服务器等待客户端连接...");
        Socket socket = ss.accept();
        log.info("客户端连接成功...");
        InputStream is = socket.getInputStream();
        ObjectInputStream ois = new ObjectInputStream(is);

        log.info("服务器开始接收数据....");
        Object obj = ois.readObject();

        Collection<Environment> list = (Collection<Environment>) obj;

        log.info("服务器共接收到数据"+list.size()+"条...");
        int count1 = 0;
        int count2 = 0;
        int count3 = 0;
        for (Environment environment:list){
            if ("温度".equals(environment.getName())){
                count1++;
            }
            if ("光照强度".equals(environment.getName())){
                count2++;
            }
            if ("二氧化碳".equals(environment.getName())){
                count3++;
            }
        }
        log.info("服务器接收到[温度]的数据"+count1+"条...");
        log.info("服务器接收到[湿度]的数据"+count1+"条...");
        log.info("服务器接收到[光照强度]的数据"+count2+"条...");
        log.info("服务器接收到[二氧化碳]的数据"+count3+"条...");
		return list;
	}

	@Override
	public void shutdown() {
		
	}

	@Override
	public void setConfiguration(Configuration conf) {
		this.conf = conf;
	}

}
