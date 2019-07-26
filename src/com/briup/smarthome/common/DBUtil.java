package com.briup.smarthome.common;

/* *
 * @author: xuchunlin
 * @createTime: 2019/7/15/15:03
 * @description: null
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUtil {
    public static void close(ResultSet rs, Statement st, Connection conn){
        try {
            if (rs!=null)rs.close();
            if(st!=null)st.close();
            if (conn!=null)conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void close(Statement st, Connection conn){
        close(null,st,conn);
    }

    public static void close(Connection conn){
        close(null,null,conn);
    }
}
