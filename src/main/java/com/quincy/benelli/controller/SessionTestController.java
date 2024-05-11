package com.quincy.benelli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quincy.MyInit;
import com.quincy.benelli.service.XxxService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/xxx")
public class SessionTestController {
	private final static String ATTR_KEY = "attrKey";
	@Value("${server.port}")
	private String port;
	@Value("${spring.data.redis.host}")
	private String redisHost;
	@Value("${spring.data.redis.port}")
	private String redisPort;

	@RequestMapping("/exists")
	@ResponseBody
	public String exists(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String sessionStatus = session==null?"NULL":"EXISTS";
		return port+"========="+sessionStatus;
	}

	@RequestMapping("/set")
	@ResponseBody
	public String set(HttpServletRequest request, @RequestParam(required = true, value = "flag")String flag) {
		HttpSession session = request.getSession();
		session.setAttribute(ATTR_KEY, flag);
		return "OK======"+port;
	}

	@RequestMapping("/get")
	@ResponseBody
	public String get(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String toRet = port+"========"+(session==null?"Session is NULL":String.valueOf(session.getAttribute(ATTR_KEY)));
		return toRet;
	}

	@RequestMapping("/cancel")
	@ResponseBody
	public String cancel(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session!=null)
			session.invalidate();
		return "OK======"+port;
	}

	@RequestMapping("/host")
	@ResponseBody
	public String host() {
		return redisHost+":"+redisPort;
	}

	@RequestMapping("/sessionid")
	@ResponseBody
	public String sessionid(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		return request.getRequestedSessionId()+"================="+(session==null?"null":session.getId());
	}

	@Autowired
	private XxxService xxxService;
	@Autowired
	private MyInit myInit;

	@RequestMapping("/test")
	@ResponseBody
	public void test() throws InterruptedException {
		xxxService.test();
	}

	@RequestMapping("/init")
	@ResponseBody
	public void init() throws InterruptedException {
		myInit.test();
	}
}
