package com.quincy.benelli.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quincy.auth.annotation.LoginRequired;
import com.quincy.auth.annotation.PermissionNeeded;

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
	public String zzz() {
		return "zzz";
	}

	@RequestMapping("/ttt")
	@ResponseBody
	public Result ttt() {
		Result result = new Result();
		result.setContent("tttxxxx");
		return result;
	}

	@PermissionNeeded("ppp")
	@RequestMapping("/ppp")
	public String ppp() {
		return "www";
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