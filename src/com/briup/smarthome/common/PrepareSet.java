package com.briup.smarthome.common;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/* *
 *@author:xuchunlin
 *@createTime:2019/7/15/16:18
 */
//定义传入值的规范
public interface PrepareSet {
    public void setter(PreparedStatement pst) throws SQLException;
}
