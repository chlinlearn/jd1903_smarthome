package com.briup.smarthome.common;

/* *
 * @author: xuchunlin
 * @createTime: 2019/7/15/14:35
 * @description: 工厂类，用来获取Connection对象
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionFactory {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    static {
        //加载db.properties文件
        Properties p = new Properties();
        try {
            /*p.load(new FileReader(
                    "src/com/briup/common/db.properties"));*/
            p.load(ConnectionFactory.class.getResourceAsStream("db.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("文件加载失败");
        }
        driver = p.getProperty("driver");
        url = p.getProperty("url");
        username = p.getProperty("username");
        password = p.getProperty("password");
    }

    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,username,password);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("数据库连接失败");
        }
        return conn;
    }

}
