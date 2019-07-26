package com.briup.smarthome;

/* *
 * @Author: xuchunlin
 * @CreateDate: 2019/7/26/11:06
 * @Description: NULL
 * @Version: 1.0
 */

import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server;
import com.briup.environment.util.Configuration;
import com.briup.smarthome.util.ConfigurationImpl;

public class ServerTest {
    public static void main(String[] args) throws Exception{
        Configuration config = new ConfigurationImpl();
        Server server = config.getServer();
        DBStore dbStore = config.getDbStore();
        dbStore.saveDb(server.reciver());
    }
}
