package com.quincy.benelli.controller;

import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.quincy.auth.AuthHelper;
import com.quincy.auth.annotation.LoginRequired;
import com.quincy.auth.annotation.PermissionNeeded;
import com.quincy.auth.o.XSession;
import com.quincy.benelli.service.BenelliService;
import com.quincy.sdk.RedisProcessor;
import com.quincy.sdk.annotation.JedisSupport;

import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/xxx")
public class XxxController {
//	@Autowired
//	private RedisProcessor redisProcessor;
	@Autowired
	private BenelliService benelliService;

//	@RequestMapping("/redis/set")
//	@ResponseBody
//	public String testRedis(@RequestParam(required = true, value = "k")String k, @RequestParam(required = true, value = "v")String v, @RequestParam(required = true, value = "e")int e) {
//		return redisProcessor.setAndExpire(k, v, e);
//	}

	@JedisSupport
	@RequestMapping("/redis/expire")
	@ResponseBody
	public long testRedis(@RequestParam(required = true, value = "k")String k, @RequestParam(required = true, value = "e")int e, Jedis jedis) {
		return jedis.expire(k, e);
	}

	@JedisSupport
	@RequestMapping("/redis/get")
	@ResponseBody
	public String testRedis(@RequestParam(required = true, value = "k")String k, Jedis jedis) {
		return jedis.get(k);
	}

	@RequestMapping("/redis/set2")
	@ResponseBody
	public String testRedis2(@RequestParam(required = true, value = "k")String k, @RequestParam(required = true, value = "v")String v, @RequestParam(required = true, value = "e")int e) throws IOException, SQLException {
		return benelliService.setRedis(k, v, e
//				, null
				, null
				, null);
	}

	@JedisSupport
	@RequestMapping("/redis/null")
	@ResponseBody
	public Long testRedis3(@RequestParam(required = true, value = "k")String k, Jedis jedis) {
		return jedis.del(k);
	}

	@LoginRequired
	@RequestMapping("/www")
	public ModelAndView www() {
		XSession xs = AuthHelper.getSession();
		return new ModelAndView("/www").addObject("user", xs.getUser());
	}

	@LoginRequired
	@RequestMapping("/zzz")
	@ResponseBody
	public String zzz(XSession session) {
		return "你好，"+session.getUser().getName();
	}

	@RequestMapping("/ttt")
	@ResponseBody
	public Result ttt() {
		Result result = new Result();
		result.setContent("BENELLI");
		return result;
	}

	@PermissionNeeded("ppp")
	@RequestMapping("/qqq")
	@ResponseBody
	public Result qqq() {
		Result result = new Result();
		result.setContent("tttqqq");
		return result;
	}

	public class Result {
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}