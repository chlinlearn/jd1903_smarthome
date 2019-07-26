package com.briup.smarthome.common;
/* *
 *@author: xuchunlin
 *@createTime: 2019/7/15/16:21
 *@description: 模板类，封装jdbc的操作
 */

import java.sql.*;

public class JDBCTemplate {
    private Connection conn;

    public JDBCTemplate(){
        this(null);
    }

    public JDBCTemplate(Connection conn){
        if (conn==null){
            conn = ConnectionFactory.getConnection();
        }
        this.conn=conn;
    }

    //执行没有传入值，也没有结果集处理的SQL语句（DML语句）
    public void execute(String sql){
        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(st,null);
        }
    }

    //执行有传入值，没有结果集处理的SQL语句（DML语句）
    public void execute(String sql,PrepareSet setter){
        PreparedStatement pst = null;
        try {
            pst = conn.prepareStatement(sql);
            //传入值
            if (setter!=null)setter.setter(pst);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pst,null);
        }
    }

    //执行没有传入值，有结果集处理的SQL语句（查询语句）
    public void execute(String sql,Handler handler){
        Statement st = null;
        ResultSet rs = null;
        try {
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (handler!=null)handler.handler(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(st,null);
        }
    }

    //执行有传入值，有结果集处理的SQL语句（查询语句）
    public void execute(String sql,PrepareSet setter,Handler handler){
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            if (setter!=null)setter.setter(pst);
            rs = pst.executeQuery();
            if (handler!=null)handler.handler(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs,pst,null);
        }
    }

    public static void main(String[] args) {
        JDBCTemplate t = new JDBCTemplate();
        //String sql = "delete from s_stu";
        //t.execute(sql);
        /*String sql = "insert into s_stu(id,name,age) values(?,?,?)";
        t.execute(sql, new PrepareSet() {
            @Override
            public void setter(PreparedStatement pst) throws SQLException {
                pst.setInt(1,1);
                pst.setString(2,"tom");
                pst.setInt(3,20);
            }
        });*/
        /*String sql = "select name from s_stu";
        t.execute(sql, new Handler() {
            @Override
            public void handler(ResultSet rs) throws SQLException {
                while (rs.next()){
                    System.out.println(rs.getString(1));
                }
            }
        });*/
        String sql = "select name from s_stu where id=?";
        t.execute(sql, new PrepareSet() {
            @Override
            public void setter(PreparedStatement pst) throws SQLException {
                pst.setInt(1,1);
            }
        }, new Handler() {
            @Override
            public void handler(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    System.out.println(rs.getString(1));
                }
            }
        });

    }
}
