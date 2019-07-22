package com.briup.smarthome.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
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

//采集模块
public class GatherImpl implements Gather {

	@Override
	public void init(Properties arg0) throws Exception {

	}

	/**
	 * 采集文件，并返回Environment集合对象
	 */
	@Override
	public Collection<Environment> gather() throws Exception {
		// 创建集合对象
		List<Environment> list = new ArrayList<Environment>();
		// 读取数据文件
		File file = new File("file/radwtmp");
		// 存储文件
		File file2 = new File("file/save");
		long len = 0;
		if (file2.exists()) {
			DataInputStream dis = new DataInputStream(new FileInputStream(file2));
			len = dis.readLong();
		}
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file2));
		dos.writeLong(0);//清空
		// 下次读取文件时跳过已读部分
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		raf.seek(len);
		long len2 = raf.length();//获取文件的长度
		dos.writeLong(len2);
		dos.flush();
		
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
				Environment e2 = new Environment();
				Integer sd = Integer.parseInt(data[6].substring(4, 8), 16);
				Float f2 = (float) (((float) sd * 0.00190735) - 6);
				e2.setName("湿度");
				e2.setData(f2);
				e2.setSrcId(data[0]);
				e2.setDstId(data[1]);
				e2.setDevId(data[2]);
				e2.setSersorAddress(data[3]);
				e2.setCount(Integer.parseInt(data[4]));
				e2.setCmd(data[5]);
				e2.setStatus(Integer.parseInt(data[7]));
				e2.setGather_date(new Timestamp(Long.parseLong(data[8])));
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


		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
		System.out.println("有效数据：" + count1);
		System.out.println("温度湿度：" + count2);
		System.out.println("光照：" + count3);
		System.out.println("二氧化碳：" + count4);

		// 关闭资源

		return list;
	}

	public static void main(String[] args) throws Exception {
		GatherImpl g = new GatherImpl();
		Collection<Environment> c = g.gather();
	}
}
