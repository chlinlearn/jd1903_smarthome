package com.briup.smarthome.client;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.Log;
import com.briup.smarthome.util.ConfigurationAware;
import com.briup.smarthome.util.ConfigurationImpl;
import com.briup.smarthome.util.LogImpl;

//网络模块--客户端
public class ClientImpl implements Client,ConfigurationAware{
    private static Log log;
    private String ip;
    private int port;
    private Configuration conf;


	@Override
	public void init(Properties properties) throws Exception {
		ip = properties.getProperty("ip");
		port = Integer.parseInt(properties.getProperty("port"));
	}

	@Override
	public void send(Collection<Environment> env) throws Exception {
		log = conf.getLogger();
		//发送数据到服务器端
        log.info("启动客户端...");
        Socket socket = new Socket(ip,port);
        log.info("客户端启动成功...");
        OutputStream os = socket.getOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os);
        log.info("客户端准备发送"+env.size()+"条数据...");
        oos.writeObject(env);//发送集合对象
        oos.flush();
        log.info("客户端数据发送成功...");
        oos.close();//关闭资源
	}

	@Override
	public void setConfiguration(Configuration conf) {
		this.conf = conf;
	}

}
