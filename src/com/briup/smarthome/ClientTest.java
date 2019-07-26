package com.briup.smarthome;

/* *
 * @Author: xuchunlin
 * @CreateDate: 2019/7/26/11:06
 * @Description: NULL
 * @Version: 1.0
 */

import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.util.Configuration;
import com.briup.smarthome.util.ConfigurationImpl;

public class ClientTest {

    public static void main(String[] args) throws Exception{
        Configuration config = new ConfigurationImpl();
        Client client = config.getClient();
        Gather gather = config.getGather();
        client.send(gather.gather());
    }
}
