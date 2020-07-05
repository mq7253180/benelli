package com.quincy.benelli.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.quincy.auth.annotation.LoginRequired;
import com.quincy.auth.annotation.PermissionNeeded;
import com.quincy.auth.o.XSession;
import com.quincy.benelli.service.BenelliService;
import com.quincy.o.AttributeKeys;
import com.quincy.o.MyParams;
import com.quincy.sdk.annotation.JedisInjector;

import redis.clients.jedis.Jedis;

@Controller
@RequestMapping("/xxx")
public class XxxController {
	@LoginRequired
	@RequestMapping("/www")
	public String www() {
		return "www";
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
		result.setContent("tttxxxx");
		return result;
	}

	@Autowired
	private BenelliService benelliService;

	@JedisInjector
	@PermissionNeeded("ppp")
	@RequestMapping("/ppp/{pathval}")
	public ModelAndView ppp(@PathVariable(required = true, name = "pathval")String pathVal, 
			@RequestParam(required = true, value = "param")String param, 
			HttpServletRequest request, 
			XSession session, 
			Jedis jedis) {
		benelliService.foo(null);
		MyParams myParams = (MyParams)session.getUser().getAttributes().get(AttributeKeys.MY_PARAMS);
		return new ModelAndView("www")
				.addObject("session", session)
				.addObject("pathVal", pathVal)
				.addObject("param", param)
				.addObject("attr", myParams.getXxx());
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