package com.briup.smarthome.client;

import java.io.*;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Collection;
import java.util.Date;

import com.briup.environment.bean.Environment;
import com.briup.environment.client.Gather;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.Log;
import com.briup.smarthome.util.ConfigurationAware;
import com.briup.smarthome.util.ConfigurationImpl;
import com.briup.smarthome.util.LogImpl;

//采集模块
public class GatherImpl implements Gather ,ConfigurationAware{
    private static Log log;
    private String src_file;
    private String record_file;
    private Configuration conf;
    
	@Override
	public void init(Properties properties) throws Exception {
		src_file = properties.getProperty("src-file");
		record_file = properties.getProperty("record-file");
	}

	/**
	 * 采集文件，并返回Environment集合对象
	 */
	@Override
	public Collection<Environment> gather() throws Exception {
		log = conf.getLogger();
		// 创建集合对象
		List<Environment> list = new ArrayList<Environment>();
		// 读取数据文件
		File file = new File(src_file);
		// 存储文件
		File file2 = new File(record_file);
		long len = 0;
		if (file2.exists()) {
		    BufferedReader br = new BufferedReader(new FileReader(file2));
		    len = Long.parseLong(br.readLine());
		}
		//读取基本数据类型
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file2));
		PrintWriter pw = new PrintWriter(dos);
		// 下次读取文件时跳过已读部分
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		raf.seek(len);
		long len2 = raf.length();//获取文件的长度
		pw.print(len2);
		pw.flush();
		
		String lineStr = null;
		
		// 每种环境的数据条数
		int count1 = 0;// 有效数据条数
		int count2 = 0;// 温度湿度
		int count3 = 0;// 光照
		int count4 = 0;// 二氧化碳
		while ((lineStr = raf.readLine()) != null) {
			Environment e = new Environment();
			// 注意分割需要转义
			String[] data = lineStr.split("\\|");
			// 判断数据是否缺失
			if (data.length != 9) {
				continue;
			}
			count1++;
			// 创建Environment对象,需要对数据进行格式转换
			e.setSrcId(data[0]);
			e.setDstId(data[1]);
			e.setDevId(data[2]);
			e.setSersorAddress(data[3]);
			e.setCount(Integer.parseInt(data[4]));
			e.setCmd(data[5]);
			e.setStatus(Integer.parseInt(data[7]));
			e.setGather_date(new Timestamp(Long.parseLong(data[8])));
			if ("16".equals(data[3])) {
				// 温度:((float)value＊0.00268127)-46.85
				Integer wd = Integer.parseInt(data[6].substring(0, 4), 16);
				Float f1 = (float) (((float) wd * 0.00268127) - 46.85);
				e.setName("温度");
				e.setData(f1);
				list.add(e);

				// 湿度:((float)value*0.00190735)-6
				Integer sd = Integer.parseInt(data[6].substring(4, 8), 16);
				Float f2 = (float) (((float) sd * 0.00190735) - 6);
                Environment e2 = new Environment("湿度",data[0],data[1],
                        data[2],data[3],Integer.parseInt(data[4]),data[5],
                        Integer.parseInt(data[7]),f2,
                        new Timestamp(Long.parseLong(data[8])));
				count2++;
				list.add(e2);
			} else if ("256".equals(data[3])) {// 光照
				Integer g = Integer.parseInt(data[6].substring(0, 4), 16);
				e.setName("光照强度");
				e.setData(g);
				// 并将Environment添加到集合中;
				count3++;
				list.add(e);
			} else {// 二氧化碳
				Integer g = Integer.parseInt(data[6].substring(0, 4), 16);
				e.setName("二氧化碳");
				e.setData(g);
				// 并将Environment添加到集合中
				count4++;
				list.add(e);
			}
		}

        log.debug("有效数据：" + count1);
        log.debug("温度湿度：" + count2);
        log.debug("光照强度：" + count3);
        log.debug("二氧化碳：" + count4);

		return list;
	}

	@Override
	public void setConfiguration(Configuration conf) {
		this.conf = conf;
	}

}
