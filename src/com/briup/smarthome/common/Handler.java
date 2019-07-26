package com.briup.smarthome.common;

import java.sql.ResultSet;
import java.sql.SQLException;

/* *
 *@author:xuchunlin
 *@createTime:2019/7/15/16:16
 */
//处理结果集的规范
public interface Handler {
    public void handler(ResultSet rs) throws SQLException;
}
