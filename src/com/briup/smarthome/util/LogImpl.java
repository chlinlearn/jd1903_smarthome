package com.briup.smarthome.util;

import com.briup.environment.util.Configuration;

/* *
 * @author: xuchunlin
 * @createTime: 2019/7/25/10:50
 * @description: 日志模块实现类
 */

import com.briup.environment.util.Log;
import org.apache.log4j.Logger;

import java.util.Properties;

public class LogImpl implements Log ,ConfigurationAware{
    private final Logger log = Logger.getRootLogger();

    @Override
    public void debug(String s) {
        log.debug(s);
    }

    @Override
    public void info(String s) {
        log.info(s);
    }

    @Override
    public void warn(String s) {
        log.warn(s);
    }

    @Override
    public void error(String s) {
        log.error(s);
    }

    @Override
    public void fatal(String s) {
        log.fatal(s);
    }

    @Override
    public void init(Properties properties) throws Exception {
    
    }

	@Override
	public void setConfiguration(Configuration conf) {
		
	}
}
