package com.quincy.benelli.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.jar.JarException;

import org.springframework.stereotype.Service;

import com.quincy.benelli.service.BenelliService;
import com.quincy.sdk.annotation.JedisSupport;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

@Service
public class BenelliServiceImpl implements BenelliService {
	@JedisSupport(transactional = true, rollbackFor = {IOException.class})
//	@JedisInjector
	@Override
	public String setRedis(String k, String v, int e
			, Transaction tx
//			, JedisCluster jedisCluster
			, Jedis jedis) throws IOException, SQLException {
//		String status = jedis.set(k, v);
		Response<String> response = tx.set(k, v);
//		String status = response.get();
		String status = "OOKK";
		if(true)
			throw new JarException("Redis Test");
//			throw new SQLException("Redis Test");
//		jedis.expire(k, e);
		tx.expire(k, e);
		return status;
	}
}