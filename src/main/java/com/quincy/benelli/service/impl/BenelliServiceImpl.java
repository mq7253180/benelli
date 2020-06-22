package com.quincy.benelli.service.impl;

import org.springframework.stereotype.Service;

import com.quincy.benelli.service.BenelliService;
import com.quincy.sdk.annotation.JedisInjector;

import redis.clients.jedis.Jedis;

@Service
public class BenelliServiceImpl implements BenelliService {
	@JedisInjector
	@Override
	public void foo(Jedis jedis) {
		System.out.println("===========BENELLI_SERVICE");
	}
}
