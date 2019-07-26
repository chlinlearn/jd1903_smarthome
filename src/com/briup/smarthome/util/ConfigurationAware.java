package com.briup.smarthome.util;

import com.briup.environment.util.Configuration;

/**
*@Author: xuchunlin
*@CreateDate: 2019年7月26日 下午3:00:21
*@Description: 将配置模块的对象传递给其他模块
*/

public interface ConfigurationAware {
	void setConfiguration(Configuration conf);
}

