package com.quincy.benelli.service;

import java.io.IOException;
import java.sql.SQLException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public interface BenelliService {
	public String setRedis(String k, String v, int e
			, Transaction tx
//			, JedisCluster jedisCluster
			, Jedis jedis) throws IOException, SQLException;
}