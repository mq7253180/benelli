package com.quincy.benelli.service;

import redis.clients.jedis.Jedis;

public interface BenelliService {
	public void foo(Jedis jedis);
}
