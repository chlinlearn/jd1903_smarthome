package com.briup.smarthome.server;

import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.server.Server;

//网络模块--服务端
public class ServerImpl implements Server{

	@Override
	public void init(Properties arg0) throws Exception {
		
	}

	@Override
	public Collection<Environment> reciver() throws Exception {
		return null;
	}

	@Override
	public void shutdown() {
		
	}
}
