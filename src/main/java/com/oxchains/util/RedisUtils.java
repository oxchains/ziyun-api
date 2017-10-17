package com.oxchains.util;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class RedisUtils {
    public static JedisCluster getJedisCluster(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        HostAndPort hostAndPort = new HostAndPort("192.168.1.16", 7000);
      /*  HostAndPort hostAndPort1 = new HostAndPort("192.168.1.16", 7001);
        HostAndPort hostAndPort2 = new HostAndPort("192.168.1.16", 7002);
        HostAndPort hostAndPort3 = new HostAndPort("192.168.1.10", 7003);
        HostAndPort hostAndPort4 = new HostAndPort("192.168.1.10", 7004);
        HostAndPort hostAndPort5 = new HostAndPort("192.168.1.10", 7005);*/
        nodes.add(hostAndPort);
      /*  nodes.add(hostAndPort1);
        nodes.add(hostAndPort2);
        nodes.add(hostAndPort3);
        nodes.add(hostAndPort4);
        nodes.add(hostAndPort5);*/
        JedisCluster jedisCluster = new JedisCluster(nodes, poolConfig);//JedisCluster中默认分装好了连接池.
        return jedisCluster;
    }
}
