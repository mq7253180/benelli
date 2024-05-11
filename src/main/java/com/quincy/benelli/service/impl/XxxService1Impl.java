package com.quincy.benelli.service.impl;

import org.springframework.stereotype.Service;

import com.quincy.benelli.service.XxxService;
import com.quincy.sdk.annotation.Synchronized;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class XxxService1Impl implements XxxService {
	@Override
	public String xxx() {
		return "111";
	}

	@Synchronized("xxx")
	@Override
	public void test() throws InterruptedException {
		log.warn("CCC=======BEFORE");
		Thread.sleep(6000);
		log.warn("CCC=======AFTER");
	}
}