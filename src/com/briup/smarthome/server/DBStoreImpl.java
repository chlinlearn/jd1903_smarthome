package com.briup.smarthome.server;

/* *
 * @author: xuchunlin
 * @createTime: 2019/7/23/9:13
 * @description: null
 */

import com.briup.environment.bean.Environment;
import com.briup.environment.server.DBStore;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.Log;
import com.briup.smarthome.common.ConnectionFactory;
import com.briup.smarthome.util.ConfigurationAware;
import com.briup.smarthome.util.ConfigurationImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class DBStoreImpl implements DBStore ,ConfigurationAware{
    private static Log log;
    private int batch_size;
    private Configuration conf;

    @Override
    public void init(Properties properties) throws Exception {
    	batch_size = Integer.parseInt(properties.get("batch-size").toString());
    }

    /**
     *  保存数据到数据库(批处理,事务同时成功同时失败)
     * @param collection
     * @throws Exception
     */
    @Override
    public void saveDb(Collection<Environment> collection) throws Exception {
        log = conf.getLogger();
    	//连接数据库
        log.info("服务端开始连接数据库...");
        Connection conn = ConnectionFactory.getConnection();
        log.info("服务端数据库连接成功...");
        //根据不同的日期存入不同的表中
        conn.setAutoCommit(false);
        PreparedStatement pst = null;
        int count = 0;
        log.info("服务端开始写入数据到数据库..."+collection.size()+"条");
        //存储key=day,value=pst
        HashMap<String,PreparedStatement> map = new HashMap<>();
        //存储key=day,value=preTable_count(每张表的数据量)
        HashMap<String,Integer> map2 = new HashMap<>();
        for (Environment environment : collection) {
            //获取日期天数的方法1
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(environment.getGather_date().getTime()));
            String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            //获取日期天数的方法2
            /*SimpleDateFormat sdf = new SimpleDateFormat("d");
            Date date = new Date(environment.getGather_date().getTime());
            String day = sdf.format(date);*/

            //获取日期天数的方法3
            /*String[] time = environment.getGather_date().toString().split("[- ]");//获取日期,根据日期判断存入哪张表
            String day = time[2];*/
            //如果是同一张表就可以用同一个PreparedStatement对象
            if (!map.containsKey(day)) {
                String sql = "insert into t_detail_" + day + " values(?,?,?,?,?,?,?,?,?)";
                pst = conn.prepareStatement(sql);
                map.put(day, pst);
                map2.put(day,1);
            }else {
                pst = map.get(day);
                int currCount = map2.get(day)+1;
                map2.put(day, currCount);
            }
            pst.setString(1, environment.getName());
            pst.setString(2, environment.getSrcId());
            pst.setString(3, environment.getDstId());
            pst.setString(4, environment.getSersorAddress());
            pst.setInt(5, environment.getCount());
            pst.setString(6, environment.getCmd());
            pst.setInt(7, environment.getStatus());
            pst.setFloat(8, environment.getData());
            pst.setTimestamp(9, environment.getGather_date());
            pst.addBatch();//批处理
            count++;
            if (count % batch_size == 0) {
                pst.executeBatch();
            }
        }
        if (pst!=null) {
            pst.executeBatch();
            pst.close();
        }
        conn.commit();//提交
        log.info("服务端写入数据库成功...");

        conn.close();

        //输出每张表有多少数据
        Set keys = map2.keySet();
        for (Object key:keys){
            String k = (String)key;
            String table_name = "t_detail_"+k;
            int val = map2.get(k);
            log.debug(table_name+"表中插入数据"+val+"条");
        }
    }

	@Override
	public void setConfiguration(Configuration conf) {
		this.conf = conf;
	}

}
